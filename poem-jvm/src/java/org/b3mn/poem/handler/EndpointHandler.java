package org.b3mn.poem.handler;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.b3mn.poem.Identity;
import org.b3mn.poem.util.HandlerWithoutModelContext;

@HandlerWithoutModelContext(uri = "/endpoint")
public class EndpointHandler extends HandlerBase {
	Properties props = null;
	final static String configPreFix = "profile.stencilset.mapping.";
	final static String defaultSS = "/stencilsets/bpmn/bpmn.json";

//	private IReportingDAOServicePortType reportingDAOService;

	@Override
	public void init() {
		// Load properties
		FileInputStream in;

		// initialize properties from backend.properties
		try {
//			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
//			factory.getInInterceptors().add(new LoggingInInterceptor());
//			factory.getOutInterceptors().add(new LoggingOutInterceptor());
//			factory.setServiceClass(IReportingDAOService.class);
//			factory.setAddress("http://71.162.140.200:8181/cxf/com/conx/bi/app/reporting/dao/services/IReportingDAOService");
//			this.reportingDAOService = ((IReportingDAOService) factory.create()).getIReportingDAOServicePort();

			in = new FileInputStream(this.getBackendRootDirectory() + "/WEB-INF/backend.properties");
			props = new Properties();
			props.load(in);
			in.close();
		} catch (Exception e) {
			props = new Properties();
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response, Identity subject, Identity object) throws IOException {
		response.setStatus(400);
		response.getWriter().println("Get not allowed");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response, Identity subject, Identity object) throws IOException {
		// Check whether the request contains at least the data and svg
		// parameters
		if ((request.getParameter("userId") != null) && (request.getParameter("call") != null)) {
			response.setStatus(201);
//			String userId = request.getParameter("userId");
			String call = request.getParameter("call");
			if ("getEndpointsByTenant".equals(call)) {
				response.setContentType("application/json");
				response.getWriter().println("[{\"value\":1,\"name\":\"MySQL\"},{\"value\":2,\"name\":\"PostgreSQL\"},{\"value\":3,\"name\":\"Oracle\"}]");
			}
		} else {
			response.setStatus(400);
			response.getWriter().println("Invalid parameters");
		}
	}
}
