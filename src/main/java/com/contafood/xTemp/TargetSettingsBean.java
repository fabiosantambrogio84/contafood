package com.contafood.xTemp;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.List;

@Data
public class TargetSettingsBean {

    private String type;
    private Integer version;
    private Long fullDate;
    private Integer month;
    private Integer year;
    private Long sendDate;
    private List<TargetSettingsStore> stores;

    public static void main(String[] args) throws Exception{
        String s = "2022-01-01T00:00:00.000Z";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss.SSSX");
        System.out.println(sdf.parse(s).getTime());
    }
}
