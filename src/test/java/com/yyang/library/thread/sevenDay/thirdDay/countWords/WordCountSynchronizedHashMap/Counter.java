package com.yyang.library.thread.sevenDay.thirdDay.countWords.WordCountSynchronizedHashMap;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

class Counter implements Runnable {

	private final BlockingQueue<Page> queue;
	private final Map<String, Integer> counts;
	private final static ReentrantLock lock = new ReentrantLock();

	public Counter(BlockingQueue<Page> queue, Map<String, Integer> counts) {
		this.queue = queue;
		this.counts = counts;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Page page = queue.take();
				if (page.isPoisonPill())
					break;
				Iterable<String> words = new Words(page.getText());
				for (String word : words) {
					countWord(word);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void countWord(String word) {
		lock.lock();
		try {
			Integer currentCount = counts.get(word);
			if (currentCount == null) {
				counts.put(word, 1);
			} else {
				counts.put(word, currentCount++);
			}
		} finally {
			lock.unlock();
		}
	}

}
