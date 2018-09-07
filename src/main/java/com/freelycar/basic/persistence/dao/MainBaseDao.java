package com.freelycar.basic.persistence.dao;

import java.io.Serializable;

public class MainBaseDao<M extends Serializable, PK extends Serializable> extends BaseDao<M, PK> {
    public MainBaseDao() {
    }

    @Override
    public void setDbName() {
        this.dbName = "defaultdb";
    }
}
