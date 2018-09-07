package com.freelycar.basic.persistence.service;

import com.freelycar.basic.wrapper.PageInfo;
import com.freelycar.basic.wrapper.TableResult;

import java.io.Serializable;
import java.util.List;

public interface IBaseService<M extends Serializable, PK extends Serializable> {
    M save(M model);

    void update(M model);

    M updateDynamic(M model, String... ignoreProperties) throws Exception;

    M updateWithNotNullProperties(M model) throws Exception;

    void saveOrUpdate(M model);

    M saveOrUpdateDynamic(M model, String... ignoreProperties);

    M saveOrUpdateWithNotNullProperties(M model);

    void merge(M model);

    void destroy(PK id);

    void destroyObject(M model);

    int deleteList(List<PK> ids);

    int startUpList(List<PK> ids);

    int destroyList(List<PK> ids);

    M get(PK id);

    M load(PK id);

    int countAll();

    List<M> listAll();

    List<M> listAllWithUndeleted();

    TableResult listAllByPage(PageInfo pageInfo);

    TableResult listAllUndeletedByPage(PageInfo pageInfo);

    void flush();

    void clear();

    void evictFromSecondCache(PK id);

    void evictAllFromSecondCache();

    boolean contansInSecondCache(PK id);
}

