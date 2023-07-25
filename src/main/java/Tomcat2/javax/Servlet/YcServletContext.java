package Tomcat2.javax.Servlet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//应用程序上下文类 常量
//<String,Class>
// url 地址 servlet字节码文件
public class YcServletContext {
    public static Map<String,Class> servletClass=new ConcurrentHashMap<String,Class>();
    public static Map<String,YcServlet> servletInstance=new ConcurrentHashMap<String,YcServlet>();

}
