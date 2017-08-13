package com.rtdl.demo;
class TestThread extends Thread{
	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			System.out.println(Thread.currentThread().getName() + i);
		}
	}
}
public class FirstThread {
	public static void main(String[] args) {
		TestThread tt = new TestThread();
		tt.start();
		
		for (int i = 0; i < 100; i++) {
			System.out.println(Thread.currentThread().getName() + i);
		}
	}
}
