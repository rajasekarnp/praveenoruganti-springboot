package com.praveen.billing.filters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.praveen.billing.model.BillingRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InputRequestFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;

		log.info("In Input Request Filter");

		if (httpServletRequest.getMethod().equalsIgnoreCase("POST")) {
			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			String line = "";
			StringBuilder sb = new StringBuilder();
			WrapperRequest wrapperRequest= new WrapperRequest((HttpServletRequest)request);
			request=wrapperRequest;
			try (BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}
			}
			BillingRequest billingRequest = gson.fromJson(sb.toString(), BillingRequest.class);
			InputRequestContext inputRequestContext = InputRequestContext.builder().appId(billingRequest.getAppId())
					.orderId(billingRequest.getOrderId()).transactionId(billingRequest.getTransactionId())
					.httpMethodWithUriPath(httpServletRequest.getMethod()+" "+httpServletRequest.getRequestURI()).build();
			InputRequestContextHolder.setInputRequestContext(inputRequestContext);

		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		InputRequestContextHolder.clear();
		Filter.super.destroy();
	}

}
