package org.b3mn.poem.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.b3mn.poem.Identity;
import org.b3mn.poem.util.HandlerWithoutModelContext;

import com.conx.bi.app.reporting.dao.services.Endpoint;
import com.conx.bi.app.reporting.dao.services.IReportingDAOServicePortType;

@HandlerWithoutModelContext(uri = "/endpoint")
public class EndpointHandler extends HandlerBase {
	private static final boolean isTesting = true;

	private IReportingDAOServicePortType reportingDAOService;

	@Override
	public void init() {
		// JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		// factory.getInInterceptors().add(new LoggingInInterceptor());
		// factory.getOutInterceptors().add(new LoggingOutInterceptor());
		// factory.setServiceClass(IReportingDAOService.class);
		// factory.setAddress("http://71.162.140.200:8181/cxf/com/conx/bi/app/reporting/dao/services/IReportingDAOService");
		// this.reportingDAOService = ((IReportingDAOService)
		// factory.create()).getIReportingDAOServicePort();
	}

	private List<Endpoint> getEndpoints() {
		if (!isTesting) {
			return this.reportingDAOService.getAllEndpoints();
		} else {
			List<Endpoint> list = new ArrayList<Endpoint>();
			Endpoint temp = new Endpoint();
			temp.setId(101L);
			temp.setCode("endpoint-mysql");
			temp.setName("MySQL Endpoint");
			list.add(temp);
			temp = new Endpoint();
			temp.setId(102L);
			temp.setCode("endpoint-oracle");
			temp.setName("Oracle Endpoint");
			list.add(temp);
			temp = new Endpoint();
			temp.setId(103L);
			temp.setCode("endpoint-mongo");
			temp.setName("MongoDB Endpoint");
			list.add(temp);
			return list;
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response, Identity subject, Identity object) throws IOException {
		if ((request.getParameter("userId") != null) && (request.getParameter("call") != null)) {
			// String userId = request.getParameter("userId");
			String call = request.getParameter("call");
			if ("getEndpointsByTenant".equals(call)) {
				StringBuffer buffer = new StringBuffer();
				buffer.append('[');
				List<Endpoint> endpoints = getEndpoints();
				boolean isFirst = true;
				for (Endpoint endpoint : endpoints) {
					if (!isFirst)
						buffer.append(',');
					
					buffer.append('{');

					buffer.append("\"title\":\"");
					if (endpoint.getName() == null) {
						buffer.append("<undefined>");
					} else {
						buffer.append(endpoint.getName());
					}
					buffer.append("\",");

					buffer.append("\"value\":\"");
					if (endpoint.getCode() == null) {
						buffer.append(0);
					} else {
						buffer.append(endpoint.getCode());
					}
					buffer.append("\",");

					buffer.append("\"id\":");
					if (endpoint.getId() == null) {
						buffer.append(0);
					} else {
						buffer.append(endpoint.getId());
					}

					buffer.append('}');

					if (isFirst)
						isFirst = false;
				}
				buffer.append(']');
				response.setContentType("application/xml");
				response.getWriter().write(buffer.toString());
				response.setStatus(200);
			}
		} else {
			response.setStatus(400);
			response.getWriter().println("Invalid parameters");
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response, Identity subject, Identity object) throws IOException {
		response.setStatus(400);
		response.getWriter().println("Post not allowed");
	}
}
