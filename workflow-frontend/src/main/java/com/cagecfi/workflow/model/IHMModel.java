/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cagecfi.workflow.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author GEOFFERY MO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IHMModel implements Serializable{
    
    private String backendMessage;
    private Boolean level = Boolean.FALSE;

}
