/*package com.jinjin.jintranet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	
	  @Autowired private SPLAuthenticationSuccessHandler successHandler;
	  
	  @Autowired private SPLAuthenticationFailureHandler failureHandler;
	 
	@Autowired
	private SecurityService securityService;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
		.antMatchers("/common/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/login2.do" , "/loginProcess.do" , "/loginAdaptor.do")
		.permitAll()
		.antMatchers("/notice/write.do")
		.hasRole("ADMIN1")
		.antMatchers("/admin/member.do" , "/admin/company.do")
		.hasRole("ADMIN2")
		.antMatchers("/admin/schedule.do" , "/admin/commuting.do")
		.hasRole("ADMIN3")
		.antMatchers("/supply/edit/status.do")
		.hasRole("ADMIN4")
		.antMatchers("/admin/company.do")
		.hasRole("ADMIN5")
		.anyRequest().authenticated()
		.and().csrf().disable()
		.logout().logoutUrl("/logout.do").logoutSuccessUrl("/login.do");

		http.formLogin()
		.loginPage("/login.do")
		.defaultSuccessUrl("/main.do")
		.loginProcessingUrl("/loginProc");
		

	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(securityService);
	}
}*/
