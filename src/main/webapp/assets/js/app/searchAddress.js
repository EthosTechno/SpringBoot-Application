/**
 * @author Vivek
 * @since 2022-02-15
 */

/** Change log
 * ---------------------------------------------------------------------------
 * Date     	Author    	Comment
 * ---------------------------------------------------------------------------
 * 15-02-22		Vivek		- Create NextPan, PrevPan, getPanNo and getRun function
 * 18-02-22		Vivek		- Create On change of pan number and runId, save the pan details from redis to database, 
 * 								clear that pan from redis and insert new pan from db to redis.
 * 21-02-22		Vivek		- Create getSearchAddress function for populate search address based on selected pan number from UI.
 * 24-02-22		Vivek		- Create getMasterAddress for populate master address based on selected pan number and search address from UI.
 * 25-02-22		Vivek		- Create clearRedis function for clear redis data based on pan number 
 * 09-03-22		Vivek		- Created validAddress function for validate search addresses with master data and update on Redis data with validated flag
 * 11-03-22		Vivek		- Created insertUpdatedPan function for update and save the pan details into database
 * 22-03-22		Vivek		- Click on Run button,when refresh RunId function and re-build the runId drop-down.
 * 28-03-22		Vivek		- create RemarkModal function for populate remark model on run button.
 * 29-03-22		Vivek		- Implement isPanExist flag in getSearchAddress function for popup validation massage for locked pan number and 
 * 								when isPanExist is true, show pan summary details only read mode and disable validation button on UI.
 * 31-03-22		Vivek		- Create runtype method and OnChnage runType method for populate runType drop down
 * 01-04-22		Vivek		- Add Runtype as a input parameter for get cersai and master data.	
 * 11-04-22		Vivek		- Change string to int of runType
 * 13-04-22		Vivek		- Set defult first selected address in search address table and remove hlAddress field in masterDatatable and change postion of valid button.
 * 14-04-22		Vivek		- Set same hight for all datatable based on device resolution.
 * 18-04-22		Vivek		- Get dynamic coloumn for master/cersai table based on selected run type.
 * 20-04-22		Vivek		- Add ajax call for cersai with master api in runAPI method and handle with based on runType. 
 * 29-04-22		Vivek		- Set loader hide and show in getSearchAddress function 
 */

$.fn.selectpicker.Constructor.BootstrapVersion = '4';

$(document).ready(function(){
 	$('#loader').hide(); 
 	setTimeout(function(){
 		$('button[data-id]').addClass('selectdropbtn');
 		$('.bs-searchbox').find('.form-control').addClass('text-theme');
 	},200);
 	getPanNo();
 	getRunType();
 });

//same height for all data-table based on device resolution
var getHeight = $(window).height();

/**
 * Click on next button, change the next pan and save the pan details from redis to database, 
 * clear that pan from redis and insert new pan from db to redis.
 * @returns
 */
 function NextPan() {
	$('#masterAddressTable').dataTable().fnClearTable();
 	var getLength = $('#PanNumber option').length;
 	var getVal = $('#PanNumber').selectpicker('val');
 	var getPan = $("#PanNumber option:selected").text();
 	
 	// insert pan details into database
 	insertUpdatedPan(getPan);
 	// clear pan details from redis server based on pan number 
 	clearRedis(getPan);
 	
 	var getPanIndex = $("#PanNumber option:selected").index();
 	var setPanIndex = getPanIndex+1;
 	var nextElement = $('#PanNumber > option:selected').next('option');
 	
	if (nextElement.length > 0) {
//	$('#PanNumber > option:selected').removeAttr('selected').next('option').attr('selected', 'selected');
	 $('#PanNumber').selectpicker('val', setPanIndex);
	 $("#PrevPan").removeClass('disabled');
	}
	$('.selectpicker').selectpicker('refresh');
	$('button[data-id]').addClass('selectdropbtn');
 		$('.bs-searchbox').find('.form-control').addClass('text-theme');
 	
 	var nextPan = $("#PanNumber option:selected").text();	
 	var currentElement = $('#PanNumber > option:selected').next('option');
 	if (currentElement.length == 0) {
		$("#NextPan").addClass('disabled');
	}
 	var runId = $("#runId option:selected").val();
 	var runType = $("#runType option:selected").val();
 	if (!nextPan == "" && !runId == "" && !runType == '') {
		getSearchAddress(nextPan, runId,runType);
	}
 	var getStatus = "" ;
 }

 /**
  * Click on prev button, change the prev pan and save the pan details from redis to database, 
 * clear that pan from redis and insert new pan from db to redis.
  * @returns
  */
 function PrevPan() {
	$('#masterAddressTable').dataTable().fnClearTable();
 	var getLength = $('#PanNumber option').length;
 	var getVal = $('#PanNumber').selectpicker('val');
 	var getPan = $("#PanNumber option:selected").text();
 	insertUpdatedPan(getPan);
 	clearRedis(getPan);
 	var getPanIndex = $("#PanNumber option:selected").index();
 	var setPanIndex = getPanIndex-1;
// 	 alert(setPanIndex);
 	var nextElement = $('#PanNumber > option:selected').prev('option');
	if (nextElement.length > 0) {
		$('#PanNumber').selectpicker('val', setPanIndex);
		$("#NextPan").removeClass('disabled');
//	$('#PanNumber > option:selected').removeAttr('selected').prev('option').attr('selected', 'selected');
	}
	
	$('.selectpicker').selectpicker('refresh');
	$('button[data-id]').addClass('selectdropbtn');
 		$('.bs-searchbox').find('.form-control').addClass('text-theme');
 	
 	var prevPan = $("#PanNumber option:selected").text();	
 	var currentElement = $('#PanNumber > option:selected').prev('option');
 	if (currentElement.length == 0) {
		$("#PrevPan").addClass('disabled');
	}
 	var runId = $("#runId option:selected").val();
 	var runType = $("#runType option:selected").val();
 	if (!prevPan == "" && !runId == "" && !runType == '') {
		getSearchAddress(prevPan, runId,runType);
	}
 }
 
 /**
  * Populate remark model click on run button
  * @returns
  */
 function RemarkModal() {
	 $('#runName').val("");
	 $('#remarkModal').modal({
			backdrop: "static",
	    	keyboard: false,
	    	show: true});
}
 
 /**
  * Reset remark model
  * @returns
  */
 function resetSectionModel() {
 	$("#runName").val('');
 	$("#error").removeClass('alert-success').hide();
 	$("#error").removeClass('alert-danger').hide();
 }
 

/**
 * Click on Run button, call address Search method in controller.
 * @returns
 */
function RunAPI() {
	var run_name = $('#runName').val().trim();
	var run_type = $('#runType').val();
	if (run_name == "" || run_name == null) {
		$('#runName').focus();
		$("#error").addClass('alert-danger').html("Please fill the Run Name").show();
		setTimeout(function() {
			$("#error").hide();
		}, 3000);
	} else {
		$("#error").addClass('alert-success').html("Run is started kindly refresh the page after some time.").show();
		setTimeout(function() {
			$("#error").hide();
			$("#remarkModal").modal('hide');
		}, 4000);
		var param = {
			runName : run_name,
			runType : parseInt(run_type),
		};
		if (run_type == 4) {
			$.ajax({
				url : 'cersaiAddressSearch',
				method : 'GET',
				data : param,
				contentType : 'application/json',
				success : function(data) {
				}
			});
		}else if (run_type == 5 || run_type == 6) {
			$.ajax({
				url : 'getSearchString',
				method : 'GET',
				data : param,
				contentType : 'application/json',
				success : function(data) {
				}
			});
		
		}else {
			$.ajax({
				url : 'addressSearch',
				method : 'GET',
				data : param,
				contentType : 'application/json',
				success : function(data) {
					if (data == 2) {
		        		getRunId(run_type);
					}
				}
			});
		}
	}
};

/**
 * Populate distinct pan number from db to UI
 * @returns
 */
function getPanNo() {
	$.ajax({
    	url: 'getPanNumber',
    	type: 'GET',
        dataType: "json",
        async : false,
        success: function(data) {
        	var options = data;
        	$.each(options, function(val, text) {
        	    $('#PanNumber').append($('<option></option>').val(val).html(text));
        	});
        	$('#PanNumber').selectpicker('refresh');
        }
    })
}

/**
 * On change of pan number, save the pan details from redis to database, clear that pan from redis and insert new pan from db to redis
 */
var lastSelected = "";
$("#PanNumber").on('change', function() {
	$('#searchAddressTable').dataTable().fnClearTable();
	$('#masterAddressTable').empty();
	$('#leftbar').addClass('d-none');
	var getLastpanNo = $("#PanNumber option").eq(lastSelected).text();
	var panIndex = $("#PanNumber option:selected").index();
	var panNo = $("#PanNumber option:selected").text();
	var runId = $("#runId option:selected").val();
	var runType = $("#runType option:selected").val();
	insertUpdatedPan(getLastpanNo)
	clearRedis(getLastpanNo);
	$("#NextPan").removeClass('disabled');
	$("#PrevPan").removeClass('disabled');
 	
 	if (!panNo == "" && !runId == "" && !runType == '') {
		getSearchAddress(panNo, runId,runType);
	}
	lastSelected = panIndex;
	
});

/**
 * Populate Run Id drop down from run_result table in database 
 * @returns
 */
function getRunId(runType) {
	$('#runId').empty();
	$('#runId').selectpicker('destroy');
	$('#runId').selectpicker('refresh');
	var param = {
			"runType" : parseInt(runType),
		}
	$.ajax({
    	url: 'getRunId',
    	type: 'GET',
    	data : param,
        dataType: "json",
        async : false,
        success: function(data) {
        	for (var i = 0; i < data.length; i++) {
        	    $('#runId').append($('<option></option>').val(data[i].runId).html(data[i].runId+" - "+data[i].remark));
			}
        	$('#runId').selectpicker('refresh');
        }
    })
    setTimeout(function(){
 		$('button[data-id]').addClass('selectdropbtn');
 		$('.bs-searchbox').find('.form-control').addClass('text-theme');
 	},100);
}

/**
 * On change of runId, save the pan details from redis to database, 
 * clear that pan from redis and insert new pan from db to redis
 * @returns
 */
$("#runId").on('change', function() {
	$('#searchAddressTable').dataTable().fnClearTable();
	$('#leftbar').addClass('d-none');
	$('#masterAddressTable').empty();
	$("#PanNumber").selectpicker("deselectAll");
	
	var panNo = $("#PanNumber option:selected").text();
	var runId = $("#runId option:selected").val();
	var runType = $("#runType option:selected").val();
	if (!panNo == "") {
		insertUpdatedPan(panNo)
		clearRedis(panNo);
	}
	
	if (!panNo == "" && !runId == "" && !runType == '') {
		getSearchAddress(panNo, runId,runType);
	}
});

/**
 * Populate RunType drop down from lookup table in database 
 * @returns
 */
function getRunType(){
	$('#runType').selectpicker('destroy');
	$.ajax({
    	url: 'getRunType',
    	type: 'GET',
        dataType: "json",
        async : false,
        success: function(data) {
        	for (var i = 0; i < data.length; i++) {
        	    $('#runType').append($('<option></option>').val(data[i].code).html(data[i].value));
			}
        	$('#runType').selectpicker('refresh');
        }
    })
}

/**
 * on change of runType, populate select Run drop down.
 * @returns
 */
var table = '<table id="masterAddressTable" class="table table-striped table-bordered" style="width:100%;"><thead><tr></tr><tbody></tbody></table>';

$("#runType").on('change', function() {
	$('#runId').empty();
	$('#runId').selectpicker('destroy');
	$('#runId').selectpicker('refresh');
	$('#searchAddressTable').dataTable().fnClearTable();
	$('#leftbar').addClass('d-none');
	$('#masterAddressTable').empty();
	$('#masterTableDiv').empty();
	$('#masterTableDiv').append(table);
	
	$("#PanNumber").selectpicker("deselectAll");
	$("#runId").selectpicker("deselectAll");
	var runType = $("#runType option:selected").val();
	if (runType != "") {
		getRunId(runType);
	}
});

 /**
  * Populate search address table
  * @param panNo
  * @param runId
  * @returns
  */
function getSearchAddress(panNo, runId, runType) {
	$('#loader').show();
	var param = {
			"panNo" : panNo,
			"runId" : runId, 
			"runType" : runType,
		}
		$.ajax({
			url : 'getSearchAddress',
			type : 'GET',
			data : param,
			async : false,
			complete: function () {
				 setTimeout(function() {
					 $('#loader').hide();
 		        }, 100);
				$('#leftbar').removeClass('d-none');
	        },
			success : function(data) {
				var isPanExist = data.isPanExist;
				// Clear datatable before initializing to empty table
				$('#searchAddressTable').dataTable().fnClearTable();
//				$('#searchAddressTable thead th').removeClass('sorting_asc');
				if (isPanExist == true) {
					$("#DetailErrorModal").show();
					document.getElementById("modal_err_msg").innerHTML = data.userName +' is working on this pan number.';
    			    document.getElementById('errorModel').className = 'alert alert-success';
    			    $('#DetailErrorModal').modal({
    			    	backdrop: false,
    			    	keyboard: false,
    			        show: true
    		        }),
    		        setTimeout(function() {
    		        	$('#DetailErrorModal').modal('hide');
    		        }, 5000);
				}
				if (data.searchAddress.length > 0) {
					$('#searchAddressTable').DataTable({
						"responsive" : false,
						"scrollCollapse": true,
						"scrollX" : true,
					    "paging": false,
		            	"info": false,
					    "searching": true,
						"data" : data.searchAddress,
						'destroy' : true,
						"aaSorting": [],
						"language" : {
							"emptyTable" : "No data available."
						},
						"aoColumns" : [ {
							data : "srNo",
							targets: "0",
							className : 'text-left',
							width : '10%'
						},{
							data : "saId",
							className : 'text-left',
						}, {
							data : "address",
							className : 'text-left',
						} ],
						columnDefs: [
					        { targets: [1], visible: false},
					        {targets: [0], "orderable": false}
					    ],
					    // Insert data-row-id attribute in TR tag  
						"fnCreatedRow" : function(nRow, data, iDataIndex) {
							$(nRow).attr('data-row-id', data.saId);
						},
						"fnRowCallback" : function(nRow, aData, iDisplayIndex){      
	                         var oSettings = $("#searchAddressTable").dataTable().fnSettings();
	                          $("td:first", nRow).html(oSettings._iDisplayStart+iDisplayIndex +1);
	                          return nRow;
						},
					});
					// click on row, add and remove 'data-status' attribute and call getMasterAddress function.
					$('tr[data-row-id]').on('click', function(e) {
						var data = $('#searchAddressTable').DataTable().row(this).data();
						var sa_id = data.saId;
						var run_id = $('#runId').val();
						var getStatus = $("#searchAddressTable").attr('data-row-id');
			    		if (getStatus == undefined){
			    			$('tr').removeAttr('data-status');
			    			$(this).attr('data-status',"selected");
			    		}
			    		else{
			    			$('tr').removeAttr('data-status');
			    		}
			    		if (!panNo == "" && !sa_id == 0) {
			    			getMasterAddress(panNo, sa_id, isPanExist)
			    		}
			    		
					});
				}
				// default first search address selected, populate master address table.
				if (data.searchAddress.length >= 1) {
					$('tr[data-row-id]').eq(0).attr('data-status',"selected");
					var sa_id = data.searchAddress[0].saId;
					if (!panNo == "" && !sa_id == 0) {
		    			getMasterAddress(panNo, sa_id, isPanExist)
		    		}
				}
			}
		});
}
/**
 * Populate master address table based on pan and search address 
 * @param panNo
 * @param saId
 * @returns
 */
function getMasterAddress(panNo, saId, isPanExist) {
	var runType = $("#runType option:selected").val();
	var param = {
			"panNo" : panNo,
			"saId" : saId
		}
	
	var filterobj = new Map([ 
	    ['srNo' , 'Sr No'],['mappingId' , 'Mapping Id'],['isValidated' ,'Valid'],['cersaiPanNo','C PanNo'],['cersaiAddress' , 'Cersai Address'],['area' , 'Area'],['district' ,'District']
	    ,['taluka','Taluka'],['state' , 'State'],['pinCode' , 'Pincode'],['score' ,'Score']
	    ]);

		$.ajax({
			url : 'getMasterAddress',
			type : 'GET',
			contentType: 'application/json; charset=utf-8',
//	        data:JSON.stringify(param),
			data : param,
			async : false,
			success : function(data) {
				if (data.Data.length != 0) {
					var setHeight = getHeight - 360 ;
					 var colsData=[];
					 $("#masterAddressTable").removeClass();
					 // get dynamic coloumn for master/cersai table based on selected run type.
					 if (runType == 1) {
						 $('#masterAddressTable').addClass('masteraddress table table-striped table-bordered dataTable no-footer');
						 var headerList = ["srNo", "mappingId","isValidated","area", "district", "taluka", "state", "pinCode", "score"];
						  var summaryReport=data.Data;
						  console.log(data);
						  if(data.Data.length!=0){
							  $.each(headerList, function(index, element){
								  var cols={
										"title":element,
										"data":element
								  }
								  colsData.push(cols);
								 });
						  }
					}else {
						 $('#masterAddressTable').addClass('cersaiaddress table table-striped table-bordered dataTable no-footer');
						var headerList = ["srNo", "mappingId","isValidated","cersaiPanNo", "cersaiAddress",  "area", "district", "taluka", "state", "pinCode", "score"];
						  var summaryReport=data.Data;
						  console.log(data);
						  if(data.Data.length!=0){
							  $.each(headerList, function(index, element){
								  var cols={
										"title":element,
										"data":element
								  }
								  colsData.push(cols);
								 });
						  }
					}

					$('#masterAddressTable').DataTable({
						"responsive" : false,
						"scrollCollapse": true,
						"scrollX" : true,
					    "paging": false,
					    "info": false,
					    "searching": true,
						"data" : data.Data,
						"aaSorting": [],
						scrollY:setHeight,
//				        scrollCollapse: true,
				        scroller:       true,
						'destroy' : true,
						"language" : {
							"emptyTable" : "No data available."
						},
						"columns":colsData,
						"fnRowCallback" : function(nRow, aData, iDisplayIndex){      
	                         var oSettings = $("#masterAddressTable").dataTable().fnSettings();
	                          $("td:first", nRow).html(oSettings._iDisplayStart+iDisplayIndex +1);
	                          return nRow;
						},
						columnDefs: [
					        {targets: [1], visible: false},
					        {targets: [0,2], "orderable": false}
					    ],
					});
					
					var masterAddressTable = $('#masterAddressTable').DataTable();
					var masterAddressTableNodes = masterAddressTable.rows().nodes();
					masterAddressTableNodes.each(function(value, index){
						var presentData = masterAddressTable.row(masterAddressTable.row(masterAddressTableNodes[index]).index()).data();
						$.each(presentData, function(index, element) {
							buttons="";
							if (index == 'isValidated') {
								if (isPanExist == true) {
									if (element == 1) {
										buttons+='<button type="button" disabled class="btn btn-default btn-sm valid-btn valid-btn-active" onclick="validAddress(\''+panNo+'\',\''+saId+'\',\''+presentData.mappingId+'\',\''+0+'\');"><i class="far fa-check-circle fas"></i></button>'
									} else {
										buttons+='<button type="button" disabled class="btn btn-default btn-sm valid-btn" onclick="validAddress(\''+panNo+'\',\''+saId+'\',\''+presentData.mappingId+'\',\''+1+'\');"><i class="far fa-check-circle"></i></button>'
									}
								} else {
									if (element == 1) {
										buttons+='<button type="button" class="btn btn-default btn-sm valid-btn valid-btn-active" onclick="validAddress(\''+panNo+'\',\''+saId+'\',\''+presentData.mappingId+'\',\''+0+'\');"><i class="far fa-check-circle fas"></i></button>'
									} else {
										buttons+='<button type="button" class="btn btn-default btn-sm valid-btn" onclick="validAddress(\''+panNo+'\',\''+saId+'\',\''+presentData.mappingId+'\',\''+1+'\');"><i class="far fa-check-circle"></i></button>'
									}
								}
								$(value.cells[1]).html(buttons);
							}
							if (index == 'score') {
								$(value.cells[9]).html(Number(element).toLocaleString('en-IN'));
							}
						});
					});
				}
				
			}
		});
		$('.table tr th').each( function (e) {
	        var filtertype = $(this).text();
	        $(this).html(filterobj.get(filtertype));

	 });
		setTimeout(function(){
			$($.fn.dataTable.tables(true)).DataTable().columns.adjust();
		},200);
	 
}

/**
 * click on valid button, change the validation flag on UI
 * @param panNo
 * @param saId
 * @param amId
 * @param isValid
 * @returns
 */
function validAddress(panNo,saId,mappingId,isValid) {
	var param = {
			"panNo" : panNo,
			"saId" : saId,
			"mappingId" : mappingId,
			"isValid": isValid
		}
	$.ajax({
    	url: 'validatedAddress',
    	type: 'GET',
    	data : param,
        dataType: "json",
        async : false,
        success: function(data) {
        	if (data == 1) {
        		getMasterAddress(panNo, saId);
			}
        	
        }
    })
}

/**
 * Click on next/prev button, save the updated pan details from redis to database.
 * @param panNo
 * @returns
 */
function insertUpdatedPan(panNo) {
	var runType = $("#runType option:selected").val();
	var param = {
			"panNo" : panNo,
			"runType": runType
		}
	$.ajax({
    	url: 'insertUpdatedPan',
    	type: 'GET',
    	data : param,
        dataType: "json",
        async : false,
        success: function(data) {
        }
    })
}

/**
 * clear redis data based on pan number
 * @param panNo
 * @returns
 */
function clearRedis(panNo) {
	var param = {
			"panNo" : panNo
		}
	$.ajax({
    	url: 'clearRedisByPan',
    	type: 'GET',
    	data : param,
        dataType: "json",
        async : false,
        success: function(data) {
        }
    })
}