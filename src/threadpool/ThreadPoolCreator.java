package threadpool;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * ThreadPoolCreator class wraps ThreadPool class, 
 * offering a singleton of ThreadPool.
 * The methods in ThreadPoolCreator is all static.
 * @author leo
 *
 */
public class ThreadPoolCreator {
	public static ThreadPool threadPool;
	
	/**
	 * Create ThreadPool with default size.
	 * @return The created ThreadPool.
	 */
	public static ThreadPool createThreadPool() {
		if(threadPool == null) {
			threadPool = new ThreadPool();
		}
		return threadPool;
	}
	
	/**
	 * Create ThreadPool with user-defined size.
	 * @param threadSize Size that how many thread ThreadPool will create.
	 * @return The created ThreadPool.
	 */
	public static ThreadPool createThreadPoolWithThreadSize(int threadSize) {
		if(threadPool == null) {
			threadPool = new ThreadPool(threadSize);
		}
		return threadPool;
	}
	
	/**
	 * 
	 * Get the ThreadPool.
	 *
	 * @return ThreadPool.
	 */
	public static ThreadPool getThreadPool() {
		return threadPool;
	}
	

}
