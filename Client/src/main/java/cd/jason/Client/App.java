package cd.jason.Client;

import cd.jason.msg.MonitorObject;
import cd.jason.msgmq.MsgServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	MonitorObject.getInstance().ischeckUpdate=true;
    	//MQServer server=new MQServer();
    	MsgServer server=new MsgServer();
    	
    	server.subscriber("Test");
    	while(true)
    	{
    		//System.out.println("执行");
    	  byte[]data=server.subscriberData();
    		//server
    	  if(data!=null)
    	  System.out.println(new String(data));
    		
    	}
    }
}
