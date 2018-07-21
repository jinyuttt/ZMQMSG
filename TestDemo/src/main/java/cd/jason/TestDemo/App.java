package cd.jason.TestDemo;

import java.text.SimpleDateFormat;
import java.util.Date;

import cd.jason.msg.MQClient;
import cd.jason.msg.MonitorObject;
import cd.jason.msgmq.MsgClient;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         MonitorObject.getInstance().ischeckUpdate=true;
    	MQClient client=new MQClient();
    	client.srvIP="127.0.0.1";
    	client.port=30000;
    	byte[] data=null;
		while(true)
			{
				String dateString = formatter.format(new Date(System.currentTimeMillis()));
				data=dateString.getBytes();
    	        client.publishM("Test", data);
    	        try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
    }
}
