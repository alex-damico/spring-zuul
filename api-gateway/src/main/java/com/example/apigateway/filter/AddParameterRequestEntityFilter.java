package com.example.apigateway.filter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class AddParameterRequestEntityFilter extends ZuulFilter {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String filterType() {
		return "pre";
	}

	public int filterOrder() {
		return 7;
	}

	public boolean shouldFilter() {
		RequestContext context = RequestContext.getCurrentContext();
		return context.get("proxy").equals("service-two");
	}
	
	private String getBody(final RequestContext context) throws Exception {
		InputStream in = (InputStream) context.get("requestEntity");
		if (in == null) {
			in = context.getRequest().getInputStream();
		}
		return StreamUtils.copyToString(in, Charset.forName("UTF-8"));
	}

	public Object run() {
		try {			
			RequestContext context = RequestContext.getCurrentContext();	
			if (context.getRequest().getMethod().equals(HttpMethod.OPTIONS.toString())) {
				return null;
			}
			
			String body = getBody(context);
			
			JsonParser parser = new JsonParser();
			JsonObject json = parser.parse(body).getAsJsonObject();
			json.addProperty("createdBy", "created");
			
			context.set("requestEntity", new ByteArrayInputStream(json.toString().getBytes("UTF-8")));
			
			// check values
			logger.debug("VALUES: " + getBody(context));
			
		}
		catch (Exception ex) {
			logger.error("Error", ex);
		}
		return null;
	}
}
