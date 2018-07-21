/**    
 * 文件名：ThreadPool.java    
 *    
 * 版本信息：    
 * 日期：2018年7月12日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.jason.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**    
 *     
 * 项目名称：msgmq    
 * 类名称：ThreadPool    
 * 类描述：  线程池使用  
 * 创建人：jinyu    
 * 创建时间：2018年7月12日 下午1:08:33    
 * 修改人：jinyu    
 * 修改时间：2018年7月12日 下午1:08:33    
 * 修改备注：    
 * @version     
 *     
 */
public class ThreadPool {
	ExecutorService executorCache =null;
	ExecutorService executorFix = null;
	private  static ThreadPool instance=null;
	private ThreadPool()
	{
		executorCache = Executors.newCachedThreadPool();
		int num=(int) (Runtime.getRuntime().availableProcessors()*1.5);
		executorFix=Executors.newFixedThreadPool(num);
	}
	
	/**
	 * 
	 * @Title: getInstance   
	 * @Description: 单例
	 * @return      
	 * ThreadPool      
	 * @throws
	 */
	public static ThreadPool getInstance()
	{
		if(instance==null)
		{
			instance=new ThreadPool();
		}
		return instance;
	}
	
	/**
	 * 
	 * @Title: executeThread   
	 * @Description: 放入线程中   
	 * @param command      
	 * void      
	 * @throws
	 */
	public void  executeThread(Runnable command)
	{
		executorCache.execute(command);;
	}
	
	/**
	 * 
	 * @Title: executeFix   
	 * @Description: 定线程执行
	 * @param command      
	 * void      
	 * @throws
	 */
	public void  executeFix(Runnable command)
	{
		executorFix.execute(command);;
	}
	
	/**
	 * 
	 * @Title: executeTimeOut   
	 * @Description: 超时执行
	 * @param command
	 * @param param
	 * @param timeOut
	 * @return      
	 * Object      
	 * @throws
	 */
	public Object executeTimeOut(final IFunction<Object> command,final Object[]param, long timeOut)
	{
		Callable<Object> task=new Callable<Object>() {

			public Object call() throws Exception {
				
				return command.executeFun(param);
			}
			
		};
		Future<Object> future= executorCache.submit(task);
		try {
		return	future.get(timeOut, TimeUnit.MILLISECONDS);
		}  catch (TimeoutException e) {
		
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
