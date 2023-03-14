package com.jinjin.jintranet.common.exception;

public class PISLogoutException extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;

	public PISLogoutException(String msg) {
		super(msg);
	}

	public PISLogoutException() {
		super("세션이 만료되어 로그인이 필요합니다.");
	}

	public void printStackTrace(String key) {
		super.printStackTrace();
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
