package com.nadmat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * This class is used to responsible for all the security (protecting the application URLs, validating submitted user name 
 * and passwords, redirecting to the log in form, etc) within your application and configuration with custom login form
 * 
 * 
 * @author Vishal 
 * @since 2022-03-26
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;
	
	@Autowired
	AuthenticationSuccessHandler authenticationSuccessHandler;
	
	/**
	 * This method is used to encrypt a password.
	 */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * It's method is provide to authenticated any request to the application and authenticated with form based login or HTTP basic authentication.
     */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http 
		 .authorizeRequests()
         .antMatchers("/assets/**").permitAll()
         .anyRequest().authenticated()
         .and()
     .formLogin()
         .loginPage("/login")
         .permitAll().successHandler(authenticationSuccessHandler);

		http.csrf().disable();
	}
	
	/**
	 * This method is used to inMemoryAuthentication with Application and it authenticate with a user details and password.
	 */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}
