package com.epam.esm.service.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryDTO {

    private String tag;
    private String contains;
    private String order;

    public QueryDTO() {}

    public QueryDTO(String tag, String contains, String order) {
        this.tag = tag;
        this.contains = contains;
        this.order = order;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getContains() {
        return contains;
    }

    public void setContains(String contains) {
        this.contains = contains;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
