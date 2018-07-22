/**    
 * 文件名：RepGroup.java    
 *    
 * 版本信息：    
 * 日期：2018年7月22日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.jason.Srv;

import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;
import cd.jason.msg.MQServer;
import cd.jason.msg.NetAddr;
import cd.jason.msgmq.MsgClient;
import cd.jason.msgmq.NetProtocol;

/**    
 *     
 * 项目名称：msgmq    
 * 类名称：RepGroup    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年7月22日 下午11:21:19    
 * 修改人：jinyu    
 * 修改时间：2018年7月22日 下午11:21:19    
 * 修改备注：    
 * @version     
 *     
 */
public class RepGroup {
	   private String conAddress="";
       private NetProtocol conNet=NetProtocol.tcp;
       private int  conPort;
       //
       private String masterAddress="";
       private int  masterPort;
       private NetProtocol masterNet=NetProtocol.tcp;
       //
       private List<NetAddr> lstAddress=null;
       public boolean isMasterCon=true;
       public int bufSize=128;
   	   public int MsgHWM=1000;
   	   private WeakHashMap<MQRep,String> mapRep=new WeakHashMap<MQRep,String>();
    	private WeakHashMap<ObjectRep,String> mapRepObj=new WeakHashMap<ObjectRep,String>();
    public void setMaster(String ip,int port,String net)
    {
    	this.masterAddress=ip;
    	this.masterPort=port;
    	this.masterNet=NetProtocol.valueOf(net.toLowerCase().trim());
    }
    public void setCurCon(NetAddr addr)
    {
    	this.conAddress=addr.ip;
    	this.conNet=NetProtocol.valueOf(addr.net.toLowerCase().trim());
        this.conPort=addr.port;
    }
    public void setName(String name)
     {
     }
    public void setInitMasterCon()
    {
    	this.conAddress=this.masterAddress;
    	this.conNet=this.masterNet;
    	this.conPort=this.masterPort;
    	this.isMasterCon=true;
    }
    public void reSetMaster(NetAddr addr)
    {
    	this.masterAddress=addr.ip;
    	this.masterNet=NetProtocol.valueOf(addr.net.toLowerCase().trim());
    	this.masterPort=addr.port;
    }
    
     /**
      * 
      * @Title: check   
      * @Description: 验证通信状态        
      * void      
      * @throws
      */
    public void check()
    {
	  boolean isChange=false;
	 if(lstAddress!=null)
	 {
		//首先检查正在使用的地址；
		MsgClient c=new MsgClient();
		c.srvIP=this.conAddress;
		c.port=this.conPort+1;
		c.netProtocol=this.conNet;
		c.senRecTimeOut(1000);
		byte[] rec=c.sendMsg("check");
		c.close();
		if(rec==null||rec.length==0)
		{
			//异常；
			int size=lstAddress.size();
			if(this.isMasterCon)
			{
				//当前连接的是master
				for(int i=0;i<size;i++)
				{
					//遍历
					MsgClient cc=new MsgClient();
					NetAddr cur=lstAddress.get(i);
					cc.srvIP=cur.ip;
					cc.port=cur.port+1;
					cc.netProtocol=NetProtocol.valueOf(cur.net.toLowerCase().trim());
					cc.senRecTimeOut(1000);
				    rec=cc.sendMsg("check");
					cc.close();
					if(rec!=null&&rec.length>0)
					{
						//验证成功，切换
						this.conAddress=cur.ip;
						this.conPort=cur.port;
						this.conNet=NetProtocol.valueOf(cur.net.toLowerCase().trim());
						//比较是否是master;
						if(this.masterAddress.equals(this.conAddress)&&this.conPort==this.masterPort)
						{
							//切换成了master
							this.isMasterCon=true;
						}
						else
						{
							this.isMasterCon=false;
						}
						isChange=true;
					}
				}
			}
		}
		else
		{
			//查看是否是master
			if(!this.isMasterCon)
			{
				MsgClient cc=new MsgClient();
				cc.srvIP=this.masterAddress;
				cc.port=this.masterPort+1;
				cc.netProtocol=this.masterNet;
				cc.senRecTimeOut(1000);
			    rec=cc.sendMsg("check");
				cc.close();
				if(rec!=null&&rec.length>0)
				{
					//master恢复
					this.conAddress=this.masterAddress;
					this.conPort=this.masterPort;
					this.conNet=this.masterNet;
					this.isMasterCon=true;
					isChange=true;
				}
			}
		}
	}
	 //
	 if(isChange)
	 {
		 //更新了正在使用的地址，准备切换
		 update();
	 }
  }
    
    /**
     * 更新底层通信
     * @Title: update   
     * @Description: TODO(这里用一句话描述这个方法的作用)         
     * void      
     * @throws
     */
 private void update()
 {
	 mapRep.size();
	 Iterator<MQRep> key = mapRep.keySet().iterator();
	 try
	 {
	 while(key.hasNext())
	 {
		 MQRep rep= key.next();
		 rep.close();
		 rep.setGroupMQ(newServer());
		 rep.bufSize=this.bufSize;
		 rep.MsgHWM=this.MsgHWM;
		 rep.srvIP=this.conAddress;
		 rep.port=this.conPort;
		 rep.netProtocol=this.conNet;//
	 }
	 }
	 catch(Exception ex)
	 {
		 ex.printStackTrace();
	 }
	 mapRepObj.size();
	 Iterator<ObjectRep> item = mapRepObj.keySet().iterator();
	 try
	 {
	 while(key.hasNext())
	 {
		 ObjectRep rep= item.next();
		 rep.close();
		 rep.setGroupMQ(createMQRep());
		 rep.bufSize=this.bufSize;
		 rep.MsgHWM=this.MsgHWM;
		 rep.srvIP=this.conAddress;
		 rep.port=this.conPort;
		 rep.netProtocol=this.conNet;//
	 }
	 }
	 catch(Exception ex)
	 {
		 ex.printStackTrace();
	 }
 }
 
 /**
  * 
  * @Title: checkMaster   
  * @Description: 验证master         
  * void      
  * @throws
  */
 public boolean checkMaster()
 {
	 if(lstAddress==null||lstAddress.size()<2)
	 {
		 return false;
	 }
	   int size=lstAddress.size();
		if(this.isMasterCon)
		{
			//当前连接的是master
			for(int i=0;i<size;i++)
			{
				//遍历
				MsgClient cc=new MsgClient();
				NetAddr cur=lstAddress.get(i);
				cc.srvIP=cur.ip;
				cc.port=cur.port+1;
				cc.netProtocol=NetProtocol.valueOf(cur.net.toLowerCase().trim());
				cc.senRecTimeOut(1000);
			    byte[]rec=cc.sendMsg("check");
				cc.close();
				if(rec!=null&&rec.length>0)
				{
					String recData=new String(rec);
					if(recData.toLowerCase().trim().equals("master"))
					{
						//说明服务端设置了master;
						this.masterAddress=cur.ip;
						this.masterPort=cur.port;
						this.masterNet=NetProtocol.valueOf(cur.net.toLowerCase().trim());
						return true;
					}
				}
			}
		}
		return false;
 }

 
 
 
 //获得新连接  
 private  MQServer newServer()  
 {  
	   MQServer conn = new MQServer();
	   conn.srvIP=this.conAddress;
	   conn.port=this.conPort;
	   conn.netProtocol=conNet;
	   conn.bufSize=this.bufSize;
	   conn.MsgHWM=this.MsgHWM;
	   return conn;
 }  
public MQRep createMQRep()
{
	 MQRep rep=new MQRep();
	 rep.bufSize=this.bufSize;
	 rep.MsgHWM=this.MsgHWM;
	 rep.srvIP=this.conAddress;
	 rep.port=this.conPort;
	 rep.netProtocol=this.conNet;//
	 //复制完成后采用设置底层的方式；
	 rep.setGroupMQ(newServer());
	 return rep;
}

public ObjectRep createObjectRep()
{
	 ObjectRep rep=new ObjectRep();
	 rep.bufSize=this.bufSize;
	 rep.MsgHWM=this.MsgHWM;
	 rep.srvIP=this.conAddress;
	 rep.port=this.conPort;
	 rep.netProtocol=this.conNet;//
	 //复制完成后采用设置底层的方式；
	 rep.setGroupMQ(createMQRep());
	 return rep;
}
public void setAddress(List<NetAddr> lstAddress) {
	this.lstAddress=lstAddress;
	
}



}