<%@page import="com.sun.xml.txw2.Document"%>
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
	    out.print(wb.getHead("Validation"));
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
                <img src="assets/image/c-logo.png" class="img2 ">
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
       	<div class="col-xl-10 col-lg-9 col-md-9 col-sm-9 col-12">
       		<div class="row">
       			<div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
		            <label><b>Search String</b></label>
                    <label class="float-right"><a href="#" class="p-0" data-search-status="string" onclick="editSearchString(this);" id="editaddress"><i class="fas fa-search" style="font-size: 18px;"></i></a></label>
		            <div class="form-group m-0">
		               <textarea class="text-theme p-1 rounded m-0 form-control" id="addressarea" rows="3" style="user-select: none;resize: none;" disabled></textarea>
		            </div>
		        </div>
	        </div>
         </div>
         <div class="col-xl-2 col-lg-3 col-md-3 col-sm-3 col-12">
            <div class="row" id="arrowbutton">
                 <div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12">
                    <label>&nbsp;</label>
                    <div class="form-group">
                        <button type="button" class="btn btn-info btn-default-theme form-control" id="PrevPan" onclick="nextAndPrevButton('prev');">Prev</button>
                    </div>
                </div>
                <div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12 text-right">
                    <label>&nbsp;</label>
                    <div class="form-group">
                         <button type="button" class="btn btn-info btn-default-theme form-control" id="NextPan" onclick="nextAndPrevButton('next');">Next</button>
                    </div>
                </div>
                <div class="col-6 offset-md-6 offset-sm-6 text-right">
                       <span class="badge badge-pill badge-secondary p-1 font-weight-normal w-100" id="progressBar"></span>
                </div>
            </div>
            <div class="row d-none" id="searchbutton">
                 <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
                    <label>&nbsp;</label>
                    <div class="form-group">
                        <button type="button" class="btn btn-info btn-default-theme form-control" id="search" onclick="searchAdressButton();">Search String</button>
                    </div>
                </div>
            </div>
            <div class="row d-none" id="savesearch">
                 <div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12">
                    <label>&nbsp;</label>
                    <div class="form-group">
                        <button type="button" class="btn btn-info btn-default-theme form-control" id="savepan" onclick="nextAndPrevButton('onReload');">Save</button>
                    </div>
                </div>
                <div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12 text-right">
                    <label>&nbsp;</label>
                    <div class="form-group">
                         <button type="button" class="btn btn-info btn-default-theme form-control" id="cancelpan" onclick="cancelButton('current');">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <hr>
    	<div class="row">
           <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
           		<table id="validation" class="table table-striped table-bordered validation" style="width:100%">
                    <thead>
                       <tr>
                          <th class="text-center" style="vertical-align: middle;">Sr.No.</th>
                          <th class="text-center" style="vertical-align: middle;" >MD Id</th>
                          <th class="text-center" style="vertical-align: middle;">Valid</th>
                          <th class="text-center" style="vertical-align: middle;">Search String</th>
                          <th class="text-center" style="vertical-align: middle;">Score</th>
                          <th class="text-center" style="vertical-align: middle;">Area</th>
                          <th class="text-center" style="vertical-align: middle;">District</th>
                          <th class="text-center" style="vertical-align: middle;">Taluka</th>
                          <th class="text-center" style="vertical-align: middle;">State</th>
                          <th class="text-center" style="vertical-align: middle;">Pincode</th>
                       </tr>
                    </thead>
                    <tbody>
                   </tbody>
               </table>
           </div>
       </div>
</div>
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

<script type="text/javascript" src="assets/js/app/validation.js"></script>
<script type="text/javascript" src="assets/js/app/messages.js"></script>

</body>
</html>