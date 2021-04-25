package com.epam.esm.entity;

import java.util.ArrayList;
import java.util.List;

public class Query {

    private final static String SELECT_ALL_GIFT_CERTIFICATES = "SELECT * FROM gift_certificates gs";
    private final static String SQL_SELECT_BY_TAG = " JOIN gift_certificate_tags gt ON gs.id = gt.gift_certificate_id JOIN tags t ON t.id = gt.tag_id WHERE t.name = ?";
    private final static String SQL_QUERY_CONTAIN = " gs.name LIKE concat ('%', ?, '%') OR gs.description LIKE concat ('%', ?, '%')";//" MATCH (gs.name, gs.description) AGAINST (?)";
    private final static String SQL_QUERY_CONTAINS_WITHOUT_TAG = " WHERE";
    private final static String SQL_QUERY_CONTAINS_WITH_TAG = " AND";
    private final static String SQL_QUERY_ORDER = " ORDER BY";
    private final static String SQL_ORDER_DESC = " DESC";
    private final static String SQL_QUERY_ORDER_BY_DATE = " gs.create_date";
    private final static String SQL_QUERY_ORDER_BY_NAME = " gs.name";
    private final static String SPLIT_SIGN = ";";
    private final static String MINUS_SIGN = "-";
    private final static String COMA_SIGN = ",";
    private final static String NAME = "name";
    private final static String DATE = "date";

    private String tag;
    private String contains;
    private String order;

    private List<String> params = new ArrayList<>();
    private StringBuilder SQLQuery = new StringBuilder(SELECT_ALL_GIFT_CERTIFICATES);

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

    public String buildSQLQuery() {
        buildSelectByTag();
        buildSelectContains();
        buildOrderByDateOrName();
        return SQLQuery.toString();
    }

    public Object[] getQueryParams() {
        return params.toArray();
    }

    private void buildSelectByTag() {
        if (tag != null) {
            SQLQuery.append(SQL_SELECT_BY_TAG);
            params.add(tag);
        }
    }

    private void buildSelectContains() {
        String sql = (tag != null) ? SQL_QUERY_CONTAINS_WITH_TAG : SQL_QUERY_CONTAINS_WITHOUT_TAG;
        if (contains != null) {
            SQLQuery.append(sql).append(SQL_QUERY_CONTAIN);
            params.add(contains);
            params.add(contains);
        }
    }

    private void buildOrderByDateOrName() {
        if (order != null) {
            String[] list = order.split(SPLIT_SIGN);
            if (list[0].contains(DATE)) {
                SQLQuery.append(SQL_QUERY_ORDER);
                SQLQuery.append(SQL_QUERY_ORDER_BY_DATE);
                if (list[0].startsWith(MINUS_SIGN)) {
                    SQLQuery.append(SQL_ORDER_DESC);
                }
            } else {
                SQLQuery.append(SQL_QUERY_ORDER);
                SQLQuery.append(SQL_QUERY_ORDER_BY_NAME);
                if (list[0].startsWith(MINUS_SIGN)) {
                    SQLQuery.append(SQL_ORDER_DESC);
                }
            }
            if (list.length > 1) {
                SQLQuery.append(COMA_SIGN);
                if (list[1].contains(NAME)) {
                    SQLQuery.append(SQL_QUERY_ORDER_BY_NAME);
                    if (list[1].startsWith(MINUS_SIGN)) {
                        SQLQuery.append(SQL_ORDER_DESC);
                    }
                } else {
                    SQLQuery.append(SQL_QUERY_ORDER_BY_DATE);
                    if (list[1].startsWith(MINUS_SIGN)) {
                        SQLQuery.append(SQL_ORDER_DESC);
                    }
                }
            }
        }
    }
}
