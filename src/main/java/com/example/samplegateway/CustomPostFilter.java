package com.example.samplegateway;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

@Component
public class CustomPostFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomPostFilter.class);

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        var context = RequestContext.getCurrentContext();
        var request = context.getRequest();
        var uri = request.getRequestURI();

        return !uri.equals("/seeya");
    }

    @SneakyThrows
    @Override
    public Object run() {
        var context = RequestContext.getCurrentContext();
        var request = context.getRequest();

        var shouldWait = request.getHeader("should_wait");
        var uri = request.getRequestURI();
        var correlationId = request.getHeader("correlation_id");

        LOGGER.info("Request '{}' [{}] entered CustomPostFilter", uri, correlationId);

        if (shouldWait.equals("YES")) {
            LOGGER.info("Request '{}' [{}] is waiting on CustomPostFilter", uri, correlationId);
            Thread.sleep(20000);
            LOGGER.info("Request '{}' [{}] is released on CustomPostFilter", uri, correlationId);
        }

        LOGGER.info("Request '{}' [{}] leaves CustomPostFilter", uri, correlationId);
        return null;
    }
}
