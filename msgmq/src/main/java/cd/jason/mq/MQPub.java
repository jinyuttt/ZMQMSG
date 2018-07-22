/**    
 * 文件名：MQPub.java    
 *    
 * 版本信息：    
 * 日期：2018年7月22日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.jason.mq;

import cd.jason.msg.MQClient;
import cd.jason.msg.MonitorObject;
import cd.jason.msgmq.NetProtocol;

/**    
 *     
 * 项目名称：msgmq    
 * 类名称：MQPub    
 * 类描述：    发布订阅模型发布端
 * 创建人：jinyu    
 * 创建时间：2018年7月22日 下午6:23:57    
 * 修改人：jinyu    
 * 修改时间：2018年7月22日 下午6:23:57    
 * 修改备注：    
 * @version     
 *     
 */
public class MQPub {
	   //发布基础地址
		public String srvIP="*";
		public int port=0;
		public NetProtocol netProtocol=NetProtocol.tcp;
		//发布订阅使用代理的请求服务原地址
		public String srcreqIP="127.0.0.1";
		public int  srcreqPort=51000;
		public NetProtocol srcreqProtocol=NetProtocol.tcp;
		//基本信息
		public int bufSize=128;
		public int MsgHWM=1000;
		//通信
		MQClient socket=null;
		public MQPub()
		{
			
		}
		   
		/**
		 * 
		 * @Title: setAddress   
		 * @Description: 构造地址        
		 * void      
		 * @throws
		 */
		public void setAddress()
		{
			socket.bufSize=this.bufSize;
			socket.MsgHWM=this.MsgHWM;
			socket.netProtocol=this.netProtocol;
			socket.port=this.port;
			socket.srvIP=this.srvIP;
			socket.srcreqIP=this.srcreqIP;
			socket.srcreqPort=this.srcreqPort;
			socket.srcreqProtocol=srcreqProtocol;
		}
		
		/**
		 * 
		 * @Title: create   
		 * @Description: 创建实例      
		 * void      
		 * @throws
		 */
		private void create()
		{
			if(socket==null)
			{
				
				socket= new MQClient();
				setAddress();
				socket.isMQ=true;
				MonitorObject.getInstance().addObject(socket);
			}
		}

	/**
	 * 绑定本机地址
	 * @Title: Publisher   
	 * @Description: 发布数据
	 * @param topic    主题 
	 * @param data    数据   
	 * void      
	 * @throws
	 */
	public void publish(String topic,byte[]data)
	{
		create();
		socket.publish(topic, data);
	}

	/**
	 * 
	 * @Title: publishM   
	 * @Description:发布数据  
	 * @param topic
	 * @param data      
	 * void      
	 * @throws
	 */
	public void publishM(String topic,byte[]data)
	{
		create();
		socket.publishM(topic, data);
	}
	/**
	 * 
	 * @Title: publishBytes   
	 * @Description: 直接发布处理好的数据 
	 * @param data      
	 * void      
	 * @throws
	 */
	public void publishBytes(byte[]data)
	{
		create();
		socket.publishBytes(data);
	}


	/**
	 * 
	 * @Title: isClose   
	 * @Description: 已经关闭
	 * @return      
	 * boolean      
	 * @throws
	 */
	public boolean isClose()
	{
		if(socket!=null)
		return socket.isClose();
		return false;
	}

	/**
	 * 关闭
	 * @Title: close   
	 * @Description:  关闭通信      
	 * void      
	 * @throws
	 */
	public void close()
	{
		if(socket!=null)
	        socket.close();
	}

}
