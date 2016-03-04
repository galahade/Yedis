package com.yyang.library.thread.productConsumer;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Product_Consumer_2 {

	private int queueSize = 10;
	private PriorityQueue<Integer> queue = new PriorityQueue<>();
	private Lock lock = new ReentrantLock();
	private Condition full = lock.newCondition();
	private Condition empty = lock.newCondition();

	public static void main(String[] args) {
		Product_Consumer_2 test = new Product_Consumer_2();
		Producer producer = test.new Producer();
		Consumer consumer = test.new Consumer();
		
		producer.start();
		consumer.start();
	}
	
	class Consumer extends Thread {
		@Override
		public void run() {
			while(true) {
				lock.lock();
				try {
					while(queue.size() == 0) {
						try {
							System.out.println("The queue is empty, please wait.");
							empty.await();
						} catch (InterruptedException e) {
							System.out.println("The consumer thread has been interupted, will exit");
							return;
						}
					}
					queue.poll();
					full.signal();
					System.out.println(String.format("Get an element from queue, it remains %d elements", queue.size()));

				} finally {
					lock.unlock();
				}
			}
		}
	}

	class Producer extends Thread {
		@Override
		public void run() {
			produce();
		}

		private void produce() {
			while (true) {
				lock.lock();
				try {
					while (queue.size() == queueSize) {
						try {
							System.out.println("Queue is full, please wait.");
							full.await();
						} catch (InterruptedException e) {
							System.out.println("Product has been interupted. It will exit.");
							return;
						}
					}
					queue.offer(1);
					empty.signal();
					System.out.println(
							"The queue has been inserted a element, queue remains :" + (queueSize - queue.size()));
				} finally {
					lock.unlock();
				}
			}
		}
	}

}
