<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	try {
	if (null != session.getAttribute("is_login")) {
		if (session.getAttribute("is_login").equals("1")) {
			response.sendRedirect(request.getContextPath() +"/dashboard");
		return;
			}
		}
	} catch (Exception e) {
	System.err.println("LOGIN PAGE EXCEPTION " + e);
}
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>NADMAT</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv='cache-control' content='no-cache'>
	<meta http-equiv='expires' content='0'>
	<meta http-equiv='pragma' content='no-cache'>
	<meta name="apple-mobile-web-app-capable" content="yes">
	<link rel='shortcut icon' href='assets/image/logo.png' type='image/x-icon'/>
	<link rel="stylesheet" type="text/css" href="assets/css/bootstrap.min.css">
	<link rel="stylesheet" href="assets/fontawesome/web-fonts-with-css/css/fontawesome-all.css">
	<link rel="stylesheet" type="text/css" href="assets/css/dataTables.bootstrap4.css">
	<link rel="stylesheet" type="text/css" href="assets/css/responsive.bootstrap4.min.css">
	<link rel="stylesheet" type="text/css" href="assets/css/bootstrap-select.min.css">
	<link rel="stylesheet" type="text/css" href="assets/css/jquery-ui.css">
	<link rel="stylesheet" type="text/css" href="assets/css/root-blue.css">
	<link rel="stylesheet" type="text/css" href="assets/css/Radix-theme.css">
	<!-- <link rel="stylesheet" type="text/css" href="assets/css/bootstrap-multiselect.css"> -->
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
<header>
 <nav class="navbar navbar-expand-md navbar-light fixed-top">
    <a class="navbar-brand logo" href="#"><img src="assets/image/logo.png"><span class="brand-name">Address Matching</span></a>
 </nav>
</header>
<div class="container-fluid d-flex vh-100">
	<div class="login-module">
        <div class="form">
				<h4 class="text-center title-theme">Login</h4>
				<form id="form_login" method="POST" action="${login}" class="form-signin">
					<div id="msgError" class="alert alert-danger alert-dismissibles">
						<button type="button" class="close" data-dismiss="alert">&times;</button> 
						<span >${error}</span>
					</div>
					<div class="form-group">
						<label>Username</label>
						<input type="text" class="form-control text-theme" autofocus="" id="user_name" name="username" tab-index="1" onkeyup="trimUserName(this);" placeholder="Username" 
							required="required" oninvalid="this.setCustomValidity('Please enter the Username.')" oninput="this.setCustomValidity('')" title=""/>
					</div>
					<div class="form-group">
						<label>Password</label>
						<input type="password" class="form-control text-theme" id="user_password" name="password" tab-index="2" placeholder="Password" required="required"
							oninvalid="this.setCustomValidity('Please enter the Password')" oninput="this.setCustomValidity('')" title=""/>
					</div>
					<div class="text-center">
						<button type="submit" class="btn btn-info btn-default-theme" id="btn_login">Login</button>
					</div>
				</form>
			</div>
        <br>
    </div>
</div>

<script type="text/javascript" src="assets/js/plugins/jquery.min.js"></script>
<script type="text/javascript" src="assets/js/plugins/popper.min.js"></script>
<script type="text/javascript" src="assets/js/plugins/bootstrap.min.js"></script>
<script type="text/javascript" src="assets/js/plugins/jquery-ui.js"></script>
<script type="text/javascript" src="assets/js/plugins/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="assets/js/plugins/dataTables.bootstrap4.min.js"></script>
<script type="text/javascript" src="assets/js/plugins/dataTables.responsive.min.js"></script>
<script type="text/javascript" src="assets/js/plugins/responsive.bootstrap4.min.js"></script>
<script type="text/javascript" src="assets/js/plugins/bootstrap-select.min.js"></script>
<script type="text/javascript" src="assets/js/plugins/jquery.slider.js"></script>

<script>
$(document).ready(function(){
	var url = window.location.href;
    url = url.split('?')[0]; 

	var error = "${error}";
    if(error == ""||error == null){
       $("#msgError").hide();
    }
    setTimeout(function() {
    	$("#msgError").hide();
    	window.history.replaceState({}, document.title, url);
    }, 3000);
    
}); 

function trimUserName(e) {
	  var txt = $("#user_name");
	  var func = function() {
	    txt.val(txt.val().replace(/\s/g, ''));
	  }
	  txt.keyup(func).blur(func);
	}
</script>
<script type="text/javascript">
	$(document).ready(function()	{
		sessionStorage.clear();
		localStorage.clear();
		setTimeout(function() {
			 $('#loader').hide();
        }, 100);        
	});
</script>

</body>
</html>