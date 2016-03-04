package com.yyang.library.thread.sevenDay.thirdDay.countWords.WordCountProducerConsumer;

interface Page {
	
	public default boolean isPoisonPill() { return false; }
	
	public String getTitle();

	public String getText();

}
