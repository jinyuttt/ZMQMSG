/**    
 * 文件名：ResetCheck.java    
 *    
 * 版本信息：    
 * 日期：2018年7月20日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.jason.msg;

import java.util.HashMap;
import java.util.Iterator;

/**    
 *     
 * 项目名称：msgmq    
 * 类名称：ResetCheck    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年7月20日 下午7:10:31    
 * 修改人：jinyu    
 * 修改时间：2018年7月20日 下午7:10:31    
 * 修改备注：    
 * @version     
 *     
 */
public class ResetCheck {
	
	/*
	 * 所有包ID
	 */
	HashMap<String,String> map=new HashMap<String,String>();
	
	/**
	 * 按照IP+process存储
	 */
	HashMap<String,ReSetModel> mapTime=new HashMap<String,ReSetModel>();
	
	/**
	 * 
	 * @Title: checkReset   
	 * @Description: 判断重复数据包   
	 * @param flage
	 * @return      
	 * boolean      
	 * @throws
	 */
public boolean checkReset(String flage)
{
	String k=map.getOrDefault(flage, null);
	if(k==null)
	{
		map.put(flage, "");
		return false;
	}
	else
	{
		String[] sf=flage.split("|");
		if(sf.length==4)
		{
			String key=sf[0]+sf[1];
			ReSetModel model=mapTime.put(key, null);
			if(model!=null)
			{
				String last=model.add(sf[2]);
				if(last!=null)
				{
					String flageid=key+last;
					delete(flageid);
				}
			}
		}
		return true;
	}
}

/**
 * 
 * @Title: delete   
 * @param key      
 * void      
 * @throws
 */
private void delete(final String key)
{
	Thread delete=new Thread(new Runnable() {
		public void run() {
			
			Iterator<String> keys = map.keySet().iterator();
			while(keys.hasNext())
			{
				if(keys.next().startsWith(key))
				{
					keys.remove();
				}
			}
		}
		
	});
	delete.setDaemon(true);
	delete.setName("delete");
	delete.start();
	
}
}