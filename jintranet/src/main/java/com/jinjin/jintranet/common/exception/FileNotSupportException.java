package com.jinjin.jintranet.common.exception;

/**
 * 지원되지 않는 첨부파일일 경우 발생하는 Exception
 *
 */
public class FileNotSupportException extends Exception {
	private static final long serialVersionUID = 1L;

	public FileNotSupportException(String msg) {
		super(msg);
	}

	public void printStackTrace(String key) {
		super.printStackTrace();
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
