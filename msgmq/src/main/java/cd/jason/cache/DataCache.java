/**    
 * 文件名：DataCache.java    
 *    
 * 版本信息：    
 * 日期：2018年7月20日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.jason.cache;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

import cd.jason.config.DataConfig;
import cd.jason.db.BerkeleyDB;

/**    
 *     
 * 项目名称：msgmq    
 * 类名称：DataCache    
 * 类描述：    缓存数据
 * 创建人：jinyu    
 * 创建时间：2018年7月20日 下午2:38:50    
 * 修改人：jinyu    
 * 修改时间：2018年7月20日 下午2:38:50    
 * 修改备注：    
 * @version     
 *     
 */
public class DataCache {
 public int maxSize=20000;
 private LinkedBlockingQueue<byte[]> queue=null;
 private static DataCache instance=null;
 private Thread dbStore=null;
 private Thread dbDelete=null;
 private volatile boolean isRun=false;
 private volatile boolean isStop=false;
 //是否已经落盘
 private volatile boolean isDisk=false;
 private BerkeleyDB db=null;
 //批量存储
 private HashMap<String,byte[]> cache=new HashMap<String,byte[]>(maxSize);
 // 信号量
 //private Semaphore event=new Semaphore(1);
 private DataCache()
 {
	 queue=new LinkedBlockingQueue<byte[]>();
	 db=new BerkeleyDB("data");
 }
 
 /**
  * 
  * @Title: getInstance   
  * @Description: 获取单例 
  * @return      
  * DataCache      
  * @throws
  */
 public static DataCache getInstance()
 {
	 if(instance==null)
	 {
		 instance=new DataCache();
	 }
	return instance;
 }
 
 /**
  * 
  * @Title: put   
  * @Description: 保持数据   
  * @param data      
  * void      
  * @throws
  */
 public void put(byte[]data)
 {
	 if(!DataConfig.isStore)
	 {
		 return;
	 }
	 if(!isRun)
	 {
		 startStore();
		 startDelete();
	 }
	 queue.add(data);
 }
 
 /**
  * 
  * @Title: getCache   
  * @Description: 获取重发数据
  * @return      
  * List<byte[]>      
  * @throws
  */
 public List<byte[]> getCache()
 {
	 //首先从内存中获取数据
	 List<byte[]> lst=new LinkedList<byte[]>();
	 HashMap<String,byte[]> map=new HashMap<String,byte[]>(maxSize);
	 map.putAll(cache);
	 if(this.isDisk)
	 {
		 //查询数据
		 HashMap<String,byte[]> data=db.getAll();
		 map.putAll(data);
	 }
	 //过滤数据，最传送最近时间内
	  SimpleDateFormat format= new SimpleDateFormat("yyyyMMddHHmmss");
	  Date cur=new Date(System.currentTimeMillis()-DataConfig.checkTime*1000);
	  String curDate=format.format(cur);
	  Iterator<Entry<String, byte[]>> item = map.entrySet().iterator();
	  while(item.hasNext())
	  {
		 Entry<String, byte[]> kv = item.next();
		 if(kv.getKey().compareTo(curDate)<0)
		 {
			 item.remove();
		 }
		 else
		 {
			 lst.add(kv.getValue());
		 }
	  }
	  map.clear();
	 return lst;
 }
 
 /**
  * 
  * @Title: getDisk   
  * @Description: 获取磁盘数据
  * @return      
  * List<byte[]>      
  * @throws
  */
 public List<byte[]> getDisk()
 {
	 //首先从内存中获取数据
	 List<byte[]> lst=new LinkedList<byte[]>();
	 //查询数据
	 HashMap<String,byte[]> map=db.getAll();
	 Iterator<byte[]> data = map.values().iterator();
	 while(data.hasNext())
	 {
		 lst.add(data.next());
	 }
	 map.clear();
     return lst;
 }
 
 
 /**
  * 
  * @Title: startStore   
  * @Description: 存储数据      
  * void      
  * @throws
  */
 private void startStore()
 {
	 if(isRun)
	 {
		 return;
	 }
	 isRun=true;
	 //
	 if(dbStore==null)
	 {
		 dbStore=new Thread(new Runnable() {
			  SimpleDateFormat format= new SimpleDateFormat("yyyyMMddHHmmss");
			  long count=0;
			public void run() {
				  DecimalFormat df=new DecimalFormat("000000");
				  df.setMaximumIntegerDigits(100000);
                  df.setMinimumIntegerDigits(0);
				
				while(!isStop)
				{
					byte[] data = null;
					try {
						data = queue.take();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					String curDate=format.format(new Date());
                    String flage= df.format(count++%100000);
                    String key=curDate+flage;
//                    try {
//						event.acquire();
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
                    cache.put(key, data);
                    if(cache.size()>maxSize)
                    {
                    	isDisk=true;
                    	db.pulAll(cache);
                    	cache.clear();
                    }
                   // event.release();
				}
				dbStore=null;
			}
			 
		 });
		 dbStore.setDaemon(true);
		 dbStore.setName("datastore");
		 if(!dbStore.isAlive())
		 {
		    dbStore.start();
		 }
	 }
 }

 /**
  * 
  * @Title: startDelete   
  * @Description: 删除之前的数据        
  * void      
  * @throws
  */
 private void startDelete()
 {
	
	 //
	 if(dbDelete==null)
	 {
		 dbDelete=new Thread(new Runnable() {
			  SimpleDateFormat format= new SimpleDateFormat("yyyyMMddHHmmssS");
			
			public void run() {
				while(!isStop)
				{
				    try {
						Thread.sleep(DataConfig.checkTime*1000);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
				    //
				    try
				    {
				    Date cur=new Date(System.currentTimeMillis()-DataConfig.checkTime*1000);
				    String curDate=format.format(cur);
				    db.deletePreAll(curDate);
				    //删除之后将已经准备存储的进行存储
//				    try {
//						event.acquire();
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
				    HashMap<String,byte[]> tmp=new HashMap<String,byte[]>();
				    tmp.putAll(cache);
					db.pulAll(tmp);//可以重复
                	//cache.clear();
                	//event.release();
				    }
				    catch(Exception ex)
				    {
				    	ex.printStackTrace();
				    }
				}
				dbDelete=null;
			}
			 
		 });
		 dbDelete.setDaemon(true);
		 dbDelete.setName("datadelete");
		 if(!dbDelete.isAlive())
		 {
			 dbDelete.start();
		 }
	 }
 }

 /**
  * 
  * @Title: stop   
  * @Description: 关闭存储       
  * void      
  * @throws
  */
public void stop()
{
	this.isStop=true;
	this.isRun=false;
	this.dbDelete=null;
	this.dbStore=null;
}
}
