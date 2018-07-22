/**    
 * 文件名：MsgProxyBridge.java    
 *    
 * 版本信息：    
 * 日期：2018年7月16日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.jason.msgmq;

import java.nio.ByteBuffer;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import cd.jason.config.DataConfig;
import cd.jason.msg.ResetCheck;

/**    
 *     
 * 项目名称：msgmq    
 * 类名称：MsgProxyBridge    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年7月16日 上午2:20:56    
 * 修改人：jinyu    
 * 修改时间：2018年7月16日 上午2:20:56    
 * 修改备注：    
 * @version     
 *     
 */
public class MsgProxyBridge {
	private	ZContext context=null;
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
	public NetProtocol netProtocol=NetProtocol.tcp;
	
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
	
	/**
	 * 级联发布IP
	 */
	public String  cascadedIP="";
	
	/**
	 * 级联端口
	 */
	public int  cascadedPort=0;
	
	/**
	 * 级联节点IP
	 */
	public String  cascadedNodeIP="";
	
	/**
	 * 级联节点端口
	 */
	public int  cascadedNodePort=0;
	
	public int bufSize=128;
	public int MsgHWM=1000;
	private String frontaddr="";
	private String backaddr="";
	private String  cascadedaddr="";
	private String  cascadedNodeaddr="";
	public NetProtocol cascadedProtocol=NetProtocol.tcp;
	private ZMQ.Socket socket=null;//数据对象
	private ZMQ.Socket backPoint=null;//关联接口
	
	/**
	 * 本节点全数据通信
	 */
	private ZMQ.Socket PubSocket=null;//本节点级联
	private ZMQ.Socket SubSocket=null;//订阅级联
	private ZMQ.Socket SubNodeSocket=null;//订阅级联
	private  volatile boolean isClose=false;//关闭
	private ResetCheck repeat=null;//数据过滤
	public MsgProxyBridge()
	{
		//创建一个I/O线程的上下文
		 context =ZMQContext.createContext();
	}
	
	/**   
	 * @Title: address   
	 * @Description: 构建地址
	 * @return      
	 * String      
	 * @throws   
	 */
	public String address()
	{
		if(frontaddr==null||frontaddr.isEmpty())
		{
		   StringBuffer buf=new StringBuffer();
		   buf.append(netProtocol.name());
		   buf.append("://");
		   buf.append(srvIP);
		   buf.append(":");
		   buf.append(port);
		   frontaddr=buf.toString();
		}
		return frontaddr;
	}
	
	
	/**
	 * 
	 * @Title: cascadedAddr   
	 * @Description: 本节点级联地址   
	 * @return      
	 * String      
	 * @throws
	 */
	public String cascadedAddr()
	{
		if(cascadedaddr==null||cascadedaddr.isEmpty())
		{
		   StringBuffer buf=new StringBuffer();
		   buf.append(netProtocol.name());
		   buf.append("://");
		   buf.append(cascadedIP);
		   buf.append(":");
		   buf.append(cascadedPort);
		   cascadedaddr=buf.toString();
		}
		return cascadedaddr;
	}
	
	
	/**
	 * 
	 * @Title: cascadedAddr   
	 * @Description: 级联地址 全数据订阅 
	 * @return      
	 * String      
	 * @throws
	 */
	public String cascadedNodeAddr()
	{
		if(cascadedNodeaddr==null||cascadedNodeaddr.isEmpty())
		{
		   StringBuffer buf=new StringBuffer();
		   buf.append(cascadedProtocol.name());
		   buf.append("://");
		   buf.append(cascadedNodeIP);
		   buf.append(":");
		   buf.append(cascadedNodePort);
		   cascadedNodeaddr=buf.toString();
		}
		return cascadedNodeaddr;
	}
	
	/**
	 * 
	 * @Title: backAddr   
	 * @Description: 后端地址
	 * @return      
	 * String      
	 * @throws
	 */
	public String backAddr()
	{
		if(backaddr==null||backaddr.isEmpty())
		{
		   StringBuffer buf=new StringBuffer();
		   buf.append(netProtocol.name());
		   buf.append("://");
		   buf.append(backIP);
		   buf.append(":");
		   buf.append(backPort);
		   backaddr=buf.toString();
		}
		return backaddr;
	}
	

/**
 *  绑定本机地址
 * @Title: router   
 * @Description: 开启路由处理        
 * void      
 * @throws
 */
public void router()
{
	if(socket==null)
	{
        socket = context.createSocket(ZMQ.ROUTER); 
        socket.setHWM(MsgHWM);
 	    socket.setReceiveBufferSize(bufSize*1024);
 	    socket.setSendBufferSize(bufSize*1024);
        socket.bind(this.address());
	}
	if(backPoint==null)
	{
		backPoint=context.createSocket(ZMQ.DEALER);
		backPoint.setHWM(MsgHWM);
		backPoint.setReceiveBufferSize(bufSize*1024);
		backPoint.setSendBufferSize(bufSize*1024);
		backPoint.bind(this.backAddr());
	}
    ZMQ.proxy(socket, backPoint, null);  
}

/**
 * 
 * @Title: zmq_pub_sub   
 * @Description: 开启发布订阅代理       
 * void      
 * @throws
 */
public  void zmq_pub_sub()
{
	
	if(socket==null)
	{
         socket = context.createSocket(ZMQ.XSUB); 
         socket.setHWM(MsgHWM);
 	     socket.setReceiveBufferSize(bufSize*1024);
 	     socket.setSendBufferSize(bufSize*1024);
         socket.bind(this.address());
         System.out.println(address());
	}
	if(backPoint==null)
	{
		backPoint=context.createSocket(ZMQ.XPUB);
		backPoint.setHWM(MsgHWM);
		backPoint.setReceiveBufferSize(bufSize*1024);
		backPoint.setSendBufferSize(bufSize*1024);
		backPoint.bind(this.backAddr());
		System.out.println(backAddr());
	}
	if(PubSocket==null)
	{
		PubSocket = context.createSocket(ZMQ.PUB); 
		PubSocket.setHWM(MsgHWM);
		PubSocket.setReceiveBufferSize(bufSize*1024);
		PubSocket.setSendBufferSize(bufSize*1024);
		PubSocket.bind(this.cascadedAddr());
		System.out.println(cascadedAddr());
	}
    ZMQ.proxy(socket, backPoint, null);
}

/**
 * 先关闭后订阅
 * @Title: zmq_pub_sub_kz   
 * @Description: 订阅级联数据，将数据直接交给订阅地址
 * void      
 * @throws
 */
public void zmq_pub_sub_kz()
{
	if(SubNodeSocket!=null)
	{
		SubNodeSocket.close();
		SubNodeSocket=null;
	}
	SubNodeSocket = context.createSocket(ZMQ.SUB); 
	SubNodeSocket.setHWM(MsgHWM);
	SubNodeSocket.setReceiveBufferSize(bufSize*1024);
	SubNodeSocket.setSendBufferSize(bufSize*1024);
	SubNodeSocket.connect(this.cascadedNodeAddr());
	System.out.println(cascadedNodeAddr());
	SubNodeSocket.subscribe("");
	//订阅段重复的数据由数据接收端（订阅客户端）过滤，就不在这里过滤了
	ZMQ.proxy(SubNodeSocket,backPoint, null);
}


/**
 * 
 * @Title: zmq_pub_sub_self   
 * @Description: 订阅自己所有数据，并且通过级联发送出去         
 * void      
 * @throws
 */
public void zmq_pub_sub_self()
{
	if(SubSocket==null)
	{
		SubSocket = context.createSocket(ZMQ.SUB); 
		SubSocket.setHWM(MsgHWM);
		SubSocket.setReceiveBufferSize(bufSize*1024);
		SubSocket.setSendBufferSize(bufSize*1024);
		SubSocket.connect(this.backAddr());
		SubSocket.subscribe("");
	}
	while(true)
	{
		
		 byte[]data=SubSocket.recv();
		
		 if(DataConfig.isAddFlage)
		 {
			 String recData=new String(data);
			 int index=recData.indexOf(" ");//找到第一个空格
			 if(repeat==null)
			 {
				 repeat=new ResetCheck();
			 }
			 //取出最近的数据
			StringBuffer buf=new StringBuffer();
			buf.append(recData);
			buf.delete(0, index+1);
			index=buf.indexOf(" ");
			String flage=buf.substring(0,index);
			if(repeat.checkReset(flage))
			{
				//判断是重复包，抛弃
				continue;
			}
			//这里不能修改数据
		 }
		 PubSocket.send(data);
	}
	//ZMQ.proxy(SubSocket,backPoint, null);
}

/**
 * 
 * @Title: isClose   
 * @Description: 是否已经关闭  
 * @return      
 * boolean      
 * @throws
 */
public boolean isClose()
{
	return this.isClose;
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
	if(backPoint!=null)
	{
		backPoint.close();
	}
	if(PubSocket!=null)
	{
		PubSocket.close();
	}
	if(SubSocket!=null)
	{
		SubSocket.close();
	}
	if(SubNodeSocket!=null)
	{
		SubNodeSocket.close();
	}
	this.isClose=true;
}

/**
 * 只有数据地址可以重置
 * @Title: reset   
 * @Description: 重置数据地址       
 * void      
 * @throws
 */
public void reset()
{
	if(socket!=null)
	{
		socket.close();
	}
	socket=null;
}
}
