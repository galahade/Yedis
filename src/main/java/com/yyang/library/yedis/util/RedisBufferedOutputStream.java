package com.yyang.library.yedis.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import com.yyang.library.yedis.protocol.RESProtocol;

public class RedisBufferedOutputStream extends FilterOutputStream {

	final private ByteBuffer buffer;

	public RedisBufferedOutputStream(final OutputStream out) {
		this(out, 8192);
	}

	public RedisBufferedOutputStream(final OutputStream out, final int size) {
		super(out);
		if (size <= 0) {
			throw new IllegalArgumentException("Buffer isze <= 0");
		}
		buffer = ByteBuffer.allocate(size);
	}

	private void flushBuffer() throws IOException {
		if (buffer.position() > 0) {
			// prepare to be read
			buffer.flip();
			out.write(buffer.array(), 0, buffer.limit());
			buffer.clear();
		}
	}

	public void write(final byte b) throws IOException {
		if (!buffer.hasRemaining()) {
			flushBuffer();
		}
		buffer.put(b);
	}

	public void write(final byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	public void write(final byte[] b, final int off, final int len) throws IOException {
		if (len > buffer.limit()) {
			flushBuffer();
			out.write(b, off, len);
		} else {
			if (buffer.remaining() < len) {
				flushBuffer();
			}
			buffer.put(b, off, len);
		}
	}

	public void writeCRLF() throws IOException {
		if (buffer.remaining() < 2) {
			flushBuffer();
		}

		buffer.put(RESProtocol.CARRIAGE_RETURN);
		buffer.put(RESProtocol.LINE_FEED);
	}

	private final static int[] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999,
			Integer.MAX_VALUE };

	private final static byte[] DigitTens = { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1',
			'1', '1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3',
			'3', '3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5',
			'5', '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7',
			'7', '7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9',
			'9', };

	private final static byte[] DigitOnes = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', };

	private final static byte[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	public void writeIntCRLF(int value) throws IOException {
		if(value < 0) {
			write((byte) '-');
			value = -value;
		}
		
		int size = 0;
		while(value > sizeTable[size]) {
			size++;
		}
		size ++;
		if(size > buffer.remaining()) {
			flushBuffer();
		}
		
		int q, r;
		int charPos = buffer.position() + size;
		buffer.position(charPos);
		
		while (value >= 65536) {
			q = value / 100;
			r = value - ((q << 6) + (q << 5) + (q << 2));// = value - (q * 100) = value % 100
			value = q; // value final equal value's first three number e.g. 65536's value = 655
			//get value's number after first three separately. e.g. 65536 will insert to buffer '3' and '6'
			buffer.put(--charPos, DigitOnes[r]);
			buffer.put(--charPos, DigitTens[r]);
			
		}
		for(;;) {
			q = (value * 52429) >>> (16 + 3);
			r = value - ((q << 3) + (q << 1));
			buffer.put(--charPos,digits[r]);
			value = q;
			if (value == 0) break;
		}

		
		writeCRLF();
	}

	@Override
	public void flush() throws IOException {
		flushBuffer();
		out.flush();
	}

}
