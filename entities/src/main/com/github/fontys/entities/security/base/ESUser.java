/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.fontys.entities.security.base;

import javax.enterprise.inject.Model;


/**
 *
 * @author Rowan
 */
@Model
public interface ESUser
{
    public ESRole getPrivilege(); 
}
