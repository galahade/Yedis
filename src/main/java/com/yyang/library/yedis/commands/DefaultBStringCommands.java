package com.yyang.library.yedis.commands;

import com.yyang.library.yedis.Connection;

public class DefaultBStringCommands extends BaseCommands implements StringCommands {
	
	 
	 public DefaultBStringCommands(Connection connection) {
		 super(connection);
	 }

	@Override
	public <K, V> void set(K key, V value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <V> V get(Object key) {
		//sendCommand(command, args)
		return null;
	}

}
