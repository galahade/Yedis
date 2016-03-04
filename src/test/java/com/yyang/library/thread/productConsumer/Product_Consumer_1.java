package com.yyang.library.thread.productConsumer;

import java.util.PriorityQueue;

public class Product_Consumer_1 {
	
	private int queueSize = 10;
	
	private PriorityQueue<Integer> queue = new PriorityQueue<>(queueSize);
	
	public static void main(String[] args) {
		Product_Consumer_1 test = new Product_Consumer_1();
		Producer producer = test.new Producer();
		Consumer consumer = test.new Consumer();
		
		producer.start();
		consumer.start();
	}
	
	class Consumer extends Thread {
		@Override
		public void run() {
			consume();
		}
		
		private void consume() {
			while(true) {
				synchronized(queue) {
					if(queue.size() == 0) {
						try {
							System.out.println("The queue is empty, please wait.");
							queue.wait();
						} catch (InterruptedException e) {
							System.out.println("The consumer thread has been interupted, will exit");
							break;
						}
					}
					queue.poll();
					queue.notify();
					System.out.println(String.format("Get an element from queue, it remains %d elements", queue.size()));
					Thread.yield();
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
			while(true) {
				synchronized(queue) {
					while(queue.size() == queueSize) {
						try {
							System.out.println("Queue is full, please wait.");
							queue.wait();
						} catch (InterruptedException e) {
							System.out.println("Product has been interupted. It will exit.");
							break;
						}
					}
					queue.offer(1);
					queue.notify();
					System.out.println("The queue has been inserted a element, queue remains :" + (queueSize - queue.size()));
					Thread.yield();
				}
			}
		}
	}

}
