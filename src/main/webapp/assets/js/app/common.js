/**
 * @description : Used to get Parameter values from URL QueryParam.
**/
function getQueryVariable(variable) {
	var query = window.location.search.substring(1);
	var vars = query.split("&");
	for (var i = 0; i < vars.length; i++) {
		var pair = vars[i].split("=");
		if (pair[0] == variable) {
			return pair[1];
		}
	}
}

$(window).on('load', function(){
	$(".nav li a").filter(function(){
		currentUrl = this.href;
		var thisUrl = currentUrl.split("?")[0];
		if(thisUrl == location.href.replace(/#.*/, "")) {
			return true;
		}
	}).parent().addClass("active");
	
});


/**
 * @description Use when get values from database 
 */

function format(input) {
	input = input + '';
	var nStr = '';
	var decimal = '';
		nStr = input.split('.')[0].replace(/,/g, '');
		decimal = input.split('.').length > 1 ? '.' + input.split('.')[1] : '';
		if(nStr == ''){
			return '';
		}else{
			 var res = Number(nStr).toLocaleString('en-IN');
				 return input = res;
		}
}

/**
 *@description Use when insert new value as an user input 
 */
function editformat(input) {
	var nStr = '';
		nStr = input.value.trim().replace(/,/g, ''); //For input entered Format
	
		if(nStr == ''){
			 input.value = '';
		}else{
			 var res = Number(nStr).toLocaleString('en-IN');
			 input.value = res;
		}
}

/**
 * @description: Used for validation only numeric value can enter in input box
 */
var specialKeys = new Array();
specialKeys.push(8); // Backspace
specialKeys.push(9); // Tab
specialKeys.push(11); // v Tab
specialKeys.push(39); // right
specialKeys.push(37); // left
var specialKeys1 = new Array();
specialKeys1.push(46); // Backspace
var specialKeys2 = new Array();
specialKeys2.push(58); // Backspace

function IsNumeric(e) {
	var keyCode = e.which ? e.which : e.keyCode
			var ret = ((keyCode >= 48 && keyCode <= 57) ||
					specialKeys.indexOf(keyCode) != -1 || specialKeys1
					.indexOf(keyCode) != -1);
	return ret;
}

function IsNumericWithNagative(e) {
	var keyCode = e.which ? e.which : e.keyCode
			if (keyCode == 45) {
				   return true;
			}
			var ret = ((keyCode >= 48 && keyCode <= 57) ||
					specialKeys.indexOf(keyCode) != -1 || specialKeys1
					.indexOf(keyCode) != -1);
	return ret;
}

/**
 * @description: When user insert Number value in input box, function used for Convert number value to comma separated value
 */
function numformatwithdecimal(input) {
	var nStr = input.value.trim();
	nStr = nStr.replace(/\,/g, "");
	var x = nStr.split('.');
	var x1 = x[0];
	var x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	x2 = (x2.slice(0, 3));
	input.value = x1 + x2;
}

function editnumformatwithdecimal(input) {
	input = input + '';
	var nStr = '';
	var decimal = '';
		nStr = input.split('.')[0].replace(/,/g, '');
		decimal = input.split('.').length > 1 ? '.' + input.split('.')[1] : '';
		if(nStr == ''){
			return '';
		}else{
			 var res = Number(nStr).toLocaleString('en-IN');
			 if(decimal == ""){
				 return input = res;
			 }
			 else{
				 decimal = (decimal.slice(0, 3));
				 return input = res + decimal;
			 }
		}
}

$(document).ready(function(){
/**
 * @description: call column_adjust() function and redraw dataTable
 */
	$("#loader").hide();
	if($.fn.dataTable != undefined){
		setTimeout(function(){
				$.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
				column_adjust();
		},200);
	}
	setTimeout(function(){
		$('.bs-searchbox').find('.form-control').addClass('text-ARO');
		$('button[data-id]').addClass('selectdropbtn');
	},200);
})

/**
 * @description: Adjust dataTable columns while open modal
 */
function column_adjust(){
		$.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
		$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
			$.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
	    } );
		
		$('.collapse').on( 'shown.bs.collapse', function (e) {
			$.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
	    } );

		$('.modal').on( 'shown.bs.modal', function (e) {
			$.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
	    } );
		
		$('.modal').on('hidden.bs.modal', function () {
			$.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
		});
}


//Logs out the user from all tabs by setting the value in local storage	
function voluntaryLogoutAll() {
    if (store.enabled) {
      store.set('idleTimerLoggedOut', true);
      window.location.href = 'logout.jsp';      // redirect to this url. Set this value to YOUR site's logout page.
    } else {
      alert('Please disable "Private Mode", or upgrade to a modern browser. Or perhaps a dependent file missing. Please see: https://github.com/marcuswestin/store.js')
    }
  }

/**
 *@description:  This function for validate input date is valid or not
 * @param inp [for get input date]
 * @returns return true when date is valid or else return false
 */
function validateDate(inp){
	var dateLen = 0;
	var regex = /^(?:(?:31(\/|-|\.)(?:0?[13578]|1[02]|(?:Jan|Mar|May|Jul|Aug|Oct|Dec)))\1|(?:(?:29|30)(\/|-|\.)(?:0?[1,3-9]|1[0-2]|(?:Jan|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec))\2))(?:(?:1[6-9]|[2-9]\d)?\d{2})$|^(?:29(\/|-|\.)(?:0?2|(?:Feb))\3(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\d|2[0-8])(\/|-|\.)(?:(?:0?[1-9]|(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep))|(?:1[0-2]|(?:Oct|Nov|Dec)))\4(?:(?:1[6-9]|[2-9]\d)?\d{2})$/;
	if(inp != undefined && inp != ""){
		if(inp.match(regex)){
			var spltDate = inp.split('/');
			dateLen = spltDate.length;
			// Extract the string into month, date and year
			  if (dateLen>1)
				  var pdate = inp.split('/');
			  if(undefined != pdate){
				  var dd = parseInt(pdate[0]);
				  var mm  = parseInt(pdate[1]);
				  var yy = parseInt(pdate[2]);
				  // Create list of days of a month [assume there is no leap year by default]
				  var ListofDays = [31,28,31,30,31,30,31,31,30,31,30,31];
				  if (mm==1 || mm>2)
				  {
					  if (dd>ListofDays[mm-1])
					  {
						  return false;
					  }
				  }
				  if (mm==2)
				  {
					  var lyear = false;
					  if ( (!(yy % 4) && yy % 100) || !(yy % 400)) 
						  lyear = true;
					  
					  if ((lyear==false) && (dd>=29))
					  {
						  return false;
					  }
					  if ((lyear==true) && (dd>29))
					  {
						  return false;
					  }
				  }
			  }else{
				  return false;
			  }
			  
		  }else{
			  return false;
			  
		  }
		  return true;
	}
}