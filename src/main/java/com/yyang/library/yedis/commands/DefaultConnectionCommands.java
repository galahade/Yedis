package com.yyang.library.yedis.commands;

import static com.yyang.library.yedis.protocol.RESProtocol.Command.PING;

import com.yyang.library.yedis.Connection;

public class DefaultConnectionCommands extends BaseCommands implements ConnectionCommands{

	public DefaultConnectionCommands(Connection connection) {
		super(connection);
	}

	@Override
	public String echo(String message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String ping() {
		sendCommand(PING);
		return connection.getReplyStatusCode();
	}

	@Override
	public String ping(String message) {
		return null;
	}

	@Override
	public void quit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void select(int index) {
		// TODO Auto-generated method stub
		
	}

}
