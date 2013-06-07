package org.b3mn.poem.handler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.b3mn.poem.Identity;
import org.b3mn.poem.util.HandlerWithoutModelContext;

import com.conx.bi.app.reporting.dao.services.IReportingDAOService;
import com.conx.bi.app.reporting.dao.services.IReportingDAOServicePortType;
import com.conx.bi.app.reporting.dao.services.ReportTemplate;

@HandlerWithoutModelContext(uri = "/reporting")
public class ReportingHandler extends HandlerBase {
	private static final boolean isTesting = false;

	private IReportingDAOServicePortType reportingDAOService;
	
	public ReportingHandler() {
//		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
//		factory.getInInterceptors().add(new LoggingInInterceptor());
//		factory.getOutInterceptors().add(new LoggingOutInterceptor());
//		factory.setServiceClass(IReportingDAOService.class);
//		factory.setAddress("http://71.162.140.197:8181/cxf/com/conx/bi/app/reporting/dao/services/IReportingDAOService?wsdl");
		try {
//			this.reportingDAOService = ((IReportingDAOService) factory.create()).getIReportingDAOServicePort();
			IReportingDAOService service = new IReportingDAOService(new URL("http://71.162.140.197:8181/cxf/com/conx/bi/app/reporting/dao/services/IReportingDAOService?wsdl"));
			this.reportingDAOService = service.getIReportingDAOServicePort();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
	}

	private List<ReportTemplate> getReportingTemplates() {
		if (!isTesting) {
			return this.reportingDAOService.getAllReportTemplates();
		} else {
			List<ReportTemplate> list = new ArrayList<ReportTemplate>();
			ReportTemplate temp = new ReportTemplate();
			temp.setId(1001L);
			temp.setName("Default Template");
			list.add(temp);
			temp = new ReportTemplate();
			temp.setId(1002L);
			temp.setName("Basic Template");
			list.add(temp);
			temp = new ReportTemplate();
			temp.setId(1003L);
			temp.setName("Advanced Template");
			list.add(temp);
			return list;
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response, Identity subject, Identity object) throws IOException {
		try {
			if ((request.getParameter("userId") != null) && (request.getParameter("call") != null)) {
				// String userId = request.getParameter("userId");
				String call = request.getParameter("call");
				if ("getReportTemplatesByTenant".equals(call)) {
					StringBuffer buffer = new StringBuffer();
					buffer.append('[');
					List<ReportTemplate> templates = getReportingTemplates();
					boolean isFirst = true;
					for (ReportTemplate template : templates) {
						if (!isFirst)
							buffer.append(',');

						buffer.append('{');

						buffer.append("\"name\":\"");
						if (template.getName() == null) {
							buffer.append("<undefined>");
						} else {
							buffer.append(template.getName());
						}
						buffer.append("\",");

						buffer.append("\"value\":");
						if (template.getId() == null) {
							buffer.append(0);
						} else {
							buffer.append(template.getId());
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
		} catch (Exception e) {
			response.setStatus(400);
			response.getWriter().println(e.toString());
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response, Identity subject, Identity object) throws IOException {
		response.setStatus(400);
		response.getWriter().println("Post not allowed");
	}
}
