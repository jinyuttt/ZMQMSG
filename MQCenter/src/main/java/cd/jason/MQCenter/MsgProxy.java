/**    
 * 文件名：MsgProxy.java    
 *    
 * 版本信息：    
 * 日期：2018年7月22日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.jason.MQCenter;

import cd.jason.log.LogFactory;
import cd.jason.msgmq.MsgProxyBridge;

/**    
 *     
 * 项目名称：MQCenter    
 * 类名称：MsgProxy    
 * 类描述：    消息代理
 * 创建人：jinyu    
 * 创建时间：2018年7月22日 下午5:28:38    
 * 修改人：jinyu    
 * 修改时间：2018年7月22日 下午5:28:38    
 * 修改备注：    
 * @version     
 *     
 */
public class MsgProxy {
	private NodeServer nodeServer=null;
	private MsgProxyBridge server=null;
public void start()
{
	  server=new MsgProxyBridge();
	  nodeServer=new NodeServer(server);
    if(AppConfig.iscascaded)
    {
       //开启节点信息
       nodeServer.startThread();
    }
	Thread start=new Thread(new Runnable() {
		public void run() {
	    	server.srvIP="*";
	    	server.backIP="*";
	    	server.port=AppConfig.frontPort;
	    	server.backPort=AppConfig.endPort;
	    	//
	    	server.cascadedIP="*";
	    	server.cascadedPort=AppConfig.cascadedPort;
	    	//
	    	System.out.println("启动本节点");
	    	server.zmq_pub_sub();
	    	
		}
		
	});
	start.setDaemon(true);
	start.setName("msgProxyCenter");
    start.start();
  
  //
  Thread startslf=new Thread(new Runnable() {
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	System.out.println("启动本节点级联发布");
	    	 String logmsg="本节点全数据地址："+server.cascadedAddr();
	    	 System.out.println(logmsg);
	    	 LogFactory.getInstance().addInfo(logmsg);
	    	 server.zmq_pub_sub_self();
	    	
		}
		
	});
  startslf.setDaemon(true);
  startslf.setName("LocalAllDataPub");
   startslf.start();
  //
  //
  Thread startNode=new Thread(new Runnable() {
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        server.cascadedNodeIP=AppConfig.cascadedNodeIP;
	    	server.cascadedNodePort=AppConfig.cascadedNodePort;
	    	System.out.println("启动本节点级联订阅");
	    	 String logmsg="本节点订阅的级联地址："+server.cascadedNodeAddr();
	    	 System.out.println(logmsg);
	    	 LogFactory.getInstance().addInfo(logmsg);
	    	 server.zmq_pub_sub_kz();
	    	
		}
		
	});
  startNode.setDaemon(true);
  startNode.setName("SubCascadeData");
  startNode.start();
}

/**
 * 
 * @Title: close   
 * @Description: 关闭服务         
 * void      
 * @throws
 */
public  void close()
{
	if(nodeServer!=null)
	{
	  nodeServer.close();
	}
	if(server!=null)
	{
	 server.close();
	}
}
}