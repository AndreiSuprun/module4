package com.epam.esm.service.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryDTO {

    private String tag;
    private String name;
    private String description;
    private String order;

    public QueryDTO() {}

    public QueryDTO(String tag, String name, String description, String order) {
        this.tag = tag;
        this.name = name;
        this.description = description;
        this.order = order;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
