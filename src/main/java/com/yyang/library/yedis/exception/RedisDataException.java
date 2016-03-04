package com.yyang.library.yedis.exception;

public class RedisDataException extends RedisException {

	private static final long serialVersionUID = 4298672192892248538L;

	public RedisDataException(String message) {
		super(message);
	}

	public RedisDataException(Throwable cause) {
		super(cause);
	}

	public RedisDataException(String message, Throwable cause) {
		super(message, cause);
	}

}
