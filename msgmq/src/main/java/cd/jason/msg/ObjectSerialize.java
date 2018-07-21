/**    
 * 文件名：ObjectSerialize.java    
 *    
 * 版本信息：    
 * 日期：2018年7月9日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.jason.msg;

import java.io.IOException;

import org.msgpack.MessagePack;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;

/**    
 *     
 * 项目名称：msgmq    
 * 类名称：ObjectSerialize    
 * 类描述：    对象序列化
 * 创建人：jinyu    
 * 创建时间：2018年7月9日 下午9:13:20    
 * 修改人：jinyu    
 * 修改时间：2018年7月9日 下午9:13:20    
 * 修改备注：    
 * @version     
 *     
 */
public class ObjectSerialize<T> {
	MessagePack msgpack = new MessagePack();
	public static ParserConfig pc = new ParserConfig();  
	public static SerializeConfig sc = new SerializeConfig();
	public ObjectSerialize()
	{
		JSONObject.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	    
	}
	
	/**
	 * 
	 * @Title: serialize   
	 * @Description:序列化 
	 * @param obj
	 * @return      
	 * byte[]      
	 * @throws
	 */
 public byte[] serialize(T obj)
 {
	
	 byte[] raw = null;
	try {
		raw = msgpack.write(obj);
	} catch (IOException e) {
	
		e.printStackTrace();
	}
	return raw;
 }
 
 /**
  * 
  * @Title: deserialize   
  * @Description: 反序列化  
  * @param raw
  * @return      
  * T      
  * @throws
  */
 public  T deserialize(byte[]raw,Class<T> clazz)
 {
	
	 try {
		return msgpack.read(raw, clazz);
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	return null;
 }
 
 /**
  * 
  * @Title: serializeJson   
  * @Description: JSON序列化
  * @param obj
  * @return      
  * String      
  * @throws
  */
 public String serializeJson(T obj)
 {
	 return JSON.toJSONString(obj);
 }
 
 /**
  * 
  * @Title: deserializeJSON   
  * @Description: 反序列化JSON 
  * @param json
  * @param clazz
  * @return      
  * T      
  * @throws
  */
 public T  deserializeJSON(String json,Class<T>clazz)
 {
	T obj= JSON.parseObject(json, clazz);
	return obj;
	
 }
}
