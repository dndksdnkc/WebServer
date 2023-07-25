package wowotuan;

import Tomcat2.javax.Servlet.YcWebServlet;
import Tomcat2.javax.Servlet.http.YcHttpServlet;
import Tomcat2.javax.Servlet.http.YcHttpServletRequest;
import Tomcat2.javax.Servlet.http.YcHttpServletResponse;

import java.io.PrintWriter;

@YcWebServlet("/bye")
public class BybyeServlet extends YcHttpServlet {
    public BybyeServlet(){
        System.out.println("构造方法");
    }

    @Override
    public void init() {
        System.out.println("初始化方法");
    }

    @Override
    protected void doGet(YcHttpServletRequest request, YcHttpServletResponse response) {
       // System.out.println("byebye");
        String result="see you tomarroe,明天见";
        PrintWriter out=response.getWriter();
        out.print("HTTP/1.1 200 OK\r\nContent-type: text/html;charset=utf-8\r\nContent-Length:"+result.getBytes()+"\r\n\r\n");
        out.print(result);
        out.flush();
    }
}
