package org.cmas.presentation.controller.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IosUniversalLinkFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String path = ((HttpServletRequest) servletRequest).getServletPath();
        if (path.contains("apple-app-site-association")) {
            servletResponse.setContentType("application/json"); //application/pkcs7-mime  application/json;charset=UTF-8
            servletResponse.setCharacterEncoding("UTF-8");
            ((HttpServletResponse)servletResponse).setHeader("Cache-Control", "no-cache, no-store");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
