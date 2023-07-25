package Tomcat2;

import Tomcat2.javax.Servlet.YcServletContext;
import Tomcat2.javax.Servlet.YcWebServlet;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;


//tomcat服务器
public class TomcatServer {
   static Logger logger=Logger.getLogger(TomcatServer.class);
    public static void main(String[] args) {
        logger.debug("程序启动了");
        TomcatServer server=new TomcatServer();
        int port=server.pareseFormXml();
        logger.debug("服务器配置端口为"+port);
        server.startPort(port);
    }
    //启动服务器的方法
    private void startPort(int port){
        boolean flag=true;
String pakgename="wowotuan";
String pakgePath=pakgename.replace("\\.","/");
try {
    Enumeration<URL> files = Thread.currentThread().getContextClassLoader().getResources(pakgePath);
    while (files.hasMoreElements()) {
        URL url = files.nextElement();
        logger.info("正在扫描的包路径为:" + url.getFile());
        //查找包下的文件
        findPackageClasses(url.getFile(), pakgename);
    }
}catch (Exception e){
    e.printStackTrace();
}
        try {
            ServerSocket  ss = new ServerSocket(port);
            logger.debug("服务器启动成功，配置端口为：" + port);
            //TODD:可以读取Server.xml中关于是否开启线程的 配置项 决定是否使用线程
            while (flag) {
                try {
                    Socket s = ss.accept();
                    logger.debug("客户端：" + s.getRemoteSocketAddress() + "连接上了服务器");
                    TaskService task = new TaskService(s);
                    Thread thread = new Thread(task);
                    thread.start();
                }catch (Exception e){
                    e.printStackTrace();
                    logger.error("客户端连接失败。。。。。");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("服务器套接字创建失败。。。。。");
        }
    }
//pakgePath  com/yc
    //name com.yc
    private void findPackageClasses(String pakgePath, String pakgename) {
  if (pakgePath.startsWith("/")){
      pakgePath=pakgePath.substring(1);//把斜杠去掉
  }
  //取这个路径下的字节码文件
        File file=new File(pakgePath);
    File []classesFiles=file.listFiles(new FileFilter() {
        @Override
        public boolean accept(File pathname) {
           if (pathname.getName().endsWith(".class")||pathname.isDirectory()){
        return  true;
           }else {
           return false;
        }}
    });
        //System.out.println(classesFiles);
        if (classesFiles!=null&&classesFiles.length>0){
for (File cf:classesFiles){
    if (cf.isDirectory()) {
        findPackageClasses(cf.getAbsolutePath(), pakgename + "." + cf.getName());
    }else {
        //是字节码文件
        URLClassLoader uc=new URLClassLoader(new URL[]{});
        try {
            Class cls = uc.loadClass(pakgename + "." + cf.getName().replaceAll(".class", ""));
            //logger.info("加载了一个类:"+cls.getName());
            if (cls.isAnnotationPresent(YcWebServlet.class)){
                YcWebServlet annotation = (YcWebServlet) cls.getAnnotation(YcWebServlet.class);
                String url=annotation.value();
             logger.info("加载了一个类:"+cls.getName());
             //通过 value注解的方法 取出url地址
                YcServletContext.servletClass.put(url,cls);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
        }
    }

    private int pareseFormXml(){
        int port=8080;
     //方案一：根据字节码的路径（Target/classes/
        //TomcatServer.class.getClassLoader().getResourceAsStream();
        //方案二:
        String serverXmlpath=System.getProperty("user.dir")+ File.separator+"conf"+File.separator+"Server.xml";
//        logger.info(serverXmlpath);
        try (InputStream iis=new FileInputStream(serverXmlpath);
        ){
            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder=factory.newDocumentBuilder();
          Document doc= documentBuilder.parse(iis);
            NodeList nl=doc.getElementsByTagName("Connector");
            for (int i=0;i<nl.getLength();i++){
                Element node= (Element) nl.item(i);
                port= Integer.parseInt(node.getAttribute("port"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return port;
    }
}
