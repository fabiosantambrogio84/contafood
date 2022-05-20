package com.contafood.xTemp;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TargetSettingsStore {

    private String storeCode;
    private String isoCurrencyCode;
    private BigDecimal value;
    private BigDecimal revised;
    private BigDecimal unassignedRevised;
    private List<TargetSettingsDay> days;

}
