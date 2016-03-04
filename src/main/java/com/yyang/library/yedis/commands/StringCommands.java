package com.yyang.library.yedis.commands;

public interface StringCommands {
	
	<K, V>void set(K key, V value);

	<V> V get(Object key);

}
