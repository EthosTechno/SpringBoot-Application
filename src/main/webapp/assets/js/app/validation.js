/**
 * @author Vivek
 * @since 2022-05-04
 */

/** Change log
 * ---------------------------------------------------------------------------
 * Date     	Author    	Comment
 * ---------------------------------------------------------------------------
 * 11-05-22		Vivek		- Add processStatus parameter in save&delete function.
 * 11-05-22		Vivek		- Add confirmation box appear when toggle is ON, click on Next and no address is validated.
 * 11-05-22		Vivek		- Set smId and isDispValidated flag in sessionStorage and get that value from sessionStorage.
 * 12-05-22		Vivek		- Create updateProgressBar function for update progress bar based on toggle is on and off.
 * 17-05-22		Vivek		- Disable shortcut key on manual search and prevent to show multi bootpopup modal.
 * 14-06-22		Vivek		- store sampletype in sessionStorage and update that value on click on validation type from navigation and replace localStorage to sessionStorage
 * 23-06-22		Vivek		- Add sampleType parameter in getSampleMaster method for show result table based on latest runId and sampleType.
 */

 $.fn.selectpicker.Constructor.BootstrapVersion = '4';
 var isDispValidated = "";
 var sampleType;
 $(document).ready(function(){
 	$('#loader').hide(); 
 	setTimeout(function(){
 		$('button[data-id]').addClass('selectdropbtn');
 		$('.bs-searchbox').find('.form-control').addClass('text-theme');
 	},200);
 	var smId = 0;
 	// check sampleType in sessionStorage if null, set as a Address validation. 
 	if (sessionStorage.getItem("sampletype") == null) {
 		if (getQueryVariable("sampleType")== 1) {
 			sampleType = sessionStorage.setItem("sampletype", 1);
		}else {
			sampleType = sessionStorage.setItem("sampletype", 2);
		}
	 }
 	if (sessionStorage.getItem("isDispValidated") == 1) {
 		isDispValidated = sessionStorage.getItem("isDispValidated");
 		var smId = sessionStorage.getItem("smId");
 		getSearchString("onReload", isDispValidated, smId);
	}else{
		var bottonType = "current";
	 	isDispValidated = 0;
		getSearchString(bottonType, isDispValidated, 0);
	}
 	// check sampleType from sessionStorage for selection class on active validation type.
 	if (sessionStorage.getItem("sampletype") == "1") {
		$('.dropdown-menu').find('.validation-opt').removeClass('acitve');
		$('#addressType').addClass('active');
		$('.buttons-colvis').show();
	}
 	if (sessionStorage.getItem("sampletype") == "2") {
		$('.dropdown-menu').find('.validation-opt').removeClass('acitve');
		$('#nameType').addClass('active');
		$('.buttons-colvis').hide();
	}
	
	//Using below code will clean Query Parameters from Application URL   
    var uri = window.location.toString();
    if (uri.indexOf("?") > 0) {
        var clean_uri = uri.substring(0, uri.indexOf("?"));
        window.history.replaceState({}, document.title, clean_uri);
    }
    //End
    
 	getTotalCount();
 	updateProgressBar(smId, isDispValidated);
 });
 
 /**
  * Store sampleType value on sessionStorage when click on Address Validation type.
  */
 function addressValidationType(e) {
	 sessionStorage.clear();
		$('.dropdown-menu').find('.validation-opt').removeClass('acitve');
	    $(e).addClass('active');
	    $('.buttons-colvis').show();
	    sessionStorage.setItem("sampletype", 1);
		location.reload(true);
	}

 /**
  * Store sampleType value on sessionStorage when click on Name Validation type.
  */
	function nameValidationType(e) {
		sessionStorage.clear();
		$('.dropdown-menu').find('.validation-opt').removeClass('acitve');
	    $(e).addClass('active');
	    $('.buttons-colvis').hide();
	    sessionStorage.setItem("sampletype", 2);
		location.reload(true);
		
	}
 
 /**
  * Get total number of search string from database.
  */
 var totalSearchStr = 0;
 function getTotalCount() {
	sampleType = sessionStorage.getItem("sampletype");
	 var param = {
				"sampleType":sampleType
			}
	 $.ajax({
	     	url: 'getTotalCount',
	     	type: 'GET',
	     	data : param,
	        dataType: "json",
	        async : false,
	        success: function(data) {
	        	totalSearchStr = data;
	        }
	 });
}
 
 /**
  * Short key event for next/previous and toggle button.
  * @param e
  * @returns
  */
 document.onkeydown = checkKey;
 function checkKey(e) {
	 var status = $('#editaddress').attr('data-search-status');
     if(status == "string"){
    	 e = e || window.event;
         if (e.keyCode == '37') {
        	 nextAndPrevButton('prev');
         }
         else if (e.keyCode == '39') {
        	 nextAndPrevButton('next');
         }
     }
 }
 
 /**
  *click on next and prev. button, checked validated button in master data table and make array of checked validated smId. 
  * @param bottonType
  * @returns
  */
 function nextAndPrevButton(bottonType) {
	 	var selectedData = [];
		var ValidationArry = [];
		var processStatus = $('#addressarea').attr('data-process-status');
		var isDispValidated = (($("#isDisplayValid").prop("checked") == true)? 1 :0);
		var smId = 0;
		if (bottonType != 'onReload') {
			if (sessionStorage.getItem("smId") != null) {
				smId = sessionStorage.getItem("smId");
			}else{
				smId = $('#addressarea').attr('data-search');
			}
		}else {
			smId = $('#addressarea').attr('data-search');
		}
		// if get last/first record on search string, that time check button type if next, disable to previous button, else disable to next.
		if (bottonType == 'next') {
			 $('#PrevPan').prop("disabled", false);
		}else if (bottonType == 'prev') {
			$('#NextPan').prop("disabled", false);
		}
		// checked validated button in master data table and make array of
		// checked validated smId.
		var masterTable = $('#validation').DataTable();
		var node = masterTable.rows().nodes();
		masterTable.rows().data().each(function(value, index) {
			var valid = masterTable.cell(node[index], 2).nodes().to$().find('button').attr('data-ischecked');
			if (valid == "true") {
				var mdId = masterTable.cell(node[index], 2).nodes().to$().find('button').attr('data-mdId');
				selectedData.push({
					"mdId" : mdId,
					"smId" : smId,
				});
				ValidationArry.push({ "mdId" : parseInt(mdId) });
			}
		});
		// check bottonType is 'onReload', smId is get from sessionStorage else get to attribute.
		// for go to current search string for validation.
		var smIdReload = 0;
		if (bottonType != 'onReload') {
			smIdReload = smId;
		} else {
			smIdReload = sessionStorage.getItem("smId");
		}
		
		// Pop-up confirmation box when processStatus is not equals to 2 and
		// not any validated question.
		if (selectedData.length == 0 && processStatus != "2") {
			var data = $(".bootpopup").hasClass('show');
			if (data != true){
				bootpopup.confirm("Are you sure there is no matching string available? ", function(ans) {
					if (ans == true) {
						saveAndDelete(selectedData);
						 if (smIdReload != undefined && smIdReload != 0 && smIdReload != "") {
							 getSearchString(bottonType, isDispValidated, smIdReload);
						}
						// update progress bar on click on next/prev. button if
						// toggle is on
							if (isDispValidated == 1) {
								var newSmId = $('#addressarea').attr('data-search');
								updateProgressBar(newSmId, isDispValidated);
							}else {
								var newSmId = 0;
								updateProgressBar(newSmId, isDispValidated);
							}
						if (bottonType == 'onReload') {
							 $('#editaddress').trigger('click');
						}
				    }
				});
			}
		} else if (processStatus == 2) {
			// check processStatus is 2, compare the older validation and new
			// validation if not match, save on db. else not save and show new data.
			 if (JSON.stringify(SelectedValidationArry) != JSON.stringify(ValidationArry)) {
				 saveAndDelete(selectedData);
			 }
			 if (smIdReload != undefined && smIdReload != 0 && smIdReload != "") {
				 getSearchString(bottonType, isDispValidated, smIdReload);
			}
		}else{
			// check processStatus is 0 or 1, save the record in db and show new data.
			saveAndDelete(selectedData);
			 if (smIdReload != undefined && smIdReload != 0 && smIdReload != "") {
				 getSearchString(bottonType, isDispValidated, smIdReload);
			}
		}
		// Redirect to current address/string when click on the save or cancel button.
		if ((selectedData.length != 0 || processStatus == 2 ) && bottonType == 'onReload') {
			 $('#editaddress').trigger('click');
		}
		// update progress bar on click on next/prev. button if toggle is on
		if (isDispValidated == 1) {
			var newSmId = $('#addressarea').attr('data-search');
			updateProgressBar(newSmId, isDispValidated);
		}else {
			var newSmId = 0;
			updateProgressBar(newSmId, isDispValidated);
		}
}
 
 /**
  * get progress bar details from database based on toggle is on and off.
  * @param smId
  * @param isDispValidated
  * @returns
  */
 function updateProgressBar(smId, isDispValidated) {
	 sampleType = sessionStorage.getItem("sampletype");
	 var param = {
				"isDispValidated": isDispValidated,
				"smId": smId,
				"sampleType":sampleType
			}
	 $.ajax({
	     	url: 'getProgessBarSummary',
	     	type: 'GET',
	     	data : param,
	        dataType: "json",
	        async : false,
	        success: function(data) {
	        	if (isDispValidated == "1" || isDispValidated == 1) {
	        		$('#progressBar').text(data.indexOfStr +'/'+ totalSearchStr);
				}else {
					$('#progressBar').text(data.validatedStr +'/'+ totalSearchStr);
				}
	        }
	 });
	}

 /**
  * Active and Inactive validation button onclick on validation button.
  * @param e
  * @returns
  */
 function validaddress(e) {
 	$(e).addClass("valid-btn-active");
 	$(e).find('.far').toggleClass("fas");
 	
 	var isValid = $(e).attr("data-isChecked");
 	if (isValid == "true") {
 		$(e).attr("data-isChecked", "false");
	}
 	if (isValid == "false") {
 		$(e).attr("data-isChecked", "true");
	}
 }
 
 /**
  * Enable to edit function on Search String field on click of search button.
  * 	Disable to next/previous button and enable to search button when Search String field is editable.
  * @param e
  * @returns
  */
 function editSearchString(e) {
	 var status = $(e).attr('data-search-status');
     if(status == "string"){
      $(e).find('i').toggleClass('fa-times-circle');
      $('#addressarea').removeAttr('disabled');
      $('#addressarea').val('');
      $(e).attr('data-search-status', 'search');
      $('#validationToggle').addClass('d-none');
      $('#arrowbutton').addClass('d-none');
      $('#searchbutton').removeClass('d-none');
      $('#validation').dataTable().fnClearTable();
     }
     else{
      $(e).find('i').toggleClass('fa-times-circle');
      $('#addressarea').attr('disabled', true);
      $(e).attr('data-search-status', 'string');
      $('#isDisplayValid').attr('disabled', true);
      $('#validationToggle').removeClass('d-none');
      $('#arrowbutton').removeClass('d-none');
      $('#searchbutton').addClass('d-none');
      $('#savesearch').addClass('d-none');
      
   // Back to current address/string when click on the save or cancel button.
      if (sessionStorage.getItem("isDispValidated") == 1) {
   			isDispValidated = sessionStorage.getItem("isDispValidated");
   			var smId = sessionStorage.getItem("smId");
   			getSearchString("onReload", isDispValidated, smId);
   			updateProgressBar(smId, isDispValidated);
  		}else{
  			var bottonType = "current";
  			isDispValidated = 0;
  			getSearchString(bottonType, isDispValidated, 0);
  			updateProgressBar(0, isDispValidated);
  		}
     }
 }
 
 /**
  * disable to Search String field and back to current address/string
  * @returns
  */
 function cancelButton() {
    $('#editaddress').trigger('click');
 }
 
 /**
  * Get the master data and populate data table based on search string value from database.
  * @returns
  */
 function searchAdressButton() {
	var searchStr =  $('#addressarea').val().trim();
	sampleType = sessionStorage.getItem("sampletype");
	if (searchStr == "") {
		$("#DetailErrorModal").show();
		document.getElementById("modal_err_msg").innerHTML = MESSAGE.VALIDATION.ERR_INVALID;
	    document.getElementById('errorModel').className = 'alert alert-danger';
	    $('#DetailErrorModal').modal({
	    	backdrop: false,
	    	keyboard: false,
	    	show: true
        })
        setTimeout(function() {
        	$('#DetailErrorModal').modal('hide');
        }, 5000);
	}else {
		 $('#savesearch').removeClass('d-none');
         $('#searchbutton').addClass('d-none');
         $('#arrowbutton').addClass('d-none');
         
		 var param = {
				"searchStr": searchStr,
				"sampleType": sampleType
		 }
		 $.ajax({
		     	url: 'getSmDetailsBySearchStr',
		     	type: 'GET',
		     	data : param,
		        dataType: "json",
		        async : false,
		        success: function(data) {
		        	$('#validation').dataTable().fnClearTable();
		        	if (data.length > 0) {
		        		$('#addressarea').attr('data-search',data[0].smId).val(searchStr);
		        		$('#addressarea').attr('data-process-status',data[0].processStatus);
		        		
		        		getSampleMaster(data[0].smId, data[0].processStatus, "searchButton");
					}else {
						 $('#searchbutton').removeClass('d-none');
				         $('#savesearch').addClass('d-none');
				         $('#arrowbutton').addClass('d-none');
				         
						$("#DetailErrorModal").show();
						document.getElementById("modal_err_msg").innerHTML = MESSAGE.VALIDATION.NO_DATA_FOUND;
					    document.getElementById('errorModel').className = 'alert alert-danger';
					    $('#DetailErrorModal').modal({
					    	backdrop: false,
					    	keyboard: false,
					    	show: true
				        })
				        setTimeout(function() {
				        	$('#DetailErrorModal').modal('hide');
				        }, 5000);
					}
		        }
		 });
	}
	
}
 
/**
 * Empty data table when data is null or not found.
 * @returns
 */
function emptyValidationTable() {
var table = $('#validation').DataTable({
			"sorting":false,
			'destroy' : true,
			"language" : {
				"emptyTable" : "No data available."
			},
			columnDefs: [
		        {targets: 1, visible: false,className: 'noVis'},
		       	{targets: [0,2], "orderable": false, width:85},
		        {targets: 4, width : 115},
		        {targets: 5, visible: false },
		        {targets: 6, visible: false },
		        {targets: 7, visible: false },
		        {targets: 8, visible: false},
		        {targets: 9, visible: false},
		    ],
		    buttons: [{
             extend: 'colvis',
             postfixButtons: [ 'colvisRestore' ],
             columns: ':not(.noVis)'
         }]
		});
		$('#validation_wrapper .col-md-6:eq(0)').addClass('d-flex');
	 	var checkToggle = "";
	 	if (isDispValidated == 1) {
			checkToggle = '<div class="ml-3 mt-1">' + 
			'<div class="custom-control custom-switch" id="validationToggle">'+
			'<input type="checkbox" class="custom-control-input" checked onclick="displayValidatedButton()" id="isDisplayValid">'+
			'<label class="custom-control-label" for="isDisplayValid">Display Validated Result</label>'+
			'</div>'+ 
	'</div>';
		} else {
			checkToggle = '<div class="ml-3 mt-1">' + 
			'<div class="custom-control custom-switch" id="validationToggle">'+
			'<input type="checkbox" class="custom-control-input" onclick="displayValidatedButton()" id="isDisplayValid">'+
			'<label class="custom-control-label" for="isDisplayValid">Display Validated Result</label>'+
			'</div>'+ 
	'</div>';
		}
	 	$(checkToggle).appendTo("#validation_wrapper .row .col-md-6:eq(0)");
	 	$('#validation_wrapper .col-md-6:eq(1)').addClass('d-flex flex-row-reverse');
	 	table.buttons().container().appendTo('#validation_wrapper .col-md-6:eq(1)').addClass('mr-2').find('button').addClass('btn btn-info btn-default-theme form-control');
	 	
}
 
 /**
  * Populate distinct search string from db to UI and call master result data.
  * @returns
  */
 function getSearchString(bottonType, isDispValidated, smId) {
	 var processStatus = -1;
	 sampleType = sessionStorage.getItem("sampletype");
	 var param = {
		"bottonType" : bottonType,
		"isDispValidated": isDispValidated,
		"smId": smId,
		"sampleType":sampleType
	}
 	$.ajax({
     	url: 'getDistinctSearchString',
     	type: 'GET',
     	data : param,
        dataType: "json",
        async : false,
        success: function(data) {
        	if (data.length > 0) {
        		$('#addressarea').val('');
        		$('#PrevPan').prop("disabled", false);
        		$('#NextPan').prop("disabled", false);
        		
        		$('#addressarea').attr('data-search',data[0].smId).val(data[0].searchString);
        		$('#addressarea').attr('data-process-status',data[0].processStatus);
	        	processStatus = data[0].processStatus;
	        	
	        	var smId = $('#addressarea').attr('data-search');
	       	 	if (smId > 0 && processStatus != -1) {
	       	 		getSampleMaster(smId, processStatus, bottonType);
	       	 	}
	       	 	if (isDispValidated == 1 && bottonType != 'onReload') {
	        		sessionStorage.setItem("smId", data[0].smId);
				}
			}else {
				emptyValidationTable();
				$('#DetailErrorModal').modal('show');
				if (bottonType == "next") {
					$('#NextPan').prop("disabled", true);
					document.getElementById("modal_err_msg").innerHTML = MESSAGE.VALIDATION.LAST_DATA;
    			    document.getElementById('errorModel').className = 'alert alert-danger';
    			    $('#DetailErrorModal').modal({
    			    	backdrop: false,
    			    	keyboard: false,
    			    	show: true
    		        })
    		        setTimeout(function() {
    		        	$('#DetailErrorModal').modal('hide');
    		        }, 5000);
				}else if(bottonType == "prev") {
					$('#PrevPan').prop("disabled", true);
					document.getElementById("modal_err_msg").innerHTML = MESSAGE.VALIDATION.FIRST_DATA;
    			    document.getElementById('errorModel').className = 'alert alert-danger';
    			    $('#DetailErrorModal').modal({
    			    	backdrop: false,
    			    	keyboard: false,
    			    	show: true
    		        })
    		        setTimeout(function() {
    		        	$('#DetailErrorModal').modal('hide');
    		        }, 5000);
				}else{
					document.getElementById("modal_err_msg").innerHTML = MESSAGE.VALIDATION.NO_DATA;
    			    document.getElementById('errorModel').className = 'alert alert-danger';
    			    $('#DetailErrorModal').modal({
    			    	backdrop: false,
    			    	keyboard: false,
    			    	show: true
    		        })
    		        setTimeout(function() {
    		        	$('#DetailErrorModal').modal('hide');
    		        }, 5000);
				}
			}
        	var searchstringtext = $('#addressarea').val();
        	if(searchstringtext == ""){
        		$('#PrevPan').prop("disabled", true);
        		$('#NextPan').prop("disabled", true);
        	}
         }
     })
 }
 
 /**
  * Populate Master result data table based on search string and display validated string on UI.
  * @param smId
  * @param processStatus
  * @returns
  */
 var SelectedValidationArry = [];
 function getSampleMaster(smId, processStatus, bottonType) {
	 var isDispValidated = 0;
	 if (sessionStorage.getItem("isDispValidated") == 1) {
		 isDispValidated = sessionStorage.getItem("isDispValidated");
	}else {
		isDispValidated = 0
	}
	
	 var param = {
		"isDispValidated" : isDispValidated,
		"smId" : smId,
		"sampleType":sampleType,
		"processStatus": processStatus
	}
	$.ajax({
  	url: 'getSampleMaster',
  	type: 'GET',
  	data : param,
     dataType: "json",
     async : false,
     success: function(data) {
    	 if (data.length > 0) {
    		 if (isDispValidated == 1) {
    			 getSelectedValidation(data);
			}
    		 var getHeight = $( window ).height();
             var setHeight = getHeight - 350;
             var table = $('#validation').DataTable({
    			 	"responsive": true,
    			 	"ordering": false,
    			 	"paging": true,
    			 	"data" : data,
    			 	"info": true,
    			 	"scrollY": setHeight,
                    "scrollCollapse": true,
    			 	"sorting":false,
    			 	"autoWidth": false,
    			 	"lengthChange": true,
					'destroy' : true,
					"language" : {
						"emptyTable" : "No data available."
					},
					"aoColumns" : [ {
						data : "srNo",
						targets: "0",
						className : 'text-right',
						width:85,
						
					}, {
						data : "mdId",
						className : 'noVis',
					}, {
						data : "isValidated",
						className :"text-center",
						"mRender": function (data, type, full) {
							var buttons="";
							if (full.isValidated == 1) {
								buttons+='<button type="button" class="btn btn-default btn-sm valid-btn valid-btn-active p-0" id="validButton" data-isChecked = '+true+' data-mdId = '+full.mdId+' onclick="validaddress(this);"><i class="far fa-check-circle fas"></i></button>'
							} else {
								buttons+='<button type="button" class="btn btn-default btn-sm valid-btn p-0" id="validButton" data-isChecked = '+false+' data-mdId = '+full.mdId+' onclick="validaddress(this);"><i class="far fa-check-circle"></i></button>'
							}							
							return buttons;
						}
					}, {
						data : "searchString",
						className : 'text-left',
					}, {
						data : "score",
						className : 'text-right',
						"mRender": function (data, type, full) {
		                    return Number(data).toFixed(2);
		                    }
					}, {
						data : "area",
						className : 'text-left',
					}, {
						data : "district",
						className : 'text-left',
					}, {
						data : "taluka",
						className : 'text-left',
					} ,{
						data : "state",
						className : 'text-left',
					}, {
						data : "pinCode",
						className : 'text-left',
					}],
					"fnRowCallback" : function(nRow, aData, iDisplayIndex){      
                      var oSettings = $("#validation").dataTable().fnSettings();
                       $("td:first", nRow).html(oSettings._iDisplayStart+iDisplayIndex +1);
                       return nRow;
					},
					columnDefs: [
				        {targets: 1, visible: false,className: 'noVis'},
				       	{targets: [0,2], "orderable": false, width:85},
				        {targets: 4, width : 115},
	                    {targets: 5, visible: false },
	                    {targets: 6, visible: false },
	                    {targets: 7, visible: false },
	                    {targets: 8, visible: false},
	                    {targets: 9, visible: false},
				    ],
				    buttons: [{
	                    extend: 'colvis',
	                    postfixButtons: [ 'colvisRestore' ],
	                    columns: ':not(.noVis)'
	                }]
				});
    		$('#validation').on( 'column-visibility.dt', function ( e, settings, column, state ) {
    			$($.fn.dataTable.tables(true)).DataTable().columns.adjust();
            });
    		if (bottonType != 'searchButton') {
    			$('#validation_wrapper .col-md-6:eq(0)').addClass('d-flex');
        	 	var checkToggle = "";
        	 	if (isDispValidated == 1) {
    				checkToggle = '<div class="ml-3 mt-1">' + 
    	 			'<div class="custom-control custom-switch" id="validationToggle">'+
    	 			'<input type="checkbox" class="custom-control-input" checked onclick="displayValidatedButton()" id="isDisplayValid">'+
    	 			'<label class="custom-control-label" for="isDisplayValid">Display Validated Result</label>'+
    	 			'</div>'+ 
    	 	'</div>';
    			} else {
    				checkToggle = '<div class="ml-3 mt-1">' + 
    	 			'<div class="custom-control custom-switch" id="validationToggle">'+
    	 			'<input type="checkbox" class="custom-control-input" onclick="displayValidatedButton()" id="isDisplayValid">'+
    	 			'<label class="custom-control-label" for="isDisplayValid">Display Validated Result</label>'+
    	 			'</div>'+ 
    	 	'</div>';
    			}
        	 	$(checkToggle).appendTo("#validation_wrapper .row .col-md-6:eq(0)");
			}
    		$('#validation_wrapper .col-md-6:eq(1)').addClass('d-flex flex-row-reverse');
    	 	table.buttons().container().appendTo('#validation_wrapper .col-md-6:eq(1)').addClass('mr-2').find('button').addClass('btn btn-info btn-default-theme form-control');
		}
    	 
    	 setTimeout(function(){
 			$($.fn.dataTable.tables(true)).DataTable().columns.adjust();
 		},100);
    	 if (sessionStorage.getItem("sampletype") == "2") {
 			$('.buttons-colvis').hide();
 		}else {
 			$('.buttons-colvis').show();
 		}
      }
  })
}
 /**
  * Display validated String on UI based on display validated troggle is on/off on UI
  * @param e
  * @returns
  */
 function displayValidatedButton() {
	sampleType = sessionStorage.getItem("sampletype");
	sessionStorage.clear();
	isDispValidated = (($("#isDisplayValid").prop("checked") == true)? 1 :0);
	sessionStorage.setItem("isDispValidated", isDispValidated);
	sessionStorage.setItem("sampletype", sampleType);
	var bottonType = "next";
	getSearchString(bottonType, isDispValidated, 0);
	
	if (sampleType == 2) {
		 $('.buttons-colvis').hide();
	}
	
	//update progress count click on display validated toggle.
	var smId = $('#addressarea').attr('data-search');
	if (isDispValidated == 1) {
		updateProgressBar(smId, isDispValidated);
	}else{
		updateProgressBar(smId, isDispValidated);
	}
}

 /**
  * when master table data load, older selected validation push in array.
  * @param data
  * @returns
  */
function getSelectedValidation(data) {
	SelectedValidationArry =[];
	for(var i = 0; i < data.length; i++) {
		if (data[i].isValidated == 1) {
			var mdId = data[i].mdId;
			SelectedValidationArry.push({ mdId });
		}
	}
}

/**
 * Save and delete record from database when click on next and previous button.
 * @returns
 */
function saveAndDelete(selectedData) {
	 var smId = $('#addressarea').attr('data-search');
	 var processStatus = $('#addressarea').attr('data-process-status');
	 var requestData = {
		"smId" :smId,
		"processStatus":processStatus,
		"selectedList" : selectedData,
	}
	$.ajax({
 	url: 'saveAndDelete',
 	type: 'POST',
 	data : JSON.stringify(requestData),
    dataType: "json",
    contentType:"application/json",
    async : false,
    success: function(data) {
    }
 })
}
