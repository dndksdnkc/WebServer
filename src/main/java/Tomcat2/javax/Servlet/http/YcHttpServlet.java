package Tomcat2.javax.Servlet.http;

import Tomcat2.javax.Servlet.YcServlet;
import Tomcat2.javax.Servlet.YcServletRequest;
import Tomcat2.javax.Servlet.YcServletResponse;

public  abstract class YcHttpServlet implements YcServlet {
    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }
    protected void doPost(YcHttpServletRequest request, YcHttpServletResponse response){

    }
    protected void doGet(YcHttpServletRequest request, YcHttpServletResponse response){

    }
    protected void doHead(YcHttpServletRequest request, YcHttpServletResponse response){

    }
    protected void doDelete(YcHttpServletRequest request, YcHttpServletResponse response){

    }
    protected void doTrace(YcHttpServletRequest request, YcHttpServletResponse response){

    }
    protected void doOption(YcHttpServletRequest request, YcHttpServletResponse response){

    }

    @Override
    public void service(YcServletRequest request, YcServletResponse response) {
        //从request中取出method
        String method=((YcHttpServletRequest)request).getMethod();
        if ("get".equalsIgnoreCase(method)){
            doGet((YcHttpServletRequest) request, (YcHttpServletResponse) response);
        }else if ("post".equalsIgnoreCase(method)){
            doPost((YcHttpServletRequest) request, (YcHttpServletResponse) response);
        }else if ("head".equalsIgnoreCase(method)){
            doHead((YcHttpServletRequest) request, (YcHttpServletResponse) response);
        }else if ("delete".equalsIgnoreCase(method)){
            doDelete((YcHttpServletRequest) request, (YcHttpServletResponse) response);
        }else if ("trace".equalsIgnoreCase(method)){
            doTrace((YcHttpServletRequest) request, (YcHttpServletResponse) response);
        }else if ("option".equalsIgnoreCase(method)){
            doOption((YcHttpServletRequest) request, (YcHttpServletResponse) response);
        }else {
            //错误响应协议
        }
    }
    public void service(YcHttpServletRequest request, YcHttpServletResponse response) {

    }
}
