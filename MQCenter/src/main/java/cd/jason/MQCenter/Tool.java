/**    
 * 文件名：Tool.java    
 *    
 * 版本信息：    
 * 日期：2018年7月12日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.jason.MQCenter;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**    
 *     
 * 项目名称：MQCenter    
 * 类名称：Tool    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年7月12日 下午4:45:02    
 * 修改人：jinyu    
 * 修改时间：2018年7月12日 下午4:45:02    
 * 修改备注：    
 * @version     
 *     
 */
public class Tool {
public static String getPath()
{
	return null;

}
public static String getLocalHostLANAddress()  {
    try {
        InetAddress candidateAddress = null;
        // 遍历所有的网络接口
        for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
            // 在所有的接口下再遍历IP
            for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                    if (inetAddr.isSiteLocalAddress()) {
                        // 如果是site-local地址，就是它了
                        return inetAddr.getHostAddress().toString();
                    } else if (candidateAddress == null) {
                        // site-local类型的地址未被发现，先记录候选地址
                        candidateAddress = inetAddr;
                    }
                }
            }
        }
        if (candidateAddress != null) {
            return candidateAddress.getHostAddress().toString();
        }
        // 如果没有发现 non-loopback地址.只能用最次选的方案
        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
        return jdkSuppliedAddress.getHostAddress().toString();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
}
