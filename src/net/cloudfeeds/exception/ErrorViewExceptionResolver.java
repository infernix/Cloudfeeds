package net.cloudfeeds.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public class ErrorViewExceptionResolver extends SimpleMappingExceptionResolver {

	@Override
	protected Integer determineStatusCode(HttpServletRequest request,
			String viewName) {

		return Integer.parseInt(viewName);

	}
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		//logException(ex, request);
		logger.warn("Error", ex);
		return super.resolveException(request, response, handler, ex);
	}
}
