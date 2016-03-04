package com.yyang.library.yedis;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

import javax.annotation.concurrent.NotThreadSafe;

import com.yyang.library.yedis.commands.ConnectionCommands;
import com.yyang.library.yedis.commands.DefaultBStringCommands;
import com.yyang.library.yedis.commands.DefaultConnectionCommands;
import com.yyang.library.yedis.commands.StringCommands;
import com.yyang.library.yedis.exception.RedisConnectionException;
import com.yyang.library.yedis.protocol.RESProtocol;
import com.yyang.library.yedis.util.RESPBufferedInputStream;
import com.yyang.library.yedis.util.RESPOutputStream;
import com.yyang.library.yedis.util.RedisBufferedOutputStream;
import com.yyang.library.yedis.util.SafeEncoder;

import lombok.Getter;
import lombok.Setter;

@NotThreadSafe
public class RedisBConnection implements Connection, Closeable {

	private final RedisClient client;
	private Socket socket;
	private int connectionTimeout = RESProtocol.DEFAULT_TIMEOUT;
	private int soTimeout = RESProtocol.DEFAULT_TIMEOUT;
	private boolean broken = false;
	@Getter
	@Setter
	private RESPOutputStream outputStream;
	@Getter
	private RESPBufferedInputStream inputStream;

	RedisBConnection(RedisClient client) {
		this.client = client;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	public void connect() {
		if (!isConnected()) {
			try {
				socket = new Socket();
				socket.setReuseAddress(true);
				// Will monitor the TCP connection is valid
				socket.setKeepAlive(true);
				// close socket buffer to ensure timely delivery of data
				socket.setTcpNoDelay(true);
				// Control calls close() method to no block.
				socket.setSoLinger(true, 0);

				socket.connect(client.getSocketAddress(), connectionTimeout);
				socket.setSoTimeout(soTimeout);

				inputStream = new RESPBufferedInputStream(socket.getInputStream());

				outputStream = new RESPOutputStream(new RedisBufferedOutputStream(socket.getOutputStream()));
				;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public boolean isConnected() {
		return socket != null && socket.isBound() && !socket.isClosed() && socket.isConnected()
				&& !socket.isInputShutdown() && !socket.isOutputShutdown();
	}
	
	@Override
	public StringCommands getStringCommands() {
		connect();
		return new DefaultBStringCommands(this);
	}
	
	@Override
	public ConnectionCommands getConnectionCommands() {
		connect();
		return new DefaultConnectionCommands(this);
	}


	public String getReplyStatusCode(){
		flush();
		final byte[] resp = (byte[]) readReplyWithCheckingBroken();
		if (null == resp) {
			return null;
		} else {
			return SafeEncoder.encode(resp);
		}
	}

	protected Object readReplyWithCheckingBroken() {
		try {
			return inputStream.readReply();
		} catch (RedisConnectionException e) {
			broken = true;
			throw e;
		}
	}

	public boolean isBroken() {
		return broken;
	}

	protected void flush() {
		try {
			outputStream.flush();
		} catch (IOException e) {
			broken = true;
			throw new RedisConnectionException(e);
		}
	}

}
