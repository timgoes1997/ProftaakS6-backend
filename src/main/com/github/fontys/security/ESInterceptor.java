package com.github.fontys.security;

import com.github.fontys.trackingsystem.user.Role;
import com.github.fontys.trackingsystem.user.User;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;

/**
 *
 * @author Rowan
 */
@Interceptor
@EasySecurity
public class ESInterceptor
{
    @Inject
    private HttpServletRequest context;

    public ESInterceptor()
    {
    }

    @AroundInvoke
    public Object checkSecurity(InvocationContext ctx) throws Exception
    {
        // get the context parameters
        EasySecurity example = ctx.getMethod().getAnnotation(EasySecurity.class);
        Role[] roles = example.grantedRoles();
        boolean requiresUser = example.requiresUser();

        if (requiresUser || roles.length > 0)
        {
            User u = (User) context.getSession().getAttribute("user");
            boolean NOAUTH = true; // by default: no access
            if (u != null)
            {
                for (Role r : roles)
                {
                    if (u.getRole() == r)
                    {
                        NOAUTH = false; // access granted
                        break;
                    }
                }
            }
            else
            {
                NOAUTH = requiresUser;
            }
            if (NOAUTH)
            {
                throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED));
            }
        }

        /* invoke the proceed() method to call the original method */
        Object ret = ctx.proceed();

        /* perform any post method call work */
        return ret;
    }
}
