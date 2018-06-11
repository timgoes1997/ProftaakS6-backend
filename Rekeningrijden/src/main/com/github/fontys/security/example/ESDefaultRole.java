/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.fontys.security.example;

import com.github.fontys.security.base.ESRole;

/**
 *
 * @author Rowan
 */
public enum ESDefaultRole implements ESRole
{
    user("user"),
    admin("admin");

    public String value;
    
    ESDefaultRole(String value) {
        this.value = value;
    }
    
    @Override
    public String getValue()
    {
        return this.value;
    }
}
