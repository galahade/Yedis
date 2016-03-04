package com.yyang.library.yedis.util;

import static com.yyang.library.yedis.protocol.RESProtocol.DOLLAR_BYTE;

import java.io.FilterOutputStream;
import java.io.IOException;

public class RESPOutputStream extends FilterOutputStream {
	
	RedisBufferedOutputStream rbOut;

	public RESPOutputStream(RedisBufferedOutputStream out) {
		super(out);
		rbOut = out;
	}
	
	/**
	 * Bulk String 用于代表一个binary-safe 单个字符串，最大512MB。
	 * Bulk String采用以下方式编码：
	 *- 以”$”开始后面接整个字符串的字节长度，以CRLF为结尾。
	 *- 实际的字符串数据。
	 *- 最后的CRLF。
	 * @param b
	 * @throws IOException
	 */
	public void writeBulkString(final byte[] b) throws IOException {
		this.write(DOLLAR_BYTE);
		this.writeIntCRLF(b.length);
		this.write(b);
		this.writeCRLF();
	}
	
	@Override
	public void flush() throws IOException {
		out.flush();
	}
	
	public void write(final byte b) throws IOException {
		rbOut.write(b);
	}

	public void write(final byte[] b) throws IOException {
		rbOut.write(b);
	}

	public void write(final byte[] b, final int off, final int len) throws IOException {
		rbOut.write(b, off, len);
	}

	public void writeCRLF() throws IOException {
		rbOut.writeCRLF();
	}
	
	public void writeIntCRLF(int value) throws IOException {
		rbOut.writeIntCRLF(value);
	}

}
