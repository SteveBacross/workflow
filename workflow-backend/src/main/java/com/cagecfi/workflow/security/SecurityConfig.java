/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.security;

import com.cagecfi.security.utils.BaseSecurityConfig;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 *
 * @author USER
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends BaseSecurityConfig{

    
}
