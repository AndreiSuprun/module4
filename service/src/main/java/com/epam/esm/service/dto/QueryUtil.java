package com.epam.esm.service.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryUtil {

    private final static String SELECT_ALL_GIFT_CERTIFICATES = "SELECT * FROM gift_certificates gs";
    private final static String SQL_SELECT_BY_TAG = " JOIN gift_certificate_tags gt ON gs.id = gt.gift_certificate_id JOIN tags t ON t.id = gt.tag_id WHERE t.name = ?";
    private final static String SQL_QUERY_BY_NAME = " gs.name LIKE %?%";
    private final static String SQL_QUERY_BY_DESCRIPTION = " gs.description LIKE %?%";
    private final static String SQL_QUERY_BY_NAME_OR_DESCRIPTION_WITHOUT_TAG = " WHERE";
    private final static String SQL_QUERY_BY_NAME_OR_DESCRIPTION_WITH_TAG = " AND";
    private final static String SQL_QUERY_ORDER = " ORDER BY";
    private final static String SQL_ORDER_DESC = " DESC";
    private final static String SQL_OR_OPERATOR = " OR";
    private final static String SQL_QUERY_ORDER_BY_DATE = " gs.create_date";
    private final static String SQL_QUERY_ORDER_BY_NAME = " gs.name";
    private final static String SPLIT_SIGN = ";";
    private final static String MINUS_SIGN = "-";
    private final static String COMA_SIGN = ",";
    private final static String TAG = "tag";
    private final static String NAME = "name";
    private final static String DESCRIPTION = "description";
    private final static String ORDER = "order";
    private final static String DATE = "date";

    private List<String> params = new ArrayList<>();
    private Map<String, String> query;
    private StringBuilder SQLQuery = new StringBuilder(SELECT_ALL_GIFT_CERTIFICATES);

    public QueryUtil(Map<String, String> query) {
        this.query = query;
    }

    public String buildSQLQuery() {
        buildSelectByTag();
        buildSelectByNameOrDescription();
        buildOrderByDateOrName();
        return SQLQuery.toString();
    }

    public Object[] getQueryParams() {
        return params.toArray();
    }

    private void buildSelectByTag() {
        if (query.containsKey(TAG)) {
            SQLQuery.append(SQL_SELECT_BY_TAG);
            params.add(query.get(TAG));
        }
    }

    private void buildSelectByNameOrDescription() {
        String sql = (query.containsKey(TAG))? SQL_QUERY_BY_NAME_OR_DESCRIPTION_WITH_TAG : SQL_QUERY_BY_NAME_OR_DESCRIPTION_WITHOUT_TAG;
        if (query.containsKey(NAME) && !query.containsKey(DESCRIPTION)) {
            SQLQuery.append(sql).append(SQL_QUERY_BY_NAME);
            params.add(query.get(NAME));
        }
        if (query.containsKey(DESCRIPTION) && !query.containsKey(NAME)) {
            SQLQuery.append(sql).append(SQL_QUERY_BY_DESCRIPTION);
            params.add(query.get(DESCRIPTION));
        }
        if (query.containsKey(NAME) && query.containsKey(DESCRIPTION)) {
            SQLQuery.append(sql).append(SQL_QUERY_BY_NAME).append(SQL_OR_OPERATOR).
                    append(SQL_QUERY_BY_DESCRIPTION);
            params.add(query.get(NAME));
            params.add(query.get(DESCRIPTION));
        }
    }

    private void buildOrderByDateOrName() {
        if (query.containsKey(ORDER)) {
            String[] list = query.get(ORDER).split(SPLIT_SIGN);
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
                    SQLQuery.append(SQL_QUERY_ORDER);
                    SQLQuery.append(SQL_QUERY_ORDER_BY_NAME);
                    if (list[0].startsWith(MINUS_SIGN)) {
                        SQLQuery.append(SQL_ORDER_DESC);
                    }
                } else {
                    SQLQuery.append(SQL_QUERY_ORDER);
                    SQLQuery.append(SQL_QUERY_ORDER_BY_DATE);
                    if (list[0].startsWith(MINUS_SIGN)) {
                        SQLQuery.append(SQL_ORDER_DESC);
                    }
                }
            }
        }
    }
}
