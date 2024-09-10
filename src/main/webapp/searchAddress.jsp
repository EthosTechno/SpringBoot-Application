<%@page import="com.nadmat.common.WebPageBuilder"%>
<%@page import="com.nadmat.common.Config"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="core/session.jsp"%>

<% 
String URL = request.getContextPath();
WebPageBuilder wb = new WebPageBuilder(URL, session); 
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%
	    out.print(wb.getHead("Search Address"));
	%>
</head>
<body>
<!-- loader -->
<div class="loaderbody" id="loader">
 <div class="loader-home">
        <img src="assets/image/slide-3.png" class="slide-3">
        <img src="assets/image/slide-2.png" class="slide-2">
        <img src="assets/image/slide-1.png" class="slide-1">
        <div class="loading-block">
            <div class="loading-inner-block">
                <img src="assets/image/c-logo.png" class="img2">
            </div>
        </div>
    </div>
</div>
<!-- loader -->
<%
	out.print(wb.getPBNavigation((String)session.getAttribute("user_name")));
%>
<div class="container-fluid margin-70">
	<div class="row">
       	<div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
       		<div class="row">
       			<div class="col-xl-3 col-lg-3 col-md-3 col-sm-3 col-12">
		            <label><b>Run Type</b></label>
		            <div class="form-group">
		               <select class="selectpicker form-control input-sm text-theme" multiple data-live-search="true" id="runType" data-max-options="1">
		               </select>
		            </div>
		        </div>
       			<div class="col-xl-3 col-lg-3 col-md-3 col-sm-3 col-12">
		            <label><b>Select Run</b></label>
		            <div class="form-group">
		               <select class="selectpicker form-control input-sm text-theme" multiple data-live-search="true" id="runId" data-max-options="1">
		               </select>
		            </div>
		        </div>
       			<div class="col-xl-3 col-lg-3 col-md-3 col-sm-3 col-12">
		            <label><b>PAN Number</b></label>
		            <div class="form-group">
		               <select class="selectpicker form-control input-sm text-theme" multiple data-live-search="true" id="PanNumber" data-max-options="1">
		               </select>
		            </div>
		        </div>
		        <div class="col-xl-1 col-lg-1 col-md-1 col-sm-1 col-12">
		        	<label>&nbsp;</label>
		        	<div class="form-group">
		        		<button type="button" class="btn btn-info btn-default-theme form-control" id="PrevPan" onclick="PrevPan();">Prev</button>
		        	</div>
		        </div>
		        <div class="col-xl-1 col-lg-1 col-md-1 col-sm-1 col-12 text-right">
		        	<label>&nbsp;</label>
		        	<div class="form-group">
		        	<button type="button" class="btn btn-info btn-default-theme form-control" id="NextPan" onclick="NextPan();">Next</button>
		        </div>
		        </div>
		        <div class="col-xl-1 col-lg-1 col-md-1 col-sm-1 col-12">
		        	<label>&nbsp;</label>
		        	<div class="form-group">
		        		<button type="button" class="btn btn-info btn-default-theme form-control" id="btnRun" onclick="RemarkModal();">Run</button>
		        	</div>
		        </div>
	        </div>
         </div>
    </div>
    <hr>
    <div class="d-flex border" id="panelContainer">
	    <div id='leftDiv' class="panel one p-2 d-inline-block h-100">
	    	<div class="row content">
               <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
               		<div id="leftbar" class="d-none">
               		<table id="searchAddressTable" class="table table-striped table-bordered" style="width:100%">
                        <thead>
                           <tr>
                              <th class="text-center" style="vertical-align: middle;">Sr.No.</th>
                              <th class="text-center" style="vertical-align: middle;" hidden>Sa.No.</th>
                              <th class="text-center" style="vertical-align: middle;">Search Address</th>
                           </tr>
                        </thead>
                        <tbody>
                       </tbody>
                   </table>
                   </div>
               </div>
           </div>
	    </div>
	    <div id='rightDiv' class="panel two p-2 d-inline-block h-100">
	    	<div class="row content">
               <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id= "masterTableDiv">
               		<table id="masterAddressTable" class="table table-striped table-bordered" style="width:100%;">
                        <thead>
                           <tr>
                           </tr>
                        </thead>
                        <tbody>
                       </tbody>
                   </table>
               </div>
           </div>
	    	<span class="slider"></span>
	    </div>
	</div>
</div>
<!-- The Modal -->
<div class="modal" id="remarkModal">
	<div class="modal-dialog">
    	<div class="modal-content">
        	<!-- Modal Header -->
        	<div class="modal-header">
            	<h5 class="modal-title" id="modelTitle">Add Remark</h5>
                <button type="button" class="close" data-dismiss="modal" onclick="resetSectionModel();" >&times;</button>
            </div>
            <!-- Modal body -->
            <div class="modal-body">
	        	<div class="alert" id="error" style="display:none;">
					<strong>Success!</strong> Indicates a dangerous or potentially negative action.
				</div>
                <div class="row justify-content-between">
                   	<div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
                    	<div class="form-group">
                        	<label><b>Run Name</b></label>
                            <input type="text" class="form-control text-theme" id="runName" placeholder="Run Name" required="required" maxlength="50">
                        </div>
                   	</div>
               	</div>
          	</div>
           	<!-- Modal footer -->
            <div class="modal-footer">
            	<button type="button" class="btn btn-info btn-default-theme" onclick="RunAPI(this);">Run</button>
                <button type="button" class="btn btn-info btn-default-theme" data-dismiss="modal" onclick="resetSectionModel();">Close</button>
            </div>
       	</div>
   	</div>
</div>
<!-- modal end -->
<!-- Error Modal -->
    <div class="modal fade" id="DetailErrorModal" role="dialog" style="display: none;">
        <div class="modal-lg modal-dialog">
            <div class="modal-content">
               <div class="modal-header">
                  <h5 class="modal-title">Alert</h5>
                  <button type="button" class="close" data-dismiss="modal">&times;</button>
               </div>
               <div class="modal-body">
                  <div class="alert" id="errorModel" style="margin-bottom: 0px;">
                     <span id="modal_err_msg"></span>
                  </div>
               </div>
               <div class="modal-footer">
                  <button type="button" class="btn btn-info btn-default-theme" data-dismiss="modal">Close</button>
               </div>
            </div>
        </div>
    </div>

<% 
	out.print(wb.getFooter());  
	out.print(wb.sessionTimeout());
%>
<script type="text/javascript" src="assets/js/app/searchAddress.js"></script>
<script type="text/javascript">
 $.fn.selectpicker.Constructor.BootstrapVersion = '4';

 $(document).ready(function(){
	 var getHeight = $(window).height();
	 var setHeight = getHeight - 230 ;
	 $('#panelContainer').height(setHeight);
	 });
</script>

</body>
</html>