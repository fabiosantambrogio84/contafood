package com.contafood.xTemp;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TargetSettingsDay {

    private Integer day;
    private Long fullDay;
    private BigDecimal value;
    private BigDecimal revised;
    private List<TargetSettingsDayAssigned> assigned;
    private BigDecimal unassignedRevised;
}
