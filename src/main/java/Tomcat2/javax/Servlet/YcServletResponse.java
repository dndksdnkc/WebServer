package Tomcat2.javax.Servlet;

import java.io.OutputStream;
import java.io.PrintWriter;

public interface YcServletResponse {
    void send();
    public OutputStream getoos();
    public PrintWriter getWriter();
}
