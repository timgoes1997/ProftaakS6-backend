/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.fontys.security.annotations.interceptors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

/**
 *
 * @author Rowan
 */
@Inherited
@InterceptorBinding
@Target( {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EasySecurity {
	
    /**
     * Roles that have full access
     * @return the roles that have access
     */
    @Nonbinding
    public String[] grantedRoles() default {};
    
    /**
     * Whether a user is required. Is overridden if granted roles has any value
     * @return whether a user is required
     */
    @Nonbinding
    public boolean requiresUser() default false;
	
}