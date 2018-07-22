package cd.jason.MQCenter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import cd.jason.msgmq.ZMQContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	String realPath = initPath();
    	AppConfig.appPath=realPath;
    	AppConfig.init();
    	ServerProxy proxyCenter=new ServerProxy();
    	proxyCenter.start();
        //
    	System.out.println("启动");
    	try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
    
    	ZMQContext.close();
    }
    
    /**
     * 
     * @Title: initPath   
     * @Description:获取路径 
     * @return      
     * String      
     * @throws
     */
    private static String initPath()
    {
    	URL url = App.class.getProtectionDomain().getCodeSource().getLocation();
		String filePath = null;
		try {
			filePath = URLDecoder.decode(url.getPath(), "utf-8");// 转化为utf-8编码
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (filePath.endsWith(".jar")) {// 可执行jar包运行的结果里包含".jar"
			// 截取路径中的jar包名
			filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
		}
		
		File file = new File(filePath);
		
		 filePath = file.getAbsolutePath();
		return filePath;

    }
}
