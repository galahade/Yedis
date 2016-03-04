package com.yyang.library.thread.sevenDay.thirdDay.countWords.WordCountBatchConcurrentHashMap;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

 class WordCount {
	
	public static void main(String[] args) throws InterruptedException {
		ArrayBlockingQueue<Page> queue = new ArrayBlockingQueue<>(100);
		ConcurrentHashMap<String, Integer> counts = new ConcurrentHashMap<>();
		
		Thread counter = new Thread(new Counter(queue, counts));
		Thread parser = new Thread(new Parser(queue));
		
		counter.start();
		parser.start();
		parser.join();
		queue.put(new PoisonPill());
		counter.join();
		
	}

}
