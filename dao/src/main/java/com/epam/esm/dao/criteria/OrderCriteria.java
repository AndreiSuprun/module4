package com.epam.esm.dao.criteria;

public class OrderCriteria {

    private static String UNDERSCORE_SIGN = "_";

    private String key;
    private String direction;
    private boolean isNestedProperty;

    public OrderCriteria() {
    }

    public OrderCriteria(String key, String direction) {
        this.key = key;
        this.direction = direction;
        this.isNestedProperty = key.contains(UNDERSCORE_SIGN);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isNestedProperty() {
        return isNestedProperty;
    }

    public void setNestedProperty(boolean nestedProperty) {
        isNestedProperty = nestedProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderCriteria that = (OrderCriteria) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        return direction != null ? direction.equals(that.direction) : that.direction == null;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        return result;
    }
}
