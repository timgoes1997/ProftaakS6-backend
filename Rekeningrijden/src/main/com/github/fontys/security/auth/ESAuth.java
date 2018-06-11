/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.fontys.security.auth;

import com.github.fontys.security.base.ESUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Rowan
 */
public class ESAuth
{

    /**
     * Registers a user as logged in
     * @param session
     * @param user
     * @return
     */
    public static boolean logon(HttpSession session, ESUser user)
    {
        if (user == null)
        {
            return false;
        }

        session.setAttribute("ESUser", user);
        return true;
    }
    
    /**
     * Registers a user as logged in
     * @param req
     * @param user
     * @return
     */
    public static boolean logon(HttpServletRequest req, ESUser user)
    {
        return logon(req.getSession(), user);
    }
    
    
    /**
     * Logs the user out, if logged in
     * @param session
     * @return whether someone has been logged out
     */
    public static boolean logout(HttpSession session)
    {
        boolean hasLoggedAnyoneOut = session.getAttribute("ESUser") != null;
        session.setAttribute("ESUser", null);
        
        return hasLoggedAnyoneOut;
    }

    /**
     * Logs the user out, if logged in
     * @param req 
     * @return whether someone has been logged out
     */
    public static boolean logout(HttpServletRequest req)
    {
        return logout(req.getSession());
    }
}
