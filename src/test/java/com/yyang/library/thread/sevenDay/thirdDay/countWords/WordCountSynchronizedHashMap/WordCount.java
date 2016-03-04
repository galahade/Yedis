package com.yyang.library.thread.sevenDay.thirdDay.countWords.WordCountSynchronizedHashMap;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class WordCount {
	private static final int NUM_COUNTERS = 2;

	public static void main(String[] args) throws InterruptedException {
		ArrayBlockingQueue<Page> queue = new ArrayBlockingQueue<>(100);
		HashMap<String, Integer> counts = new HashMap<>();
		
		ExecutorService executor = Executors.newCachedThreadPool();
		for(int i = 0; i < NUM_COUNTERS; ++i) {
			executor.execute(new Counter(queue, counts));
		}
		
		Thread parser = new Thread(new Parser(queue));
		
		parser.start();
		parser.join();
		for (int i = 0; i <NUM_COUNTERS; ++i) 
			queue.put(new PoisonPill());
		executor.shutdown();
		executor.awaitTermination(10L, TimeUnit.MINUTES);
		
	}

}
