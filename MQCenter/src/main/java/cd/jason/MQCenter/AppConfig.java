/**    
 * 文件名：AppConfig.java    
 *    
 * 版本信息：    
 * 日期：2018年7月12日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.jason.MQCenter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;
/**    
 *     
 * 项目名称：MQCenter    
 * 类名称：AppConfig    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年7月12日 下午4:54:54    
 * 修改人：jinyu    
 * 修改时间：2018年7月12日 下午4:54:54    
 * 修改备注：    
 * @version     
 *     
 */
public class AppConfig {
public static String nodeName="A";
public static int frontPort=30000;
public static int endPort=30001;
public static boolean iscascaded=false;
public static int cascadedPort=30002;//本节点全数据发布端口
public static String  cascadedIP="*";//本节点全数据发布IP
public static int cascadedNodePort=30002;//级联节点全数据端口
public static String cascadedNodeIP="127.0.0.1";//级联节点全数据IP
public static String cascadedNodeNet="tcp";//级联节点全数据协议
public static int cascadedNodeReqPort=0;//级联节点服务端口
public static String cascadedNodeReqIP="127.0.0.1";//级联节点全数据IP
public static int reqPort=51000;//本节点服务端口
public static String reqIp="127.0.0.1";//本节点服务IP
public static String monitorAddr="127.0.0.1";//监视地址
public static int monitorPort=50000;
public static String appPath="";
public static String masterServer="slave";

/**
 * 
 * @Title: init   
 * @Description: 读取配置    
 * void      
 * @throws
 */
public static void init()
{
	try
	{
      String conf=appPath+"/config/"+"appliction.properties";
      Properties properties = new Properties();
        // 使用InPutStream流读取properties文件
        BufferedReader bufferedReader = new BufferedReader(new FileReader(conf));
        properties.load(bufferedReader);
        String fpoint= (String) properties.getOrDefault("PUBPort", "30000");
        String epoint= (String) properties.getOrDefault("SUBPort", "30001");
        String cpoint= (String) properties.getOrDefault("CascadedPort", "30002");
        String cip= (String) properties.getOrDefault("CascadedIP", "*");
        String nodeIP= (String) properties.getOrDefault("cascadedNodeIP", "127.0.0.1");
        String nodePort= (String) properties.getOrDefault("cascadedNodePort", "0");
        String nodenet= (String) properties.getOrDefault("cascadedNodeNet", "tcp");
        String webPort= (String) properties.getOrDefault("reqPort", "51000");
        String webIp= (String) properties.getOrDefault("reqIp", "127.0.0.1");
        String monitorIP= (String) properties.getOrDefault("monitorAddr", "127.0.0.1");
        String monitorP= (String) properties.getOrDefault("monitorPort", "50000");
        String name= (String) properties.getOrDefault("nodeName", "A");
        String iscascad= (String) properties.getOrDefault("iscascaded", "true");
        String cReqIP= (String) properties.getOrDefault("cascadedReqIP", "127.0.0.1");
        String cReqPort= (String) properties.getOrDefault("cascadedReqPort", "0");
        //
        frontPort=Integer.valueOf(fpoint);
        endPort=Integer.valueOf(epoint);
        cascadedPort=Integer.valueOf(cpoint);
        cascadedIP=cip;
        cascadedNodePort=Integer.valueOf(nodePort);
        cascadedNodeIP=nodeIP;
        //
        cascadedNodeNet=nodenet;
        reqPort=Integer.valueOf(webPort);
        reqIp=webIp;
        monitorAddr=monitorIP;
        monitorPort=Integer.valueOf(monitorP);
        nodeName=name;
        iscascaded=iscascad.toLowerCase().equals("true")?true:false;
        cascadedNodeReqIP=cReqIP;
        cascadedNodeReqPort=Integer.valueOf(cReqPort);
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
}

}
