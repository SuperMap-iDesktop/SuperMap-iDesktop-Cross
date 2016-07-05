package com.supermap.desktop.http.upload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 

 import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
 
// import net.sf.json.JSONArray;
// import net.sf.json.JSONObject;
 

 import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
// import org.apache.ibatis.ognl.Evaluation;
import org.apache.log4j.Logger;
// import com.augurit.agcom.system.util.PropertiesReader;

import com.alibaba.fastjson.JSONArray;
 
 
// import com.augurit.agcom.persistence.dao.IAgSupUploadfileDao;
// import com.augurit.agcom.persistence.dao.impl.AgSupUploadfileDaoImpl;
// import com.augurit.agcom.system.bean.AgSupUploadfile;
// import com.augurit.agcom.system.bean.User;
// import com.sun.jersey.api.spring.Autowire;
// import com.sun.jersey.spi.resource.Singleton;
// 
 
 
 /**
  * rest文件上传、下载服务 使用方法，/rest/uploadservices/(方法名)/（后面这些是方法的参数）
  * 
  * @author Liuji
  */

 public class FileUploadDownRest{
     
//    private IAgSupUploadfileDao agSupUploadfileDao=new AgSupUploadfileDaoImpl();
 
    private static final Logger log = Logger.getLogger(FileUploadDownRest.class);
    
     private static final String FILE_BELONG_OTHER="other";   //其他文件
    private static final String FILE_BELONG_ZHOULH="zhoulh";   //周例会文件
     /**
      * 文件上传
      * @param body
      * @return
      * @throws IOException 
      */
     @POST
     @Path("upload")
     @Consumes( { MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
     @Produces(MediaType.TEXT_PLAIN)
     //@Produces("application/json")
    public void upload( @Context HttpServletRequest request, @Context
                         HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
         String msg = "上传失败！";
        Boolean flag=false;
         Map<String, String> map= new HashMap<String, String>();
         String objectNameStr = "";
         String docbelongStr="";
         String fileName="";
         String fileFormat="";
         String saveFilePath="";
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
         if (isMultipart) {
             // 构造一个文件上传处理对象
             FileItemFactory factory = new DiskFileItemFactory();
             ServletFileUpload upload = new ServletFileUpload(factory);
             upload.setHeaderEncoding("utf-8");  // 支持中文文件名
            List list = new ArrayList<FileItem>();
             try {
                 // 解析表单中提交的所有文件内容
                 list = upload.parseRequest(request);
                 for (int i = 0; i < list.size(); i++) {
                     FileItem item = (FileItem) list.get(i);
                     if(item.isFormField()){    //普通表单值
                         map.put(item.getFieldName(), item.getString("UTF-8"));
                     }else {
                         String name = item.getName(); //获得上传的文件名（IE上是文件全路径，火狐等浏览器仅文件名）
                         fileName = name.substring(
                                 name.lastIndexOf('\\') + 1, name.length());
                         fileFormat=fileName.substring(fileName.lastIndexOf("."));   //文件扩展名
                         objectNameStr = map.get("objectName");   //实体id
                         docbelongStr = map.get("docbelong");   //文件归属
                         
                         //上传文件的保存的位置
                     //    saveFilePath =PropertiesReader.getValue("agcom.fileupload.Path")+File.separator+objectNameStr;
                         saveFilePath=pathHandle(objectNameStr, docbelongStr);
 
                       //  saveFilePath = request.getRealPath("/fileupload");  
                         //    absolutePath = projectLayerId+"/"+fileName;
                     //    parseFile(item, filePath);
                         //上传操作
                         flag=upload4Stream(fileName, saveFilePath, item.getInputStream());   //上传文件
                         if(flag){
                             msg = "上传成功！";
                         }
                     }
                 }
                 
//                  AgSupUploadfile agSupUploadfile= new AgSupUploadfile();// 保存属性信息
//                  User user = (User) request.getSession().getAttribute("user");
//                  agSupUploadfile.setUserId(String.valueOf(user.getId()));    
//                  agSupUploadfile.setCmp(user.getUserName());    //上传人
//                  agSupUploadfile.setCdt(getCurrentTime());      //上传时间 
//                  agSupUploadfile.setObjectName(map.get("objectName"));
//                  agSupUploadfile.setFileFormat(fileFormat);
//                  agSupUploadfile.setFilePath(saveFilePath);
//                  agSupUploadfile.setFileName(fileName);
//                  agSupUploadfile.setDocbelong(map.get("docbelong"));   //上传文件归属
//                  agSupUploadfile.setRemark(map.get("remark"));
//                  
//                  agSupUploadfileDao.addAgSupUploadfile(agSupUploadfile);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
         //msg="<html><body>"+msg+"<a href=\"javascript:window.history.back(-1);\">返回继续上传！</a>"+"</body></html>";
         msg="<html><body>"+msg+"<input type=\"button\" value=\"返回继续上传\" onclick=\"window.history.back(-1);\"></body></html>";
         //msg=msg+"<a href='javascript:window.history.back(-1);'>返回继续上传！</a>";
         response.getWriter().write(msg);
     //    response.getWriter().write("<script language=javascript>+eval(alert("+msg+"));</script>");
     //    return "";
     }
 
     /**
      * 上传文件解析(文件名处理、上传)
      * @param item
      * @param task
      * @return
      */
     private boolean parseFile(FileItem item, String filePath) throws Exception {
         boolean flag = false;
         try {
             // 上传文件的上传路径
             String name = item.getName();
             // 上传文件的文件名
             String fileName = name.substring(name.lastIndexOf('\\') + 1, name
                     .length());
             if (fileName == null) {
                 String[] str = fileName.split(".");
                 if (str != null && str.length == 2)
                     fileName = str[0] + (new Date()).getTime() + str[1];
             }
             flag = this
                     .upload4Stream(fileName, filePath, item.getInputStream());
         } catch (Exception e) {
             e.printStackTrace();
         }
         return flag;
     }
 
     /**
      * 上传文件具体操作
      * @param fileName 文件名
      * @param filePath 文件上传路径
      * @param inStream 文件流
      * @return 上传是否成功
      */
     private boolean upload4Stream(String fileName, String filePath,
             InputStream inStream) {
         boolean result = false;
         if ((filePath == null) || (filePath.trim().length() == 0)) {
             return result;
         }
         OutputStream outStream = null; 
         try {
             String wholeFilePath = filePath + "\\" + fileName;
             File dir = new File(filePath);
             if (!dir.exists()) {
                 dir.mkdirs();
             }
             File outputFile = new File(wholeFilePath);
             boolean isFileExist = outputFile.exists();
             boolean canUpload = true;
             if (isFileExist) {
                 canUpload = outputFile.delete();
             }
             if (canUpload) {    
                 int available = 0;
                 outStream = new BufferedOutputStream(new FileOutputStream(
                         outputFile), 48);
                 byte[] buffer = new byte[48];
                 while ((available = inStream.read(buffer)) > 0) {
                     if (available < 48)
                         outStream.write(buffer, 0, available);
                     else {
                         outStream.write(buffer, 0, 48);
                     }
                 }
                 result = true;
             }
         } catch (Exception e) {
             e.printStackTrace();
             try {
                 if (inStream != null) {
                     inStream.close();
                 }
                 if (outStream != null)
                     outStream.close();
             } catch (Exception ex) {
                 e.printStackTrace();
             }
         } finally {
             try {
                 if (inStream != null) {
                     inStream.close();
                 }
                 if (outStream != null)
                     outStream.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
         return result;
     }
 
     /**
      * 文件下载
      * @throws UnsupportedEncodingException 
      * @throws IOException 
      */
     @GET
     @Path("/download")
     @Produces(MediaType.TEXT_PLAIN)
     public String downloadFile(@Context HttpServletRequest request, @Context HttpServletResponse response, 
                                @QueryParam("fileName")String fileName, @QueryParam("objectName")String objectName,
                                @QueryParam("docbelong")String docbelong) throws UnsupportedEncodingException {
         response.setContentType("text/html; charset=UTF-8");
         response.setCharacterEncoding("UTF-8");
         Boolean isOnLine = false;   //是否在线浏览
         //得到下载文件的名字
         String fileNameStr = java.net.URLDecoder.decode(fileName, "utf-8");
         //解决中文乱码问题
         //  String filename=new String(request.getParameter("filename").getBytes("iso-8859-1"),"gbk");
         
         StringBuffer path=new StringBuffer(pathHandle(objectName, docbelong))
                          .append(File.separator).append(fileNameStr);
         File file = new File(path.toString());
         FileInputStream fis = null;
         BufferedInputStream buff = null;
         OutputStream myout = null;
         try {
             if (!file.exists()) {
                 response.sendError(4, "File not found!");
                 return "";
             }
             response.reset(); 
             if (isOnLine) { //在线打开方式
                 URL u = new URL("file:///" + path.toString());
                 response.setContentType(u.openConnection().getContentType());
                 response.setHeader("Content-Disposition", "inline; filename="
                         + new String(file.getName().getBytes("gbk"), "iso-8859-1"));
             } else { //纯下载方式
                 //设置response的编码方式
                 response.setContentType("application/x-msdownload");
                 //写明要下载的文件的大小
                 response.setContentLength((int) file.length());
                 //设置附加文件名(解决中文乱码)
                 response.setHeader("Content-Disposition",
                         "attachment;filename=" + new String(file.getName().getBytes("gbk"), "iso-8859-1"));
             }
 
             fis = new FileInputStream(file);
             buff = new BufferedInputStream(fis);
             byte[] b = new byte[1024];//相当于我们的缓存
             long k = 0;//该值用于计算当前实际下载了多少字节
             //从response对象中得到输出流,准备下载
             myout = response.getOutputStream();
             while (k < file.length()) {
                 int j = buff.read(b, 0, 1024);
                 k += j;
                 //将b中的数据写到客户端的内存
                 myout.write(b, 0, j);
             }
             //将写入到客户端的内存的数据,刷新到磁盘
             myout.flush();
         } catch (MalformedURLException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } catch (FileNotFoundException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } finally {
             try {
                 if (fis != null) {
                     fis.close();
                 }
                 if (buff != null)
                     buff.close();
                 if (myout != null)
                     myout.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
 
         return "";
     }
 
     private String getCurrentTime() {
         Date currentTime = new Date();
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String dateString = formatter.format(currentTime);
         return dateString;
     }
     
     /*文件路径处理
      * 返回路径如：C\:\\Program Files\\Apache Software Foundation\\Tomcat 6.0\\upload\\\\other
      */
     private String pathHandle(String objectName,String docbelong) {
         
         StringBuffer path = new StringBuffer();//new StringBuffer(PropertiesReader.getValue("agcom.fileupload.Path"));
         path.append(File.separator).append(objectName).append(File.separator);
 
         if ("0".equals(docbelong)) { // 其他文件
             path.append(FileUploadDownRest.FILE_BELONG_OTHER);
         } else if ("1".equals(docbelong)) { // 周例会文件
             path.append(FileUploadDownRest.FILE_BELONG_ZHOULH);
         }
         return path.toString();
     }
     
     /**
      * 获取文件列表
      * @param request
      * @param response
      * @param docbelong 文件归属
      * @param objectName
      * @throws IOException
      * @throws ServletException
      *   请求路径如：http://localhost:8089/agcom/rest/uploadservices/filelist/0?objectName=''
      */
     @GET
     @Path("/filelist/{docbelong}")
     @Produces("application/json")
     public String getFileList(@Context HttpServletRequest request, @Context HttpServletResponse response, 
                             @PathParam("docbelong")String docbelong, @QueryParam("objectName")String objectName) throws ServletException, IOException{
         response.setContentType("text/html");
         response.setCharacterEncoding("UTF-8");
         Map<String,String> map=new HashMap<String, String>();
         map.put("docbelong",docbelong);
         map.put("objectName",objectName);
         //根据objectName查询 关联的文件集合
//         IAgSupUploadfileDao agSupUploadfileDao=new AgSupUploadfileDaoImpl();
//         List<AgSupUploadfile> agSupUploadfiles=agSupUploadfileDao.selectByMoreParam(map);
//         JSONArray array=JSONArray.fromObject(agSupUploadfiles);  //json对象数组
//         String arrayStr=array.toString();    
//         return arrayStr;
         return "";
     }
 }
