package com.yyang.library.yedis.exception;

public class RedisException extends RuntimeException {

	private static final long serialVersionUID = 2701817161756727651L;

	  public RedisException(String message) {
	    super(message);
	  }

	  public RedisException(Throwable e) {
	    super(e);
	  }

	  public RedisException(String message, Throwable cause) {
	    super(message, cause);
	  }

}
