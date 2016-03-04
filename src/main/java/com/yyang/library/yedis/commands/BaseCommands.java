package com.yyang.library.yedis.commands;

import static com.yyang.library.yedis.protocol.RESProtocol.*;

import java.io.IOException;

import com.yyang.library.yedis.Connection;
import com.yyang.library.yedis.exception.RedisConnectionException;
import com.yyang.library.yedis.protocol.RESPCommand;
import com.yyang.library.yedis.util.RESPOutputStream;
import com.yyang.library.yedis.util.SafeEncoder;

public class BaseCommands {
	
	protected Connection connection;
	
	public BaseCommands(Connection connection) {
		this.connection = connection;
	}
	
	protected Connection sendCommand(final RESPCommand command) {
		return sendCommand(command.getRaw(), EMPTY_ARGS);
	}
	
	protected Connection sendCommand(final RESPCommand command, final String...args) {
		final byte[][] bargs = SafeEncoder.encodeMany(args);
		
		return sendCommand(command, bargs);
		
	}
	
	protected Connection sendCommand(final RESPCommand command, final byte[]... args) {
		
		return sendCommand(command.getRaw(), args);
	}
	
	protected Connection sendCommand(final byte[] command, final byte[]...args) {
		RESPOutputStream os = connection.getOutputStream();
		// RESP use RESP Array to send command to server.
		try {
			os.write(ASTERISK_BYTE);
			os.writeIntCRLF(args.length + 1);
			os.writeBulkString(command);
			for(final byte[] arg : args) {
				os.writeBulkString(arg);
			}
		} catch (IOException e) {
			throw new RedisConnectionException(e);
		}
		
		return null;
	}

}
