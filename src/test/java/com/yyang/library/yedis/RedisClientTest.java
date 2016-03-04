package com.yyang.library.yedis;

import java.nio.charset.Charset;

import org.junit.Test;

import com.yyang.library.yedis.commands.ConnectionCommands;

public class RedisClientTest {
	
	@Test
	public void createYedisClient() {
		RedisClient  client = RedisClient.create("192.168.56.110");
		Connection connection = client.blockConnect();
		ConnectionCommands connectionCommands = connection.getConnectionCommands();
		String pong = connectionCommands.ping();
		System.out.println(pong);
	}
	
	@Test 
	public void createJedisClient() {
		
	}
	
	
	
	
	@Test 
	public void CharSetTest() {
        String csn = Charset.defaultCharset().name();
System.out.println(csn);
	}

}
