package com.yyang.library.yedis.commands;

public interface ConnectionCommands {

	/**
	 * Returns message.
	 * @return the message send to server with Bulk String format
	 */
	String echo(String message);

	/**
	 * Returns PONG if no argument is provided, otherwise return a copy of the argument as a bulk. 
	 * This command is often used to test if a connection is still alive, or to measure latency.
	 * @return "PONG"
	 */
	String ping();
	
	/**
	 * Returns PONG if no argument is provided, otherwise return a copy of the argument as a bulk. 
	 * This command is often used to test if a connection is still alive, or to measure latency.
	 * @return "PONG"
	 */
	String ping(String message);

	/**
	 * Ask the server to close the connection. The connection is closed as soon
	 * as all pending replies have been written to the client.
	 */
	void quit();

	/**
	 * Select the DB with having the specified zero-based numeric index. New
	 * connections always use DB 0
	 */
	void select(int index);

}
