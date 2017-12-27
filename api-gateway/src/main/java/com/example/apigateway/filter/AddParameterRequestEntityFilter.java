package com.example.apigateway.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;

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
			json.addProperty("createdBy", "paperino");
					
			context.setRequest(modifyRequest(context.getRequest(), json.toString()));
			
			// check values
			logger.debug("VALUES: " + getBody(context));
			
		}
		catch (Exception ex) {
			logger.error("Error", ex);
		}
		return null;
	}
	
	private static HttpServletRequestWrapper modifyRequest(HttpServletRequest request, String body) {

	    HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request) {
	        @Override
	        public byte[] getContentData() {           	
	            byte[] data = null;
	            try {
	                data = body.getBytes("UTF-8");
	            } catch (UnsupportedEncodingException e) {
	                e.printStackTrace();
	            }
	            return data;
	        }

	        @Override
	        public int getContentLength() {
	            return body.getBytes().length;
	        }

	        @Override
	        public long getContentLengthLong() {
	            return body.getBytes().length;
	        }

	        @Override
	        public BufferedReader getReader() throws IOException {
	            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(body.getBytes("UTF-8"))));
	        }

	        @Override
	        public ServletInputStream getInputStream() throws UnsupportedEncodingException {
	            return new ServletInputStreamWrapper(body.getBytes("UTF-8"));
	        }
	    };

	    return wrapper;
	}
}
