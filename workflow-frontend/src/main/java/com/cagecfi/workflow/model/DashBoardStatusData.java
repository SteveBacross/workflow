package com.cagecfi.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardStatusData implements Serializable {

    private Long id;
    private Long nbrDmdNv;
    private Long percNbrDmdNv;
    private Long nbrDmdEncours;
    private Long percNbrDmdEncours;
    private Long nbrDmdValidee;
    private Long percNbrDmdValidee;
    private Long nbrDmdRefuse;
    private Long percNbrDmdRefuse;
    private Long nbrTotDmd;
}
