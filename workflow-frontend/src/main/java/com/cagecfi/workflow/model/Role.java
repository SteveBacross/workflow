package com.cagecfi.workflow.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Role implements Serializable {
    private Long id;
    private String rolename;
    private String rolelibelle;

}
