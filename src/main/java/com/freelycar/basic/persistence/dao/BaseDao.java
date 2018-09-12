package com.freelycar.basic.persistence.dao;

import com.freelycar.basic.wrapper.PageInfo;
import com.freelycar.basic.wrapper.TableResult;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tangwei
 * @date 2018/9/3
 */
public interface BaseDao<M extends Serializable, PK extends Serializable> {
    PK save(M model);

    void update(M model);

    M updateDynamic(M model, String... ignoreProperties);

    M updateWithNotNullProperties(M model);

    void merge(M model);

    void saveOrUpdate(M model);

    M saveOrUpdateDynamic(M model, String... ignoreProperties);

    M saveOrUpdateWithNotNullProperties(M model);

    void destroyObject(M model);

    void destroy(PK id);

    int destroyList(List<PK> idList);

    int deleteList(List<PK> idList);

    int startUpList(List<PK> idList);

    M get(PK id);

    M load(PK id);

    boolean exists(PK id);

    int countAll();

    List<M> listAll();

    List<M> listAllWithUndeleted();

    TableResult listAllByPage(PageInfo pageInfo);

    TableResult listAllUndeletedByPage(PageInfo pageInfo);

    <T> List<T> list(String hql, List paramlist);

    <T> List<T> listWithIn(String hql, Map<String, Collection<?>> map, List paramlist);

    <T> T unique(String hql, List paramlist);

    <T> T uniqueCacheAble(String hql, List paramlist, boolean... useQueryCache);

    <T> List<T> listByNative(String nativeSQL, HashMap<String, Type> scalarMap, List paramlist, boolean... useQueryCache);

    void flush();

    void clear();

    void evict(PK id);

    void evictObject(M model);

    void evictFromSecondCache(PK id);

    void evictAllFromSecondCache();

    boolean contansInSecondCache(PK id);

    void executeBatchNative(List<String> sqlList);

    /** @deprecated */
    @Deprecated
    void executeWithPreparedStatementNative(String sql, List<List<Object>> recordList);

    TableResult listByNativeByPage(String nativeSQL, Map<String, Type> scalarMap, PageInfo pageInfo, List paramlist);

    <T> T uniqueByNative(String nativeSQL, Map<String, Type> scalarMap, List paramlist);
}
