package com.yyang.library.thread.sevenDay;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Philosopher extends Thread {
	
	private static int total = 2;
	
	private Chopstick left, right;
	private Random random;
	static CyclicBarrier barrier = new CyclicBarrier(total, new Runnable() {
		public void run() {
			System.out.println("All Philosopher are ready");
		}
	});
	
	public static void main(String[] args) {
		Chopstick[] chopsticks = new Chopstick[total];
		Philosopher[] pers = new Philosopher[total];
		for(int i = 0; i < total; i++) {
			chopsticks[i] = new Chopstick();
		} 
		
		for(int i = 0; i < total; i++) {
			pers[i] = new Philosopher(chopsticks[i],chopsticks[(i+1)%total]);
		} 
		
		for(int i = 0; i < total; i++) {
			pers[i].start();
		} 
		
		
	
	}
	
	public Philosopher(Chopstick left, Chopstick right) {
		this.left = left;
		this.right = right;
		random = new Random();
	}
	
	public void run() {
		try{
			barrier.await();
			while(true) {
				Thread.sleep(random.nextInt(10));
				synchronized (left) {
					Thread.sleep(random.nextInt(100));
					synchronized (right) {
						Thread.sleep(random.nextInt(10000));
						System.out.println(Thread.currentThread().getName() + "is eating");
					}
				}
			}
		} catch (InterruptedException e) {
			
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	static class Chopstick {
}
	
}
