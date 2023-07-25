package wowotuan;

import Tomcat2.javax.Servlet.YcWebServlet;
import Tomcat2.javax.Servlet.http.YcHttpServlet;
import Tomcat2.javax.Servlet.http.YcHttpServletRequest;
import Tomcat2.javax.Servlet.http.YcHttpServletResponse;

@YcWebServlet("/hello")
public class HelloServlet extends YcHttpServlet {
    public HelloServlet(){
        System.out.println("构造方法");
    }

    @Override
    public void init() {
        System.out.println("初始化方法");
    }

    @Override
    protected void doGet(YcHttpServletRequest request, YcHttpServletResponse response) {
        System.out.println("hello word");
    }
}
