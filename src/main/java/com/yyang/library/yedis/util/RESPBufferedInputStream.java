package com.yyang.library.yedis.util;

import static com.yyang.library.yedis.protocol.RESProtocol.*;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

import com.yyang.library.yedis.exception.RedisConnectionException;
import com.yyang.library.yedis.exception.RedisDataException;
import com.yyang.library.yedis.protocol.RESProtocol;



/**
 * @author Young Yang
 * @Email galahade@sina.com
 * @Date Feb 23, 2016
 * 
 * This class is used for accept RESP(REdis Serialization Protocol) input stream . And make cache for it.
 * It will throws error if the RESP layer requires a byte that byte is not there.
 */
@NotThreadSafe
public class RESPBufferedInputStream extends FilterInputStream {
	
    private static int DEFAULT_BUFFER_SIZE = 8192;
    
    private final byte[] buffer;
    
    private int count, limit;


	public RESPBufferedInputStream(InputStream in) {
		this(in, DEFAULT_BUFFER_SIZE);
	}
	
	public RESPBufferedInputStream(InputStream in, int size) {
		super(in);
		if(size <= 0 ) {
			throw new IllegalArgumentException("Buffer size <= 0");
		}
		this.buffer = new byte[size];
	}
	
	public byte readByte() throws RedisConnectionException {
		ensureFill();
		return buffer[count++];
	}
	
	public Object readReply() {
		final byte b = readByte();
		switch(b) {
		// reply a simple string
		case(PLUS_BYTE):
			return readLineBytes();
		case(DOLLAR_BYTE):
			return readBulkString();
		case(ASTERISK_BYTE):
			return readArray();
		case(COLON_BYTE):
			return readInteger();
		case(MINUS_BYTE):
			processError();
			return null;
		default:
			throw new RedisConnectionException("Unknow reply:" + (char) b);
		
		}
	}
	
	private void processError() {
		String message = readLine();
		throw new RedisDataException(message);
	}
	
	// read RESP Integer a 64 bit singed number
	private long readInteger() {
		return readLongCrLf();
	}
	
	public byte[] readBulkString() {
		final int len = this.readIntCrLf();
		if(len == -1)
			return null;
		
		final byte[] read = new byte[len];
		int offset = 0;
		while(offset < len) {
			final int size = read(read, offset, (len - offset));
			if(size == -1) 
				throw new RedisConnectionException("It seems like server has closed the connection.");
			offset += size;
		}
		//read 2 more bytes for the command delimiter
		readByte();
		readByte();
		return read;
	}
	
	public List<Object> readArray() {
		final int num = readIntCrLf();
		if(num == -1) {
			return null;
		}
		final List<Object> ret = new ArrayList<>(num);
		for(int i = 0; i < num; i++) {
			ret.add(readReply());
		}
		return ret;
	}
	
	/**
	 * This method read a RESP line which is end with "\r\n";
	 * @return a String with this line's byte and default encoding 
	 */
	public String readLine() {
		StringBuilder sb = new StringBuilder();
		while(true) {
			ensureFill();
			
			byte b = buffer[count++];
			if( b == RESProtocol.CARRIAGE_RETURN) {
				ensureFill();// Must be followed a '/n'
				
				byte c = buffer[count++]; 
				if(c == RESProtocol.LINE_FEED) {
					// end with "\r\n" one line end
					break;
				}
				//not end with "\r\n" this line don't end
				sb.append(b);
				sb.append(c);
			} else {
				sb.append(b);
			}
		}
		String reply = sb.toString();
		if(reply.length() == 0) {
			throw new RedisConnectionException("It seems like server has close the connection.");
		}
		return reply;
	}
	
	/**
	 * This operation should only require one fill. We optimize allocation and copy of the byte array for this situation.
	 * But if there are more than one fill is required then we take a slower path and expand a byte array output as is necessary.
	 * @return bytes array.
	 */
	public byte[] readLineBytes() {
		ensureFill();
		
		int pos = count;
		final byte[] buf = this.buffer;
		while(true) {
			if (pos == limit) {
				return readLineBytesSlowly();
			}
			if(buf[pos++] == RESProtocol.CARRIAGE_RETURN) {
				if (pos == limit) {
					return readLineBytesSlowly();
				}
				if(buf[pos++] == RESProtocol.LINE_FEED) {
					break;
				}
			}
		}
		
		final int N = (pos - count) - 2;
		final byte[] line = new byte[N];
		System.arraycopy(buf, count, line, 0, N);
		count = pos;
		return line;
	}
	
	
	/**
	 * This method is as a back－up for a optimized readLineByte() method when one fill can't hold one line.
	 * 
	 */
	private byte[] readLineBytesSlowly() {
		ByteArrayOutputStream bout = null;
		while (true) {
			ensureFill();
			
			byte b = buffer[count++];
			if (b == RESProtocol.CARRIAGE_RETURN) {
				ensureFill();// Must be followed a '/n'
				
				byte c = buffer[count++];
				if (c == RESProtocol.LINE_FEED) {
					break;
				}
				if(bout == null) {
					bout = new ByteArrayOutputStream(16);
				}
				bout.write(b);
				bout.write(c);
			} else {
				if (bout == null) {
					bout = new ByteArrayOutputStream(16);
				}
				bout.write(b);
			}
		}
		return bout == null ? new byte[0] : bout.toByteArray();
	}
	
	public int readIntCrLf() {
		return (int) readLongCrLf();
	}
	
	public long readLongCrLf() {
		final byte[] buf = this.buffer;
		
		ensureFill();
		
		final boolean isNeg = buf[count] == '-';
		if (isNeg) {
			++count;
		}
		
		long value = 0;
		while(true) {
			ensureFill();
			
			int b = buf[count++];
			if (b == RESProtocol.CARRIAGE_RETURN) {
				ensureFill();
				
				if(buf[count++] != RESProtocol.LINE_FEED) {
					throw new RedisConnectionException("Unexpected character!");
				}
				break;
			} else {
				value = value * 10 + b - '0';
			}
		}
		
		return (isNeg ? -value : value);
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws RedisConnectionException {
		ensureFill();
		
		int length = Math.min(limit - count, len);
		System.arraycopy(buffer, count, b, off, length);
		count += length;
		return length;
	}
	
	/**
	 * This methods assumes there are required bytes to be read. If we cannot
	 * read anymore bytes an exception is thrown to quickly ascertain that the
	 * stream was smaller than expected.
	 */
	private void ensureFill() {
		if (count >= limit) {
			try {
				limit = in.read(buffer);
				count = 0;
				if (limit == -1) {
					throw new RedisConnectionException("Unexpected end of stream.");
				}
			} catch (IOException e) {
				throw new RedisConnectionException(e);
			}
		}
	}

}
