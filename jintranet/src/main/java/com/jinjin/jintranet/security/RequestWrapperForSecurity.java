package com.jinjin.jintranet.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RequestWrapperForSecurity extends HttpServletRequestWrapper {

	private String username = null;
	private String password = null;
 
	public RequestWrapperForSecurity(HttpServletRequest request, String username, String password) {
		super(request);
 
		this.username = username;
		this.password = password;
	}
 
	@Override
	public String getRequestURI() {
		return ((HttpServletRequest)super.getRequest()).getContextPath() + "/j_spring_security_check";
	}
 
	@Override
	public String getParameter(String name) {
        if (name.equals("j_username")) {
        	return username;
        }
 
        if (name.equals("j_password")) {
        	return password;
        }
 
        return super.getParameter(name);
    }
}
