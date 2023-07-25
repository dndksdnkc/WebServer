package Tomcat2.javax.Servlet;

public interface YcServlet {
    public void init();
    public void destroy();
    public void service(YcServletRequest request,YcServletResponse response);
}
