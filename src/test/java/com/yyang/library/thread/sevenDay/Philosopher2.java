package com.yyang.library.thread.sevenDay;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.yyang.library.thread.sevenDay.Philosopher.Chopstick;

public class Philosopher2 extends Thread {
	private boolean eating;
	private Philosopher2 left;
	private Philosopher2 right;
	private ReentrantLock table;
	private Condition condition;
	private Random random;
	
	private static int total = 2;
	public static void main(String[] args) {
		Philosopher2[] pers = new Philosopher2[total];
		ReentrantLock table = new ReentrantLock();
		
		for(int i = 0; i < total; i++) {
			pers[i] = new Philosopher2(table);
		} 
		
		for(int i = 0; i < total; i++) {
			pers[i].start();
		} 
		
	}
	
	public Philosopher2(ReentrantLock table) {
		eating = false;
		this.table = table;
		condition = table.newCondition();
		random = new Random();
	}
	
	public void setLeft(Philosopher2 left) {this.left = left;}
	public void setRight(Philosopher2 right) { this.right = right;}
	
	public void run() {
		try {
			while(true) {
				think();
				eat();
			}
		} catch(InterruptedException e) {}
		
	}
	
	private void think() throws InterruptedException {
		table.lock();
		try {
			eating = false;
			left.condition.signal();
			right.condition.signal();
		} finally { table.unlock();}
		Thread.sleep(1000);
	}
	
	private void eat() throws InterruptedException {
		table.lock();
		try {
			while(left.eating || right.eating) {
				condition.await();
			}
			this.eating = true;
		} finally {
			table.unlock();
		}
		Thread.sleep(1000);
	}
}
