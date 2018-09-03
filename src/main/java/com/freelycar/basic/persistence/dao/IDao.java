package com.freelycar.basic.persistence.dao;

import java.io.Serializable;

/**
 * @author tangwei
 * @date 2018/9/3
 */
public interface IDao<M extends Serializable, PK extends Serializable> {

    PK save(M model);

    void update(M model);

    M updateDynamic(M model, String... ignoreProperties);


}
