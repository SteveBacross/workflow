package com.cagecfi.workflow.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Token implements Serializable {

    private String accessToken;
    private String refreshToken;

}
