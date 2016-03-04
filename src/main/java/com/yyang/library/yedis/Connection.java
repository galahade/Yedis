package com.yyang.library.yedis;

import com.yyang.library.yedis.commands.ConnectionCommands;
import com.yyang.library.yedis.commands.StringCommands;
import com.yyang.library.yedis.util.RESPBufferedInputStream;
import com.yyang.library.yedis.util.RESPOutputStream;

public interface Connection {
	
	public RESPOutputStream getOutputStream();
	
	public void setOutputStream(RESPOutputStream out);
	
	public RESPBufferedInputStream getInputStream();
	
	public String getReplyStatusCode();
	
	public StringCommands getStringCommands();
	
	public ConnectionCommands getConnectionCommands();

}
