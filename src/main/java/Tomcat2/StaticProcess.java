package Tomcat2;

import Tomcat2.javax.Servlet.YcServletRequest;
import Tomcat2.javax.Servlet.YcServletResponse;
import Tomcat2.javax.Servlet.http.YcHttpServletResponse;

public class StaticProcess implements Process{
    @Override
    public void process(YcServletRequest request, YcServletResponse response) {
       // ((YcHttpServletResponse)response).send();
      response.send();
    }
}
