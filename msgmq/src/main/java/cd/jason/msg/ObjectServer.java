/**    
 * 文件名：ObjectServer.java    
 *    
 * 版本信息：    
 * 日期：2018年7月10日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.jason.msg;

import cd.jason.msgmq.NetProtocol;

/**    
 *     
 * 项目名称：msgmq    
 * 类名称：ObjectServer    
 * 类描述：    接收数据对象,不封装代理
 * 创建人：jinyu    
 * 创建时间：2018年7月10日 上午9:11:11    
 * 修改人：jinyu    
 * 修改时间：2018年7月10日 上午9:11:11    
 * 修改备注：    
 * @version     
 *     
 */
public class ObjectServer<T> {
	/**
	 * 绑定地址
	 * 也是路由地址
	 */
	public String srvIP="*";
	
	/**
	 * 绑定端口
	 * 也是路由端口
	 */
	public int port=0;
	
	/**
	 * 协议
	 */
	public NetProtocol netProtocol;
	
	/**
	 * 后端分发IP
	 * 路由模式
	 */
	public String backIP="*";
	
	/**
	 * 后端端口
	 * 路由模式
	 */
	public int backPort=0;
	public int bufSize=128;
	public int MsgHWM=1000;
	MQServer socket=null;
	byte[] rspData="reviceRsp".getBytes();
	public String ObjProtocol="Byte";
	private ObjectSerialize<T> msgDeserialize=null;
	public ObjectServer()
	{
		socket=new MQServer();
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
	}
	
	/**
	 * 
	 * @Title: setRsp   
	 * @Description: 设置回复数据
	 * @param data      
	 * void      
	 * @throws
	 */
	public <V> void  setRsp(V obj)
	{
	
		if(socket!=null)
		{
			byte[]data=serialize( obj);
		    socket.setRsp(data);
		}
	}
	
	/**
	 * 
	 * @Title: serialize   
	 * @Description: 序列化  
	 * @param obj
	 * @return      
	 * byte[]      
	 * @throws
	 */
	private <V> byte[] serialize(V obj)
	{
		ObjectSerialize<V> msgSerialize=new ObjectSerialize<V>();
		if(ObjProtocol.equals("Byte"))
		{
		   return msgSerialize.serialize(obj);
		}
		else if(ObjProtocol.equals("JSON"))
		{
			return msgSerialize.serializeJson(obj).getBytes();
		}
		return null;
		
	}
	
	/**
	 * 
	 * @Title: deserialize   
	 * @Description: 反序列  
	 * @param data
	 * @param clazz
	 * @return      
	 * T      
	 * @throws
	 */
	private T deserialize(byte[] data,Class<T> clazz)
	{
		if(ObjProtocol.equals("Byte"))
		{
		return msgDeserialize.deserialize(data, clazz);
		}
		else if(ObjProtocol.equals("JSON"))
		{
			String json=String.valueOf(data);
			return msgDeserialize.deserializeJSON(json, clazz);
		}
		return null;
	}
	
	/**
	 * 
	 * @Title: start   
	 * @Description: 启动初始化，通过阻塞获取数据
	 * @param mode      
	 * void      
	 * @throws
	 */
	public void start(ProcessMode mode)
	{
		socket.start(mode);
	}
	/**
	 * 
	 * @Title: getData   
	 * @Description: 获取阻塞数据   
	 * @param clazz
	 * @return      
	 * T      
	 * @throws
	 */
	public T getData(Class<T> clazz)
	{
		byte[] data=socket.getData();
		T obj=this.deserialize(data, clazz);
		return obj;
	}
	
	/**
	 * 
	 * @Title: recvice   
	 * @Description: 接收数据  
	 * @return      
	 * byte[]      
	 * @throws
	 */
    public T  recvice(Class<T> clazz)
    {
    	byte[] data=socket.recvice();
		T obj=this.deserialize(data, clazz);
		return obj;
    }


/**
 * 
 * @Title: Subscriber   
 * @Description: 订阅数据  
 * @param topic
 * @return      
 * byte[]      
 * @throws
 */
public void subscriber(String topic)
{
	socket.subscriber(topic);
	
}

/**
 * 
 * @Title: cancleSubscriber   
 * @Description: 取消订阅，如果为null则全部取消  
 * @param topic      
 * void      
 * @throws
 */
public void cancleSubscriber(String topic)
{
	socket.cancleSubscriber(topic);
}
/**
 * 
 * @Title: subscriberData   
 * @Description: 获取对象  
 * @param clazz
 * @return      
 * T      
 * @throws
 */
public T subscriberData(Class<T> clazz)
{
	byte[] data=socket.subscriberData();
	if(data==null)
	{
		return null;
	}
	T obj=this.deserialize(data, clazz);
	return obj;
}
/**
 * 连接处理地址
 * @Title: pull   
 * @Description: 处理数据
 * @return      
 * byte[]      
 * @throws
 */
public T pull(Class<T> clazz)
{
	byte[] data=socket.pull();
	T obj=this.deserialize(data, clazz);
	return obj;
}

/**
 * 
 * @Title: close   
 * @Description: 关闭      
 * void      
 * @throws
 */
public void close()
{
	if(socket!=null)
	{
		socket.close();
	}
}
}
