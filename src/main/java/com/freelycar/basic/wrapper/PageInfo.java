package com.freelycar.basic.wrapper;

/**
 * @author tangwei
 * @date 2018/9/3
 */
public class PageInfo {
    int current = -1;
    int pageSize = -1;
    private String sort;
    private String order;

    public PageInfo() {
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
