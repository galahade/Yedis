package com.yyang.library.yedis.protocol;

import com.yyang.library.yedis.util.SafeEncoder;

public interface RESProtocol {
	
	public final byte[][] EMPTY_ARGS = new byte[0][];
	
	public static final String ASK_RESPONSE = "ASK";
	public static final String MOVED_RESPONSE = "MOVED";

	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 6379;
	public static final int DEFAULT_TIMEOUT = 2000;
	public static final int DEFAULT_DATABASE = 0;
	public static final String CHARSET = "UTF-8";

	public static final byte DOLLAR_BYTE = '$';
	public static final byte ASTERISK_BYTE = '*';
	public static final byte PLUS_BYTE = '+';
	public static final byte MINUS_BYTE = '-';
	public static final byte COLON_BYTE = ':';
	public static final byte CARRIAGE_RETURN = '\r';
	public static final byte LINE_FEED = '\n';
	
	public static enum Command implements RESPCommand{
		PING, SET, GET, EXISTS, DEL;

		private final byte[] raw;
		
		Command() {
			raw = SafeEncoder.encode(this.name());
		}
		@Override
		public byte[] getRaw() {
			return raw;
		}
	}

}
