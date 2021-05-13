package com.epam.esm.dao.criteria;

public class OrderCriteria {

    private String key;
    private String direction;

    public OrderCriteria() {
    }

    public OrderCriteria(String key, String direction) {
        this.key = key;
        this.direction = direction;
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
