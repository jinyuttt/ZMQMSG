/**    
 * 文件名：ReSetModel.java    
 *    
 * 版本信息：    
 * 日期：2018年7月20日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.jason.msg;

import java.util.HashMap;

import cd.jason.config.DataConfig;

/**    
 *     
 * 项目名称：msgmq    
 * 类名称：ReSetModel    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年7月20日 下午7:22:26    
 * 修改人：jinyu    
 * 修改时间：2018年7月20日 下午7:22:26    
 * 修改备注：    
 * @version     
 *     
 */
public class ReSetModel {
public String key;
private String[] cache=new String[(int) (DataConfig.checkTime+5)];
private int index=0;
private HashMap<String,String> mapTime=new HashMap<String,String>();
public String add(String time)
{
	if(mapTime.containsKey(time))
	{
		return null;
	}
	else
	{
		mapTime.put(time, "");
		String kk=cache[index];
		cache[index]=time;
		index++;
		return kk;
	}

}
}
