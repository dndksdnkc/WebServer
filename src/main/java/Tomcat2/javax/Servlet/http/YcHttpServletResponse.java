package Tomcat2.javax.Servlet.http;

import Tomcat2.javax.Servlet.YcServletResponse;

import java.io.*;

public class YcHttpServletResponse implements YcServletResponse {
    private  YcHttpServletRequest request;
    private OutputStream oos;

    public YcHttpServletResponse(YcHttpServletRequest request, OutputStream oos){
        this.request=request;
        this.oos=oos;
    }
    @Override
   public void send(){
        String uri=this.request.getRequestURI();//  /wowotuan/index.html
    String realpath=this.request.getRealPath();//服务器地址
       File file=new File(realpath,uri);
       byte[]fileContent=null;
       String responseProtocol=null;
       if (!file.exists()){
           //文件不存在 返回4xx响应
           fileContent=readFile( new File(realpath,"/404.html"));
           responseProtocol=gen404(fileContent);
       }else {
           //文件存在 返回2xx
           fileContent=readFile( new File(realpath,uri));
           responseProtocol=gen200(fileContent);
       }
       try {
           oos.write(responseProtocol.getBytes());
           oos.flush();
           oos.write(fileContent);
           oos.flush();
       }catch (Exception e){
           e.printStackTrace();
       }finally {
           if (oos!=null){
               try {
                   this.oos.close();
               } catch (IOException e) {
                  e.printStackTrace();
               }
           }
       }
    }

    @Override
    public OutputStream getoos() {
        return oos;
    }

    @Override
    public PrintWriter getWriter() {
        return new PrintWriter(this.oos);
    }

    private String gen200(byte[] fileContent) {
        String protocol200="";
        //先取出回送的资源的类型
        String uri=this.request.getRequestURI(); //uri" /wowotuan/index.html a.jpg
        //从uri后面取后缀名
        int index=uri.lastIndexOf(".");
        if (index>=0){
            index+=1;
        }
        String fileExtension=uri.substring(index);
        if ("JPG".equalsIgnoreCase(fileExtension)){
            protocol200="HTTP/1.1 200 OK\r\nContent-type: image/jpeg\r\nContent-Length:"+fileContent.length+"\r\n\r\n";
        } else if ("css".equalsIgnoreCase(fileExtension)) {
            protocol200="HTTP/1.1 200 OK\r\nContent-type: text/css\r\nContent-Length:"+fileContent.length+"\r\n\r\n";
        } else if ("js".equalsIgnoreCase(fileExtension)) {
            protocol200="HTTP/1.1 200 OK\r\nContent-type: application/javascript\r\nContent-Length:"+fileContent.length+"\r\n\r\n";
        }else if ("gif".equalsIgnoreCase(fileExtension)) {
            protocol200="HTTP/1.1 200 OK\r\nContent-type: image/gif\r\nContent-Length:"+fileContent.length+"\r\n\r\n";
        }else if ("png".equalsIgnoreCase(fileExtension)) {
            protocol200="HTTP/1.1 200 OK\r\nContent-type: image/png\r\nContent-Length:"+fileContent.length+"\r\n\r\n";
        }
        else {
            protocol200="HTTP/1.1 200 OK\r\nContent-type: text/html;charset=utf-8\r\nContent-Length:"+fileContent.length+"\r\n\r\n";
        }
        return protocol200;
    }

    private String gen404(byte[] fileContent) {
        String protocol404="HTTP/1.1 404 Not Found\r\nContent-type: text/html;charset=utf-8\r\nContent-Length:"+fileContent.length+"\r\n";
        protocol404+="Server: chx\r\n\r\n";
        return protocol404;
    }

    //读取本地文件
    private byte[] readFile(File file){
        ByteArrayOutputStream boas=new ByteArrayOutputStream();
        FileInputStream fis=null;
        try {
           fis = new FileInputStream(file);
            byte[] bs = new byte[100 * 1024];
            int length = -1;
            while ((length = fis.read(bs, 0, bs.length)) != -1) {
                boas.write(bs, 0, length);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return boas.toByteArray();
    }
}
