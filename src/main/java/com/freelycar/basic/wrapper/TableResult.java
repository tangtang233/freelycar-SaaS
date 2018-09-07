package com.freelycar.basic.wrapper;

import java.util.List;

public class TableResult {
    private Long total;
    private List data;

    public TableResult() {
    }

    public TableResult(List data, Long total) {
        this.total = total;
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
