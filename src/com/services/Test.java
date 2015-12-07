package com.services;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/test")
public class Test extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String api_version = getServletContext().getRealPath("/");

	@Path("/version")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnVersion() {
		return "<p>Version: " + api_version + "</p>";
	}
}
