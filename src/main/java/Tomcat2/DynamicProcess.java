package Tomcat2;

import Tomcat2.javax.Servlet.YcServlet;
import Tomcat2.javax.Servlet.YcServletContext;
import Tomcat2.javax.Servlet.YcServletRequest;
import Tomcat2.javax.Servlet.YcServletResponse;
import Tomcat2.javax.Servlet.http.YcHttpServletRequest;

import java.io.PrintWriter;

public class DynamicProcess implements Process{
    @Override
    public void process(YcServletRequest request, YcServletResponse response) {
        //从request中取出requestURI（hello，到ServletContext的map中去取class
        String uri = ((YcHttpServletRequest) request).getRequestURI();
        int contentPathLength= ((YcHttpServletRequest) request).getContextPath().length();
        uri= uri.substring(contentPathLength);
        //为保证单例 先看另一个map中是否有这个class的实例 如有 说明是第二次访问则直接取 在调用service(),如果没有则说明servlet是第一次调用 先利用反射
        YcServlet servlet = null;
        try {
            if (YcServletContext.servletInstance.containsKey(uri)) {
                servlet = YcServletContext.servletInstance.get(uri);
            } else {
                Class clz = YcServletContext.servletClass.get(uri);
                Object obj = clz.newInstance();//调用此servlet的构造方法
                if (obj instanceof YcServlet) {
                    servlet = (YcServlet) obj;
                    servlet.init();
                    YcServletContext.servletInstance.put(uri, servlet);
                }
            }
            //创建servlet
            //此servlet就是客户端要访问的servlet helloservlet
            servlet.service(request, response);//-》YcHttpServlet.service()->根据method调用doxxx()
            //还要考虑servlet执行失败的情况，则输出500错误 响应给客户端
        }catch (Exception e){
           String bodyEntiy=e.toString();
           String protocl=gen500(bodyEntiy);
           //以输出流返回客户端
            PrintWriter out=response.getWriter();
            out.println(protocl);
            out.println(bodyEntiy);
            out.flush();
        }
    }

    private String gen500(String bodyEntiy) {
        String protocl500="HTTP/1.1 500 Internal Server Error\r\nContent-type: text/html\r\nContent-Length:"+bodyEntiy.getBytes()+"\r\n\r\n";
        return protocl500;
    }
}
