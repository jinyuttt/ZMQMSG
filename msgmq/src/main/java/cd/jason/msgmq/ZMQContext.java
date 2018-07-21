/**    
 * 文件名：Context.java    
 *    
 * 版本信息：    
 * 日期：2018年7月12日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.jason.msgmq;

import org.zeromq.ZContext;

import cd.jason.cache.DataCache;

/**    
 *     
 * 项目名称：msgmq    
 * 类名称：Context    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年7月12日 下午2:53:34    
 * 修改人：jinyu    
 * 修改时间：2018年7月12日 下午2:53:34    
 * 修改备注：    
 * @version     
 *     
 */
public class ZMQContext {
	private static ZContext  context=null;
	public static void close()
	{
		context.close();
		context.destroy();
		DataCache.getInstance().stop();
	}
	public static ZContext  createContext()
	{
		if(context==null)
		{
		   context= new ZContext();
		}
		return context;
	}
}
