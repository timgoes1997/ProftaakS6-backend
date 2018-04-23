package com.github.fontys.base;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

public class CORSFilter implements ContainerResponseFilter {

    @Context
    private HttpServletRequest request;

    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();

        // "http://domain.com/path/to/url
        String r = request.getHeader("Referer");

        if (r != null) {
            r = request.getScheme() + "://" +   // "http" + "://
                    request.getServerName();       // "myhost"
        }
        else {
            r = "*";
        }
        headers.add("Access-Control-Allow-Origin", r);
        headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type");
        headers.add("Access-Control-Allow-Credentials", "true");
    }

}