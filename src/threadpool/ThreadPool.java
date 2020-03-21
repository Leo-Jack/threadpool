package threadpool;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The ThreadPool class manages threads create,run and destroy.
 * @author leo
 *
 */
public class ThreadPool {
	
	private int threadSize;
	
	private Queue<Runnable> taskQueue;
	
	private boolean isShutDown;
	
	private ReentrantLock lock;
	private Condition taskQueueNotEmpty;
	/**
	 * Default threadSize is 10.
	 */
	public ThreadPool() {
		this.threadSize = 10;
		this.taskQueue = new LinkedList<Runnable>();
		this.isShutDown = false;
		this.lock = new ReentrantLock();
		this.taskQueueNotEmpty = lock.newCondition();
		startThreadPool();
	}
	
	
	/**
	 * ThreadSize is allow to with 1 to 20.
	 * @param threadSize
	 */
	public ThreadPool(int threadSize) {
		if(threadSize > 20) {
			this.threadSize = 20;
		}
		else if(threadSize < 1){
			this.threadSize = 1;
		}
		else {
			this.threadSize = threadSize;
		}
		
		this.taskQueue = new LinkedList<Runnable>();
		this.isShutDown = false;
		this.lock = new ReentrantLock();
		this.taskQueueNotEmpty = lock.newCondition();
		startThreadPool();
	}
	
	/**
	 * Let Runnable r commit to the waiting taskQueue.
	 * And then notify all the waiting threads.
	 * If this.isShutDown is true, then you can't submit task any more.
	 * @param r
	 */
	public void submitTask(Runnable r) {
		if(this.isShutDown) {
			System.out.println("The ThreadPool has been shut down!");
			System.out.println("The task you commit will not be execute!");
		} else {
			this.lock.lock();
			this.taskQueue.add(r);
			this.taskQueueNotEmpty.signalAll();
			this.lock.unlock();
		}
	}
	/**
	 * Shut down the ThreadPool.
	 */
	public void shutdown() {
		this.isShutDown = true;
	}
	
	/**
	 * Let TheadPool start to work.
	 * If this.isShutDown is true, then after run out of the task in taskQueue,
	 * threads will be destroy.
	 */
	private void startThreadPool() {

		for(int i = 0;i < this.threadSize;++i) {
			Thread t = new Thread(new Runnable() {
				//The run method is simple, it follow the belowing rules:
				//1 if taskQueue is not empty, ok. then get the top task to run with.
				//2 if taskQueue is empty, then check whether the ThreadPool has been 
				//  shut down. if it is shut down, then the method is end.
				//3 if taskQueue is empty and the ThreadPool hasn't been shut down,
				//  then wait  for 10ms. 
				//  whether it is signaled or time out, go to rule 1 again.
				public void run() {
					while(true) {
						lock.lock();
						if(taskQueue.isEmpty()) {
							if(isShutDown) {
								lock.unlock();
								break;
							}
							try {
								taskQueueNotEmpty.await(10, TimeUnit.MILLISECONDS);
								lock.unlock();
								continue;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} 
						Runnable task = taskQueue.remove();
						lock.unlock();
						task.run();
					}
				}
			});
			t.start();
		}
	}

}
