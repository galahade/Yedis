package com.yyang.library.yedis.util;

import java.io.UnsupportedEncodingException;

import com.yyang.library.yedis.exception.RedisDataException;
import com.yyang.library.yedis.exception.RedisException;
import com.yyang.library.yedis.protocol.RESProtocol;

public class SafeEncoder {
	
	public static byte[][] encodeMany(final String... strs) {
		byte[][] many = new byte[strs.length][];
		for(int i = 0; i < strs.length; i++) {
			many[i] = encode(strs[i]);
		}
		return many;
	}

	public static byte[] encode(final String str) {
		try {
			if (str == null)
				throw new RedisDataException("value sent to redis cannot be null");

			return str.getBytes(RESProtocol.CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new RedisException(e);
		}
	}
	
	public static String encode(final byte[] data) {
		try {
			return new String(data, RESProtocol.CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new RedisException(e);
		}
	}
	
}
