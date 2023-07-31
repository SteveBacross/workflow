/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cagecfi.workflow.utilisateurs.feign;

import com.cagecfi.feign.FeignClientConfiguration;
import com.cagecfi.utilisateur.feign.BaseAppUserRestClient;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *
 * @author USER
 */
@FeignClient(name = "SECURITY-SERVICE",
        configuration = FeignClientConfiguration.class)
public interface AppUserRestClient extends BaseAppUserRestClient{
    
}
