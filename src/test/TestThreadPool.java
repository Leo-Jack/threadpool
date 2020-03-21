package test;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import threadpool.ThreadPool;
import threadpool.ThreadPoolCreator;

/**
 * 
 * This is the class that test the ThreadPool class.
 * @author leo
 *
 */
public class TestThreadPool {
	static AtomicInteger count = new AtomicInteger(0);

	public static void main(String[] args) throws InterruptedException {
		ThreadPool threadPool = ThreadPoolCreator.createThreadPool();
		for(int i = 0;i < 200;++i) {
			Runnable r = new Runnable() {
				public void run() {
					for(int j = 0;j < 1000;++j) {
						count.getAndIncrement();
					}
				}
			};
			
			threadPool.submitTask(r);
		}
		threadPool.shutdown();
		for(int i = 0;i < 10;++i) {
			Runnable r = new Runnable() {
				public void run() {
					for(int j = 0;j < 1000;++j) {
						count.getAndIncrement();
					}
				}
			};
			
			threadPool.submitTask(r);
		}
		while(Thread.activeCount() > 1) {
			Thread.sleep(1000);
		}
		System.out.println(count.get());
		
	}
}
