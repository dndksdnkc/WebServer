package Comcat;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TaskService implements Runnable{
    private Logger logger=Logger.getLogger(TaskService.class);
    private Socket s;
    private InputStream iis;
    private OutputStream oos;
  private boolean flag=true;
    public  TaskService(Socket s){
        this.s=s;
        try {
            this.iis=this.s.getInputStream();
            this.oos=this.s.getOutputStream();
        }catch (Exception e){
            logger.error("socket套接字获取流异常。。");
            e.printStackTrace();
            flag=false;
        }

    }
    @Override
    public void run() {
//        do {
//            //通过socket的InpuSteream读取客户端的请求 ，解析
//        //处理请求的资源
//        //返回请求的响应
//        }while ();
        //结束（socket.close()）
  if (this.flag){
      //httpserverrequest解析出所有的请求信息（method，资源地址uri http版本 头域（referre，user-agent 参数parameter
      //存在 httpserverresuest对象中
      HttpServletRequest request=new  HttpServletRequest(this.s,this.iis);
      //响应 本地地址+资源地址uri 读取文件 拼接http响应 以流的形式回传给客户端
      HttpServletResponse response=new HttpServletResponse(request,this.oos);
      response.send();
  }
        try {
            this.iis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
