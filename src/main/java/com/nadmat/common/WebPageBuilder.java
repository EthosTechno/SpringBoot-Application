package com.nadmat.common;

import javax.servlet.http.HttpSession;

/**
 * @author Vishal
 */

/** Change log
 * ---------------------------------------------------------------------------
 * Date			Author		Comment
 * ---------------------------------------------------------------------------
 * 14-06-2022	Vishal		- Add Validation bar on header
 */

/**
* <h3> Helper class for all the JSPs </h3>
*  -- HashMap for screen access 
*/

public class WebPageBuilder {
	private String URL = "/";
	HttpSession session;
	String pageName ="";
	public WebPageBuilder(String URL, HttpSession session) {
		this.URL = URL;
		this.session = session;
	}
	
	public String getBootstrap() {
		return "<link rel='stylesheet' type='text/css' href='"+URL+"assets/css/bootstrap.css?v="+Config.appVersion()+"'>";
	}
	
	/**
	 * This method is used to prepare dynamic header(title), set theme according to selected themeId, and load css files
	 * @param title
	 * @return
	 */
	public String getHead(String title) {
		pageName = title;
		return 	"<title>" + title + "</title>"+
				"<meta charset='utf-8'>"+
				"<meta http-equiv='X-UA-Compatible' content='IE=edge'>"+
				"<meta http-equiv='cache-control' content='no-cache'>"+
				"<meta http-equiv='expires' content='0'>"+
				"<meta http-equiv='pragma' content='no-cache'>"+
				"<meta name='viewport' content='width=device-width, initial-scale=1'>"+
				"<meta name='apple-mobile-web-app-capable' content='yes'>"+
	
				"<link rel='shortcut icon' href='assets/image/logo.png' type='image/x-icon'/>"+
				"<link rel='stylesheet' type='text/css' href='assets/css/bootstrap.min.css?v="+Config.appVersion()+"'/>"+
				"<link rel='stylesheet' type='text/css' href='assets/css/root-blue.css?v="+Config.appVersion()+"'/>"+
				"<link rel='stylesheet' href='assets/fontawesome/web-fonts-with-css/css/fontawesome-all.css?v="+Config.appVersion()+"'>"+
				"<link rel='stylesheet' type='text/css' href='assets/css/bootstrap-select.min.css?v="+Config.appVersion()+"'>"+
				"<link rel='stylesheet' type='text/css' href='assets/css/dataTables.bootstrap4.css?v="+Config.appVersion()+"'/>"+
				"<link rel='stylesheet' type='text/css' href='assets/css/responsive.bootstrap4.min.css?v="+Config.appVersion()+"'/>"+
				"<link rel='stylesheet' type='text/css' href='assets/css/jquery-ui.css?v="+Config.appVersion()+"'>"+
				"<link rel='stylesheet' href='assets/css/buttons.bootstrap4.min.css?v="+Config.appVersion()+"'/>"+
				"<link rel='stylesheet' type='text/css' href='assets/css/dix-theme.css?v="+Config.appVersion()+"'>";
	
	}

	/**
	 * This method is used to prepare dynamic navigation against user access rights through project
	 * @param user_name
	 * @return
	 */
	public String getPBNavigation(String user_name) {
		String _navigation_menu = "<header>\n"
				+ " <nav class='navbar navbar-expand-md navbar-light fixed-top'>\n"
				+ "    <a class='navbar-brand logo' href='#'><img src='assets/image/logo.png'><span class='brand-name'>NADMAT</span></a>\n"
				+ "    <button class='navbar-toggler' type='button' data-toggle='collapse' data-target='#collapsibleNavbar'>\n"
				+ "    <span class='navbar-toggler-icon'></span>\n"
				+ "    </button>\n"
				+ "    <div class='collapse navbar-collapse' id='collapsibleNavbar'>\n"
				+ "       <ul class='navbar-nav ml-auto'>\n"
				+ "         <li class='nav-item dropdown'>\n"
				+ "	      		<a class='nav-link dropdown-toggle' href='#' data-toggle='dropdown'>Validation</a>\n"
				+ "	      	<div class='dropdown-menu dropdown-menu-right'>\n"
				+ "	      		<a class='dropdown-item validation-opt' href='"+URL+"/validation?sampleType=1' id='addressType' onclick='addressValidationType(this)'>Company Address</a>\n"
				+ "	      		<a class='dropdown-item validation-opt' href='"+URL+"/validation?sampleType=2' id = 'nameType' onclick='nameValidationType(this)'>Company Name</a>\n"
				+ "	      	</div>\n"
				+ "	    	</li>\n"
				+ "         <li class='nav-item dropdown'>\n"
				+ "	      <a class='nav-link dropdown-toggle' href='#' id='navbardrop' data-toggle='dropdown'>" +user_name +"</a>\n"
				+ "	      <div class='dropdown-menu dropdown-menu-right'>\n"
				+ "	        <a class='dropdown-item' href='#' onclick='voluntaryLogoutAll()'>Logout</a>\n"
				+ "	      </div>\n"
				+ "	    </li>\n"
				+ "       </ul>\n"
				+ "    </div>\n"
				+ " </nav>\n"
				+ "</header>";
		return _navigation_menu;
	}
	
	/**
	 * This method is used to prepare dynamic footer and load common java script system files 
	 * @return
	 */
	public String getFooter() {
		String dataTable = 	"<script type='text/javascript' src='assets/js/plugins/jquery.dataTables.min.js?v="+Config.appVersion()+"'></script>"+
							"<script type='text/javascript' src='assets/js/plugins/dataTables.bootstrap4.min.js?v="+Config.appVersion()+"'></script>"+
							"<script type='text/javascript' src='assets/js/plugins/dataTables.responsive.min.js?v="+Config.appVersion()+"'></script>"+
							"<script type='text/javascript' src='assets/js/plugins/responsive.bootstrap4.min.js?v="+Config.appVersion()+"'></script>";
		
		String selectPicker = "<script type='text/javascript' src='assets/js/plugins/bootstrap-select.min.js?v="+Config.appVersion()+"'></script>";
		
		String button = "<script type='text/javascript' src='assets/js/plugins/dataTables.buttons.min.js?v="+Config.appVersion()+"'></script>"+
						"<script type='text/javascript' src='assets/js/plugins/buttons.bootstrap4.min.js?v="+Config.appVersion()+"'></script>"+
						"<script type='text/javascript' src='assets/js/plugins/buttons.colVis.min.js?v="+Config.appVersion()+"'></script>";
		
		String jsFiles =  "<script type='text/javascript' src='assets/js/plugins/jquery.min.js?v="+Config.appVersion()+"'></script>"+
							"<script type='text/javascript' src='assets/js/plugins/popper.min.js?v="+Config.appVersion()+"'></script>"+
							"<script type='text/javascript' src='assets/js/plugins/bootstrap.min.js?v="+Config.appVersion()+"'></script>"+
							"<script type='text/javascript' src='assets/js/plugins/jquery-ui.js?v="+Config.appVersion()+"'></script>"+
							"<script type='text/javascript' src='assets/js/plugins/bootpopup.js?v="+Config.appVersion()+"'></script>"+
							"<script type='text/javascript' src='assets/js/plugins/jquery.slider.js?v="+Config.appVersion()+"'></script>"+
							"<script type='text/javascript' src='assets/js/app/common.js?v="+Config.appVersion()+"'></script>"+
							"<script type='text/javascript' src='assets/js/plugins/session/timeout.js?v="+Config.appVersion()+"'></script>"+
							"<script type='text/javascript' src='assets/js/plugins/session/jquery-idleTimeout.js?v="+Config.appVersion()+"'></script>"+
							"<script type='text/javascript' src='assets/js/plugins/session/store.legacy.min.js?v="+Config.appVersion()+"'></script>";
							
							
					
			if(pageName.equalsIgnoreCase("NADMAT")){
			
			}else if(pageName.equalsIgnoreCase("Dashboard")){
				jsFiles+=dataTable+selectPicker;
			}else if(pageName.equalsIgnoreCase("Validation")){
				jsFiles+=dataTable+selectPicker+button;
			}else if(pageName.equalsIgnoreCase("Search Address")){
				jsFiles+=dataTable+selectPicker;
			}
			return jsFiles;
	}
	/**
	 * This method is used to check session time out
	 * @return
	 */
	public String sessionTimeout() {
		return "  <script type='text/javascript'>"+
		"$(document).ready(function () {\n" + 
		"		    $(document).idleTimeout({\n" + 
		"		      redirectUrl:  'logout.jsp' // redirect to this url. Set this value to YOUR site's logout page.\n" + 
		"		    });\n" + 
		"		  });"+
		"  </script>";
	}

}
