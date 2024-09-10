package com.nadmat.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This class is navigate to relevant web-page and call to jsp files.
 * 
 * @author Vishal
 * @since 2022-02-15
 */

/** Change log
 * ---------------------------------------------------------------------------
 * Date     	Author    	Comment
 * ---------------------------------------------------------------------------
 * 28-03-22		Vishal		- Add login and logout method for redirect page.
 */

@Controller
public class PageController {
	
	/**
	 * redirect the user to login page and show the error when Invalidate user.
	 * @param model
	 * @param error
	 * @param session
	 * @return
	 */
	@GetMapping("/login")
	public String login(Model model, String error, HttpSession session) {
		if (error != null)
			model.addAttribute("error", "Invalid username and/or password.");
		return "login";
	}
	
	/**
	 * redirect the user to search address page when click on search address button on navigation bar
	 * @param model
	 * @return
	 */
	@GetMapping({"/searchAddress"})
    public String serachAddress(Model model) {
        return "searchAddress";
	}
	
	/**
	 * redirect the user to dashboard page when click on dashboard button on navigation bar
	 * @param model
	 * @return
	 */
	@GetMapping({"/dashboard"})
    public String dashboard(Model model) {
        return "dashboard";
	}
	
	/**
	 * redirect the user to validation page when click on validation button on navigation bar
	 * @param model
	 * @return
	 */
	@GetMapping({"/validation"})
    public String validation(Model model) {
        return "validation";
	}
	
	/**
	 * redirect the user to login page when click on Logout button in navigation bar
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping({ "/logout" })
	public String logout(Model model, HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
		return "redirect:/login";
	}
}
