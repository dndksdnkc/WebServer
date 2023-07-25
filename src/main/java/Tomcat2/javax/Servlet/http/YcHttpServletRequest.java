package Tomcat2.javax.Servlet.http;

import Tomcat2.javax.Servlet.YcServletRequest;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

public class YcHttpServletRequest implements YcServletRequest {
    private Logger logger=Logger.getLogger(YcHttpServletRequest.class);
    private Socket s;
    private InputStream iis;
    //get post put delete head trace option
    private String method;
 //定位符 http://localhost:81/108109res/doupload.action
    private String requestURL;
    //标识符 /108109res/doupload.action
    private  String requestURI;
    //上下文
    private  String contextPath;
    //请求字符串 请求的地址栏参数 age=20&sex=mam*/
    private String queryString;
    //地址栏参数 uname=a&pwd=b
    private Map<String,String[]> parameterMap=new ConcurrentHashMap<>();
    //协议类型
    private String scheme;
    //协议版本
    private String protocl;
    //项目的真实路径
    private String realPath;
    public YcHttpServletRequest(Socket s, InputStream iis){
        this.s=s;
        this.iis=iis;
        this.ParaseRequest();
    }
    //解析方法
    private void ParaseRequest(){
    String requestInfoString=readFormInputStream();//从输入流中读取http请求信息（文字）
        if (requestInfoString==null||"".equals(requestInfoString.trim())){
            throw  new RuntimeException("读取输入流异常"+requestInfoString);
        }
        //解析http请求头
        parseRequestInfoString(requestInfoString);
    }
//解析http请求头(文件信息)
    private void parseRequestInfoString(String requestInfoString) {
        StringTokenizer st=new StringTokenizer(requestInfoString);//按空格 切割
        this.method=st.nextToken();
        this.requestURI=st.nextToken();
        //requestURI要考虑地址栏参数
        int questionIndex=this.requestURI.lastIndexOf("?");
        if (questionIndex>=0){
     //有？即有地址栏参数-》参数存queryString属性
            this.queryString=this.requestURI.substring(questionIndex+1);
            this.requestURI=this.requestURI.substring(0,questionIndex);
        }
        //协议版本 HTTP/1.1
        this.protocl=st.nextToken();
        //HTTP
        this.scheme=this.protocl.substring(0,this.protocl.indexOf("/"));
        //requestURI: /1080dres/index.html
        //contextPath:/18511res  www.baidu.com-> get/
        int slash2Index=this.requestURI.indexOf("/",1);
        if (slash2Index>=0){
            this.contextPath=this.requestURI.substring(0,slash2Index);
        }else {
            this.contextPath=this.requestURI;
        }
        this.requestURL=this.scheme+"://"+this.s.getLocalSocketAddress()+this.requestURI;
        //参数处理
        if (this.queryString!=null&&this.queryString.length()>=0){
            String[]ps=this.queryString.split("&");
            for (String s:ps){
                String[]params=s.split("=");
                // 情况1：uname=a 情况二：uname=a,b,c
                this.parameterMap.put(params[0],params[1].split(","));
            }
            //还有post的实体中可能含有参数
        }
        this.realPath=System.getProperty("user.dir")+ File.separator+"webapps";
    }
//从输入流读取http请求信息
    private String readFormInputStream() {
int length=-1;
StringBuffer sb=null;
byte[]bs=new  byte[300*1024];//300k足够存除 文件上传之外的请求
        try {
  length=this.iis.read(bs,0,bs.length);
  //将byte[]-》String
            sb=new StringBuffer();
            for (int i=0;i<length;i++){
                sb.append((char) bs[i]);
            }
        }catch (Exception e){
            logger.error("读取请求信息失败");
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String[] getParameterValues(String name){
        if (parameterMap==null||parameterMap.size()<=0){
            return null;
        }
        String[] values=this.parameterMap.get(name);
        return values;
    }
    public String getParameter(String name){
        String [] values=getParameterValues(name);
        if (values==null||values.length<=0){
 return null;
        }
        return values[0];
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getQueryString() {
        return queryString;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public String getScheme() {
        return scheme;
    }

    public String getProtocl() {
        return protocl;
    }

    public String getRealPath() {
        return realPath;
    }

    public String getMethod(){
        return this.method;
    }
}
