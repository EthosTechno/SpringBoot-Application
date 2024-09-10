/**
 * This work is licensed under the MIT License
 *
 * Configurable idle (no activity) timer and logout redirect for jQuery.
 * Works across multiple windows and tabs from the same domain.
 *
 * Dependencies: JQuery v1.7+, JQuery UI, store.js from https://github.com/marcuswestin/store.js - v1.3.4+
 * version 1.0.8
 **/

/*global jQuery: false, document: false, store: false, clearInterval: false, setInterval: false, setTimeout: false, window: false, alert: false*/
/*jslint indent: 2, sloppy: true*/

(function ($) {

  $.fn.idleTimeout = function (options) {

    //##############################
    //## Configuration Variables
    //##############################
    var defaults = {
      idleTimeLimit: 1680,       // 'No activity' time limit in seconds. 1200 = 20 Minutes
      redirectUrl: 'logout.jsp',    // redirect to this url on timeout logout. Set to "redirectUrl: false" to disable redirect

      // optional custom callback to perform before logout
      customCallback: false,     // set to false for no customCallback
      // customCallback:    function () {    // define optional custom js function
          // perform custom action before logout
      // },

      // configure which activity events to detect
      // http://www.quirksmode.org/dom/events/
      // https://developer.mozilla.org/en-US/docs/Web/Reference/Events
      activityEvents: 'click keypress scroll wheel mousewheel mousemove', // separate each event with a space

      // warning dialog box configuration
      enableDialog: true,        // set to false for logout without warning dialog
      dialogDisplayLimit: 120,   // time to display the warning dialog before logout (and optional callback) in seconds. 180 = 3 Minutes
      dialogTitle: 'Session Expiration Warning',
      dialogText: 'Your session is about to expire.',
      
      keepAlivePinged : true,

      // server-side session keep-alive timer
      sessionKeepAliveTimer: false // Ping the server at this interval in seconds. 600 = 10 Minutes
      // sessionKeepAliveTimer: false // Set to false to disable pings
    },

    //##############################
    //## Private Variables
    //##############################
      opts = $.extend(defaults, options),
      checkHeartbeat = 180, // frequency to check for timeouts in seconds
      origTitle = document.title, // save original browser title
      sessionKeepAliveUrl = window.location.href, // set URL to ping to user's current window
      keepSessionAlive, activityDetector,
      idleTimer, remainingTimer, checkIdleTimeout, idleTimerLastActivity, startIdleTimer, stopIdleTimer,
      openWarningDialog, dialogTimer, checkDialogTimeout, startDialogTimer, stopDialogTimer, isDialogOpen, destroyWarningDialog,
      countdownDisplay, logoutUser;

    //##############################
    //## Private Functions
    //##############################
    
    // Function not In use
    // This function checks if the user is logged in or not on intervals 
    keepSessionAlive = function () {

      if (opts.sessionKeepAliveTimer) {
        var keepSession = function () {        	

          if (idleTimerLastActivity === store.get('idleTimerLastActivity')) {
            $.get(sessionKeepAliveUrl);
          }
          else if(store.get('idleTimerLoggedOut') === true){
        		logoutUser();
        	}
        };

        setInterval(keepSession, (opts.sessionKeepAliveTimer * 1000));
      }
    };

    // Function In use
    // This function detect for activities and closes the modal(if opened) and start the timer 
    activityDetector = function () {

      $('body').on(opts.activityEvents, function () {
    	  
    	  if ($('#idletimer_warning_dialog').length > 0 &&
                  $('#idletimer_warning_dialog').data('bs.modal') &&
                  $('#idletimer_warning_dialog').data('bs.modal').isShown) {
                  // http://stackoverflow.com/questions/11519660/twitter-bootstrap-modal-backdrop-doesnt-disappear
                  $('#idletimer_warning_dialog').modal('hide');
                  $('body').removeClass('modal-open');
                  $('div.modal-backdrop').remove();
                  document.title = origTitle;
                  startIdleTimer();
              }

        if (!opts.enableDialog || (opts.enableDialog && isDialogOpen() !== true)) {
          startIdleTimer();
        }
      });
    };

    // Function In use 
    // Function to check if the application exceeded its idle timeout in intervals
    checkIdleTimeout = function () {
    	
      var timeNow = $.now(), timeIdleTimeout = (store.get('idleTimerLastActivity') + (opts.idleTimeLimit * 1000));

      console.log('timeNow: ', new Date(timeNow));
  		console.log('timeIdleTimeout: ', new Date(timeIdleTimeout));
  	
      if (store.get('idleTimerLoggedOut') === true) { // a 'manual' user logout?
          logoutUser();
      }
      
      // Check if it exceed the idle timeout
      if (timeNow > timeIdleTimeout) {

        if (!opts.enableDialog) { // Not in use
          logoutUser();
        }else if (store.get('idleTimerLoggedOut') === true) { // a 'manual' user logout?
            logoutUser();
        } else if (opts.enableDialog && isDialogOpen() !== true) { // Not in use
          openWarningDialog();
          startDialogTimer();
        }
      }  else { // Not in use
        if (isDialogOpen() === true) {
          destroyWarningDialog();
          stopDialogTimer();
        }
      }
    };

    // Function In use
    // Function to start the idle timeout
    startIdleTimer = function () { 
      stopIdleTimer();
      idleTimerLastActivity = $.now();
      store.set('idleTimerLastActivity', idleTimerLastActivity);
      idleTimer = setInterval(checkIdleTimeout, (checkHeartbeat * 1000));
    };

    // Function In use
    // Function to stop the idle timeout
    stopIdleTimer = function () {
      clearInterval(idleTimer);
    };

    // Function In use
    // Function shows the warning dialog box or the modal pop up
    openWarningDialog = function () {
     
    	$('body').append('<div class="modal" id="idletimer_warning_dialog">\
    			   <div class="modal-dialog">\
    			      <div class="modal-content">\
    			         <div class="modal-header">\
    						<h5 class="modal-title">' + opts.dialogTitle + '</h5>\
    			            <button type="button" class="close" data-dismiss="modal"\
    			               aria-hidden="true">&times;</button>\
    			         </div>\
    			         <div class="modal-body">\
    			            <center>\
    			               <p>' + opts.dialogText + '</p>\
    			            </center>\
    			         </div>\
    			      </div>\
    			   </div>\
    			</div>');
    	 $('#idletimer_warning_dialog').modal('show');

    	 // Not in use right now (the code commented below)
    	 
//    	 var dialogContent = "<div id='idletimer_warning_dialog'><p>" + opts.dialogText + "</p><p style='display:inline'>Time remaining: <div style='display:inline' id='countdownDisplay'></div></p></div>";    	 
//      $(dialogContent).dialog({
//        buttons: {
//          "Stay Logged In": function () {
//            destroyWarningDialog();
//            stopDialogTimer();
//            startIdleTimer();
//          },
//          "Log Out Now": function () {
//            logoutUser();
//          }
//        },
//        closeOnEscape: false,
//        modal: true,
//        title: opts.dialogTitle
//      });
//
//      // hide the dialog's upper right corner "x" close button
//      $('.ui-dialog-titlebar-close').css('display', 'none');
//
//      // start the countdown display
//      countdownDisplay();
//
//      document.title = opts.dialogTitle;
      
//      setTimeout(function(){
//  		$('#idletimer_warning_dialog').modal('hide');
//  		$('body').removeClass('modal-open');
//          $('div.modal-backdrop').remove();
//          document.title = origTitle;
//  	},opts.dialogDisplayLimit * 1000);
    };
    
    
 // Keeps the server side connection live, by pingin url set in keepAliveUrl option.
    // KeepAlivePinged is a helper var to ensure the functionality of the keepAliveInterval option
//    var keepAlivePinged = true;

    function keepAlive() {
        if (opts.keepAlivePinged) {
            // Ping keepalive URL using (if provided) data and type from options
            $.ajax({
                type: 'POST',
                url: 'alive.jsp',
                data: '',
                success: function(data) {
                	if(data.status == "403") {
                		logoutUser();
                	}
                },
                error: function (request, status, error) {
                	if(status != 200) {
                    	// Create timeout warning dialog
                		$('body').append('<div class="modal" id="connection-timeout-dialog">\
                				<div class="modal-dialog">\
                					<div class="modal-content">\
                						<div class="modal-header">\
                							<h5 class="modal-title">Connection Error</h5>\
                							<button type="button" class="close" data-dismiss="modal"\
                								aria-hidden="true">&times;</button>\
                						</div>\
                						<div class="modal-body">\
                							<center>\
                								<p>Oops! We lost connection with server.</p>\
                							</center>\
                						</div>\
                					</div>\
                				</div>\
                			</div>');
                        
                        $("#connection-timeout-dialog").modal("show");
                        
                        setTimeout(function() {
                        	logoutUser();
                        }, 2000);
                        
                	}
                }
            });
            
            setTimeout(function() {
            	keepAlive();
            }, checkHeartbeat * 1000);
        }
    }    

    checkDialogTimeout = function () {
      var timeNow = $.now(), timeDialogTimeout = (store.get('idleTimerLastActivity') + (opts.idleTimeLimit * 1000) + (opts.dialogDisplayLimit * 1000));

      if ((timeNow > timeDialogTimeout) || (store.get('idleTimerLoggedOut') === true)) {
        logoutUser();
      }
    };

    startDialogTimer = function () {
      dialogTimer = setInterval(checkDialogTimeout, (checkHeartbeat * 1000));
    };

    stopDialogTimer = function () {
      clearInterval(dialogTimer);
      clearInterval(remainingTimer);
    };

    // Function In use
    // Function to check if the dialog is open or not
    isDialogOpen = function () {
      var dialogOpen = $("#idletimer_warning_dialog").is(":visible");

      if (dialogOpen === true) {
        return true;
      }
      return false;
    };

    destroyWarningDialog = function () {
      $(".ui-dialog-content").dialog('destroy').remove();
      $('#idletimer_warning_dialog').modal('hide');
      $('body').removeClass('modal-open');
      $('div.modal-backdrop').remove();
      document.title = origTitle;
    };

    // display remaining time on warning dialog
    countdownDisplay = function () {
      var dialogDisplaySeconds = opts.dialogDisplayLimit, mins, secs;

      remainingTimer = setInterval(function () {
        mins = Math.floor(dialogDisplaySeconds / 60); // minutes
        if (mins < 10) { mins = '0' + mins; }
        secs = dialogDisplaySeconds - (mins * 60); // seconds
        if (secs < 10) { secs = '0' + secs; }
        $('#countdownDisplay').html(mins + ':' + secs);
        dialogDisplaySeconds -= 1;
      }, 1000);
    };

    // Function In use
    // Function logs out the user and stores it in local storage
    logoutUser = function () {
      store.set('idleTimerLoggedOut', true);
      
      if (opts.customCallback) {
        opts.customCallback();
      }

      if (opts.redirectUrl) {
        window.location.href = opts.redirectUrl;
      }
    };

    //###############################
    // Build & Return the instance of the item as a plugin
    // This is your construct.
    //###############################
    return this.each(function () {

      if (store.enabled) {
        idleTimerLastActivity = $.now();
        store.set('idleTimerLastActivity', idleTimerLastActivity);
        store.set('idleTimerLoggedOut', false);
        console.log('idleTimerLastActivity: ', new Date(idleTimerLastActivity));
      } else {
        alert('Please disable "Private Mode", or upgrade to a modern browser. Or perhaps a dependent file missing. Please see: https://github.com/marcuswestin/store.js');
      }

      activityDetector();

      keepSessionAlive();
      
      keepAlive();

      startIdleTimer();
    });
  };
}(jQuery));
