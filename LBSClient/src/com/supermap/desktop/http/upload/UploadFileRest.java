package com.supermap.desktop.http.upload;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;  
import org.apache.commons.fileupload.disk.DiskFileItemFactory;  
import org.apache.commons.fileupload.servlet.ServletFileUpload; 

/**
 * 
 * REST,非spring版本。
 * 
 * @author zhangdapeng
 * @version 1.0,2014-1-03 13:05:06
 * @since 1.0
 */
@Path("/file")
public class UploadFileRest {
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;

	@POST
	@Path("/upload")
	@Consumes("multipart/form-data")
	public Response upLoad() throws Exception {
		String upload_file_path = request.getSession().getServletContext()
				.getRealPath("/")
				+ "upload/";
		System.out.println(upload_file_path);
		// 设置工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置文件存储位置
		factory.setRepository(Paths.get(upload_file_path).toFile());
		// 设置大小，如果文件小于设置大小的话，放入内存中，如果大于的话则放入磁盘中,单位是byte
		factory.setSizeThreshold(1024 * 1024);

		ServletFileUpload upload = new ServletFileUpload(factory);
		// 这里就是中文文件名处理的代码，其实只有一行
		upload.setHeaderEncoding("utf-8");
		String fileName = null;
		List<FileItem> list = upload.parseRequest(request);
		for (FileItem item : list) {
			if (item.isFormField()) {
				String name = item.getFieldName();
				String value = item.getString("utf-8");
				System.out.println(name);
				System.out.println(value);
				request.setAttribute(name, value);
			} else {
				String name = item.getFieldName();
				String value = item.getName();
				System.out.println(name);
				System.out.println(value);

				fileName = Paths.get(value).getFileName().toString();
				request.setAttribute(name, fileName);
				if (!Paths.get(upload_file_path).toFile().exists()) {
					Paths.get(upload_file_path).toFile().mkdirs();
				}
				// 写文件到path目录，文件名问filename
				item.write(new File(upload_file_path, fileName));
			}
		}

		return Response
				.status(200)
				.entity("uploadFile is called, Uploaded file name : "
						+ fileName).build();
	}
}
