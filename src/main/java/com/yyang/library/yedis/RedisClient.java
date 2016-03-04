package com.yyang.library.yedis;

import static com.yyang.library.yedis.protocol.RESProtocol.*;

import java.net.InetSocketAddress;

import lombok.Getter;

public class RedisClient {
	
	@Getter
	private String host = DEFAULT_HOST;
	@Getter
	private int port = DEFAULT_PORT;
	@Getter
	private InetSocketAddress socketAddress;
	
	private RedisClient() {
		socketAddress = new InetSocketAddress(host, port);
	}
	
	private RedisClient(String host, int port) {
		this.host = host;
		this.port = port;
		this.socketAddress = new InetSocketAddress(host, port);
	}
	
	public static RedisClient create() {
		return new RedisClient();
	}
	
	public static RedisClient create(String host) {
		return new RedisClient(host, DEFAULT_PORT);
	}
	
	public static RedisClient create(String host, int port) {
		return new RedisClient(host, port);
	}
	
	public Connection blockConnect() {
		return new RedisBConnection(this);
	}
	
}
