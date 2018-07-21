/**    
 * 文件名：MQCenterInfo.java    
 *    
 * 版本信息：    
 * 日期：2018年7月15日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.jason.msg;

import java.util.ArrayList;
import java.util.List;

import cd.jason.msgmq.NetProtocol;

/**    
 *     
 * 项目名称：msgmq    
 * 类名称：MQCenterInfo    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年7月15日 下午3:34:02    
 * 修改人：jinyu    
 * 修改时间：2018年7月15日 下午3:34:02    
 * 修改备注：    
 * @version     
 *     
 */
public class MQCenterInfo {
	   //请求服务原地址
		public String srcreqIP="127.0.0.1";
		public int  srcreqPort=51000;
		public NetProtocol srcreqProtocol;
		//
		//请求级联服务地址
		public String reqIP="127.0.0.1";
		public int reqPort=0;
		public NetProtocol reqProtocol;
		//
		public boolean isCacs=false;//已经启用级联
		
		//所有级联地址
		public List<String> lstAddrs=new ArrayList<String>();
		
		//正在使用地址
		public int index=0;
		public NetAddr aysAddr(String addr)
		{
			NetAddr netaddr=new NetAddr();
			int index=addr.indexOf(":");
			netaddr.net=addr.substring(0, index);
			String tmp=addr.substring(index+3);
			String[] caddr=tmp.split(":");
			netaddr.ip=caddr[0];
			netaddr.port=Integer.valueOf(caddr[1]);
			return netaddr;
			
		}
}
