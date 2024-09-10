<%@page import="com.nadmat.common.WebPageBuilder"%>
<%@page import="com.nadmat.common.Config"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
    
<% 
String URL = request.getContextPath();
WebPageBuilder wb = new WebPageBuilder(URL, session); 
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%
	    out.print(wb.getHead("Dashboard"));
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
		<div class="col-xl-8 col-lg-10 col-md-6 col-sm-6 col-12">
			<div class="row">
				<div class="col-xl-4 col-lg-4 col-md-4 col-sm-6 col-12">
						<div class="card mt-3 mb-2" data-href="#">
							<div class="card-body">
								<div class="dropdown float-right">
	                          <div class="dropdown dropleft">
										  <a href="javascript:void(0);" data-toggle="dropdown">
										    <i class="fas fa-ellipsis-v"></i>
										  </a>
										  <div class="dropdown-menu">
										    <a href="javascript:void(0);" class="dropdown-item" >Setting</a>
										  </div>
										</div>
	                      </div>
	                      <h5 class="text-muted mt-0">CERSAI</h5>
	                      <hr>
	                      <h3 class="mt-3 mb-3 text-success">36,254</h3>
	                      <!-- <p class="mb-0 text-muted">
	                         <span class="text-success"><i class="fas fa-arrow-up text-success"></i> 5.27%</span>
	                         <span class="text-nowrap">Total Address</span>  
	                     </p> -->
							</div>
						</div>
				</div>
				<div class="col-xl-4 col-lg-4 col-md-4 col-sm-6 col-12">
					<div class="card mt-3 mb-2">
						<div class="card-body">
							<div class="dropdown float-right">
                          <div class="dropdown dropleft">
									  <a href="#" data-toggle="dropdown">
									    <i class="fas fa-ellipsis-v"></i>
									  </a>
									  <div class="dropdown-menu">
									    <a class="dropdown-item" href="#">Setting</a>
									  </div>
									</div>
                      </div>
                      <h5 class="text-muted mt-0">Company</h5>
                      <hr>
                      <h3 class="mt-3 mb-3 text-danger">5,254</h3>
                      <!-- <p class="mb-0 text-muted">
                         <span class="text-danger"><i class="fas fa-arrow-down text-danger"></i> 5.27%</span>
                         <span class="text-nowrap">Total Company</span>  
                     </p> -->
						</div>
					</div>
				</div>
				<div class="col-xl-4 col-lg-4 col-md-4 col-sm-6 col-12">
					<div class="card mt-3 mb-2">
						<div class="card-body">
							<div class="dropdown float-right">
                          <div class="dropdown dropleft">
									  <a href="#" data-toggle="dropdown">
									    <i class="fas fa-ellipsis-v"></i>
									  </a>
									  <div class="dropdown-menu">
									    <a class="dropdown-item" href="#">Setting</a>
									  </div>
									</div>
                      </div>
                      <h5 class="text-muted mt-0">Director</h5>
                      <hr>
                      <h3 class="mt-3 mb-3 text-warning">15,754</h3>
                      <!-- <p class="mb-0 text-muted">
                         <span class="text-warning"><i class="fas fa-arrow-down text-warning"></i> 5.27%</span>
                         <span class="text-nowrap">Total Last Month</span>  
                     </p> -->
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-xl-4 col-lg-2 col-md-6 col-sm-6 col-12">
			
		</div>
	</div>
</div>

<% 
	out.print(wb.getFooter());  
	out.print(wb.sessionTimeout());
%>
<script type="text/javascript">
 $.fn.selectpicker.Constructor.BootstrapVersion = '4';
</script>

<script type="text/javascript">
         $(document).ready(function() {
            $('.table').DataTable({
            	 responsive: true,
            	 "ordering": false,
            	 "searching": false,
            	 "paging": false,
            	 "info": false,
            	 "sorting":false
            });
         $('tr[data-row-id]').click(function(){
	    		// $('#loader').show();
	    		getStatus = $(this).attr('data-status');

	    		if (getStatus == undefined){
	    			var rowID = $(this).attr('data-status',"selected");
	    		}
	    		else{
	    			$(this).removeAttr('data-status');
	    		}
    		});
    		$('.card[data-href]').click(function(){
    			var e = $(this).attr('data-href');
    			window.location.href = e ;
    		});
    		setTimeout(function() {
				 $('#loader').hide();
	        }, 100);
         } );
      </script>
</body>
</html>