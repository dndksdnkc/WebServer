package Tomcat2;

import Tomcat2.javax.Servlet.YcServletRequest;
import Tomcat2.javax.Servlet.YcServletResponse;

//资源处理接口
public interface Process {
    public void process(YcServletRequest request, YcServletResponse response);
}
