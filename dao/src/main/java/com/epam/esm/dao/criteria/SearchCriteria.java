package com.epam.esm.dao.criteria;

public class SearchCriteria {

    private static String UNDERSCORE_SIGN = "_";

    private String key;
    private SearchOperation operation;
    private Object value;
    private boolean orPredicate;
    private boolean isNestedProperty;

    public SearchCriteria() {
    }

    public SearchCriteria(String key, SearchOperation operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.isNestedProperty = key.contains(UNDERSCORE_SIGN);
    }

    public SearchCriteria(String orPredicate, String key, SearchOperation operation, Object value) {
        this.orPredicate = orPredicate != null && orPredicate.equals(SearchOperation.OR_PREDICATE_FLAG);
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.isNestedProperty = key.contains(UNDERSCORE_SIGN);
    }

    public SearchCriteria(String key, String searchOperation, String prefix, String value, String suffix) {
        SearchOperation operation = SearchOperation.getSimpleOperation(searchOperation.charAt(0));
        if (operation != null) {
            if (operation == SearchOperation.EQUALITY) {
                final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    operation = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    operation = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    operation = SearchOperation.STARTS_WITH;
                }
            }
        }
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.isNestedProperty = key.contains(UNDERSCORE_SIGN);
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public SearchOperation getOperation() {
        return operation;
    }

    public void setOperation(final SearchOperation operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(final Object value) {
        this.value = value;
    }

    public boolean isOrPredicate() {
        return orPredicate;
    }

    public void setOrPredicate(boolean orPredicate) {
        this.orPredicate = orPredicate;
    }

    public boolean isNestedProperty() {
        return isNestedProperty;
    }

    public void setNestedProperty(boolean nestedProperty) {
        isNestedProperty = nestedProperty;
    }
}
