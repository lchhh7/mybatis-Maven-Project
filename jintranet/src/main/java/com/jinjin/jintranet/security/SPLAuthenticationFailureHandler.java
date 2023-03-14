package com.jinjin.jintranet.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SPLAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SPLAuthenticationFailureHandler.class);

	private String loginidname;
	private String loginpasswdname;
	private String exceptionmsgname;
	private String defaultFailureUrl;

	public SPLAuthenticationFailureHandler() {
		this.loginidname = "j_username";
		this.loginpasswdname = "j_password";
		this.exceptionmsgname = "securityexceptionmsg";
		this.defaultFailureUrl = "/project.do";
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		
		LOGGER.info("onAuthenticationFailure");

		String errorCode;
		if (exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
			/* BadCridential */
			errorCode = "패스워드를 확인하세요.";
		} else if (exception.getClass().isAssignableFrom(InternalAuthenticationServiceException.class)) {
			errorCode = "아이디가 존재하지 않습니다.";
		} else if (exception.getClass().isAssignableFrom(LockedException.class)) {
			/* UseYn 값이 N 인 경우 */
			errorCode = "사용이 허가되지 않은 계정입니다.";
		} else {
			errorCode = "정의되지 않은 에러 발생";
		}
		
		LOGGER.info("" + exception);
		request.setAttribute("errorCode", errorCode);
		request.getRequestDispatcher(defaultFailureUrl).forward(request, response);
	}

	public String getLoginidname() {
		return loginidname;
	}

	public void setLoginidname(String loginidname) {
		this.loginidname = loginidname;
	}

	public String getLoginpasswdname() {
		return loginpasswdname;
	}

	public void setLoginpasswdname(String loginpasswdname) {
		this.loginpasswdname = loginpasswdname;
	}

	public String getExceptionmsgname() {
		return exceptionmsgname;
	}

	public void setExceptionmsgname(String exceptionmsgname) {
		this.exceptionmsgname = exceptionmsgname;
	}

	public String getDefaultFailureUrl() {
		return defaultFailureUrl;
	}

	public void setDefaultFailureUrl(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}

	public static Logger getLogger() {
		return LOGGER;
	}

}
