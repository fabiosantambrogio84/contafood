package com.contafood.model.beans;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse {

    private Integer draw;
    private Integer recordsTotal;
    private Integer recordsFiltered;
    private List<?> data;
}
