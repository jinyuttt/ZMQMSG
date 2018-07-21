/**    
 * 文件名：IFunction.java    
 *    
 * 版本信息：    
 * 日期：2018年7月12日    
 * Copyright 足下 Corporation 2018     
 * 版权所有    
 *    
 */
package cd.jason.thread;

/**    
 *     
 * 项目名称：msgmq    
 * 类名称：IFunction    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2018年7月12日 下午2:06:53    
 * 修改人：jinyu    
 * 修改时间：2018年7月12日 下午2:06:53    
 * 修改备注：    
 * @version     
 *     
 */
public interface IFunction<T> {
public Object executeFun(T[]param);
}
