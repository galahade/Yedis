package com.yyang.library.yedis.exception;

public class RedisConnectionException extends RedisException {

	public RedisConnectionException(String message) {
		super(message);
	}

	public RedisConnectionException(Throwable cause) {
		super(cause);
	}

	public RedisConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	private static final long serialVersionUID = 4965274566324865586L;

}
