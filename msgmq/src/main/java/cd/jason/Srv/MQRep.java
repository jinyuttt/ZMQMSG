/**    
 * 文件名：MQReq.java    
 *    
 * 版本信息：    
 * 日期：2018年7月22日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.jason.Srv;

import cd.jason.msg.MQServer;
import cd.jason.msg.MonitorObject;
import cd.jason.msg.ProcessMode;
import cd.jason.msgmq.NetProtocol;

/**    
 *     
 * 项目名称：msgmq    
 * 类名称：MQReq    
 * 类描述：   请求回复模型请求端
 * 创建人：jinyu    
 * 创建时间：2018年7月22日 下午6:52:39    
 * 修改人：jinyu    
 * 修改时间：2018年7月22日 下午6:52:39    
 * 修改备注：    
 * @version     
 *     
 */
public class MQRep {
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
	public int bufSize=128;
	public int MsgHWM=1000;
	//
	private MQServer socket=null;
	
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
	 * @Title: create   
	 * @Description: 创建实例        
	 * void      
	 * @throws
	 */
	private void create()
	{
		if(socket==null)
		{
			socket=new MQServer();
			MonitorObject.getInstance().addObject(socket);
			setAddress();
		}
	}
    public void setGroupMQ(MQServer server)
    {
    	this.socket=server;
    	MonitorObject.getInstance().addObject(socket);
    }
	/**
	 * 
	 * @Title: setRsp   
	 * @Description: 设置回复数据
	 * @param data      
	 * void      
	 * @throws
	 */
	public void setRsp(byte[]data)
	{
	
		if(socket!=null)
		   socket.setRsp(data);
	}
	
	/**
	 * 
	 * @Title: start   
	 * @Description: 阻塞启用  
	 * @param mode  处理模式
	 * @param topic 订阅模式时使用的主题名称
	 * void      
	 * @throws
	 */
	public boolean start(ProcessMode mode)
	{
		if(mode==ProcessMode.SUBPUB)
		{
			return false;
		}
		if(socket!=null)
		{
			socket.start(mode);
		}
		return true;
	}
	/**
	 * 
	 * @Title: getData   
	 * @Description: 接收数据 
	 * @return      
	 * byte[]      
	 * @throws
	 */
	public byte[] getData()
	{
		this.create();
		return socket.getData();
	}
	
	/**
	 * 
	 * @Title: recvice   
	 * @Description: 接收数据  
	 * @return      
	 * byte[]      
	 * @throws
	 */
    public byte[]  recvice()
    {
    	this.create();
	    return socket.recvice();
    }
    /**
	 * 
	 * @Title: recvice   
	 * @Description: 接收数据  
	 * @return      
	 * byte[]      
	 * @throws
	 */
    public byte[]  recviceProxy()
    {
    	this.create();
	    return socket.recviceProxy();
    }

/**
 * 连接处理地址
 * @Title: pull   
 * @Description: 处理数据
 * @return      
 * byte[]      
 * @throws
 */
public byte[] pull()
{
	this.create();
	return socket.pull();
}

/**
 * 
 * @Title: isClose   
 * @Description:  关闭状态
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