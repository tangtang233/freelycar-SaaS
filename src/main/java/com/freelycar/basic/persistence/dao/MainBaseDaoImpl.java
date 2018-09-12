package com.freelycar.basic.persistence.dao;

import java.io.Serializable;

/**
 * @author tangwei
 * @param <M>
 * @param <PK>
 */
public class MainBaseDaoImpl<M extends Serializable, PK extends Serializable> extends BaseDaoImpl<M, PK> {
    public MainBaseDaoImpl() {
    }

    @Override
    public void setDbName() {
        this.dbName = "defaultdb";
    }
}
