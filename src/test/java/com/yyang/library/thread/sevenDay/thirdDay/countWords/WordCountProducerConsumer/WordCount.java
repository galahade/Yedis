package com.yyang.library.thread.sevenDay.thirdDay.countWords.WordCountProducerConsumer;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

 class WordCount {
	
	public static void main(String[] args) throws InterruptedException {
		ArrayBlockingQueue<Page> queue = new ArrayBlockingQueue<>(100);
		HashMap<String, Integer> counts = new HashMap<>();
		
		Thread counter = new Thread(new Counter(queue, counts));
		Thread parser = new Thread(new Parser(queue));
		
		counter.start();
		parser.start();
		parser.join();
		queue.put(new PoisonPill());
		counter.join();
		
	}

}
