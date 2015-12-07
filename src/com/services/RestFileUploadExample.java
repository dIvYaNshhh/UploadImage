package com.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/files")
public class RestFileUploadExample {
	static Logger logger = Logger.getLogger(RestFileUploadExample.class);
	private static final String api_version = "1.01A rev.10023";
	//private static final String SAVE_FOLDER = "/Images/";
//	@Resource
//	private WebServiceContext context;

	@Path("/version")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnVersion() {
		return "<p>Version: " + api_version + "</p>";
	}

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(@Context HttpServletRequest request,
			@FormDataParam("file") InputStream fileInputString,
			@FormDataParam("file") FormDataContentDisposition fileInputDetails) {
		String status = null;
		NumberFormat myFormat = NumberFormat.getInstance();
		myFormat.setGroupingUsed(true);
		System.out.println("Success");
		// Save the file
		
		ServletContext servletContext = (ServletContext) request.getServletContext();
		String path = servletContext.getRealPath("/Images");
		File f = new File(path);
		if(!f.exists()){
			f.mkdir();
		}
		
		String fileLocation = path+"/" + fileInputDetails.getFileName();
		String filePath = "http://45.63.64.105:8080/UploadImage/Images/"+fileInputDetails.getFileName();
		try {
			OutputStream out = new FileOutputStream(new File(fileLocation));
			byte[] buffer = new byte[1024];
			int bytes = 0;
			long file_size = 0;
			while ((bytes = fileInputString.read(buffer)) != -1) {
				out.write(buffer, 0, bytes);
				file_size += bytes;
			}
			out.flush();
			out.close();

			logger.info(String.format(
					"Inside uploadFile==> fileName: %s,  fileSize: %s",
					fileInputDetails.getFileName(), myFormat.format(file_size)));

			status = myFormat.format(file_size) + " bytes";
		} catch (IOException ex) {
			logger.error("Unable to save file: " + fileLocation);
			ex.printStackTrace();
			return Response.status(200).entity(ex.getMessage()).build();
		}
		JSONObject obj = new JSONObject();
		
		try {
			obj.put("status", 0);
			obj.put("message", "Successfully Image Uploaded");
			obj.put("path", filePath);
			obj.put("file_size", status);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(200).entity(obj).build();
	}
}
