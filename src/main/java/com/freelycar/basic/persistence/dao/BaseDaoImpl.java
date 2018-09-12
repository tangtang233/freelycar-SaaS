package com.freelycar.basic.persistence.dao;

import com.freelycar.basic.persistence.exceptions.DAOException;
import com.freelycar.basic.persistence.model.CurrentDataBase;
import com.freelycar.basic.persistence.util.SessionFactoryManage;
import com.freelycar.basic.util.BeanUtilsExtend;
import com.freelycar.basic.util.PageUtil;
import com.freelycar.basic.wrapper.PageInfo;
import com.freelycar.basic.wrapper.TableResult;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * @author tangwei
 * @param <M>
 * @param <PK>
 */
public abstract class BaseDaoImpl<M extends Serializable, PK extends Serializable> implements InitializingBean, BaseDao<M, PK> {
    @Autowired
    private SessionFactoryManage sessionFactoryManage;
    protected Map<String, SessionFactory> sessionFactoryMap;
    private SessionFactory sessionFactory;
    protected String dbName = "defaultdb";
    private final Class<M> entityClass = (Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    private String HQL_LIST_ALL;
    private String HQL_LIST_ALL_UNDELETED;
    private String HQL_COUNT_ALL;
    private String HQL_DELETE_ALL;
    private String HQL_DELETE_ENTITY_ALL;
    private String HQL_STARTUP_ALL;
    private String pkName = null;
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     *
     */
    public abstract void setDbName();

    public BaseDaoImpl() {
    }

    @Override
    public void afterPropertiesSet() {
        this.setDbName();
        this.sessionFactoryMap = this.sessionFactoryManage.getSessionFactoryMap();
        this.sessionFactory = (SessionFactory)this.sessionFactoryMap.get(this.dbName);
        ClassMetadata md = this.sessionFactory.getClassMetadata(this.entityClass);
        this.pkName = md.getIdentifierPropertyName();
        this.HQL_LIST_ALL = "from " + this.entityClass.getSimpleName() + " order by " + this.pkName + " desc";
        this.HQL_LIST_ALL_UNDELETED = "from " + this.entityClass.getSimpleName() + " where delStatus=" + 0 + " order by " + this.pkName + " desc";
        this.HQL_COUNT_ALL = " select count(*) from " + this.entityClass.getSimpleName();
        this.HQL_DELETE_ALL = " update " + this.entityClass.getSimpleName() + "  set delStatus=" + 1 + " where " + this.pkName + "  in :ids";
        this.HQL_DELETE_ENTITY_ALL = " delete from  " + this.entityClass.getSimpleName() + "  where " + this.pkName + " in :ids";
        this.HQL_STARTUP_ALL = " update " + this.entityClass.getSimpleName() + " set delStatus=" + 0 + " where " + this.pkName + "  in :ids";
    }

    public void setSessionFactoryMap(Map<String, SessionFactory> sessionFactoryMap) {
        this.sessionFactoryMap = sessionFactoryMap;
    }

    public String processSqlServerFunction(String sql) {
        if (CurrentDataBase.SQLSERVER_DIALECT.equals(CurrentDataBase.getCurrentDialect()) && sql.indexOf("dbo.") == -1 && sql.indexOf("DBO.") == -1) {
            sql = sql.replaceAll("concat", "dbo.concat").replaceAll("CONCAT", "DBO.CONCAT").replaceAll("to_date", "dbo.to_date").replaceAll("TO_DATE", "DBO.TO_DATE").replaceAll("to_char", "dbo.to_char").replaceAll("TO_CHAR", "DBO.TO_CHAR").replaceAll("to_number", "dbo.to_number").replaceAll("TO_NUMBER", "DBO.TO_NUMBER");
        }

        return sql;
    }

    private Session getSession() {
        return ((SessionFactory)this.sessionFactoryMap.get(this.dbName)).getCurrentSession();
    }

    @Override
    public PK save(M model) {
        return (PK) this.getSession().save(model);
    }

    @Override
    public void update(M model) {
        this.getSession().update(model);
    }

    @Override
    public M updateDynamic(M model, String... ignoreProperties) {
        Field field = null;
        PK pkid = null;

        try {
            field = this.entityClass.getDeclaredField(this.pkName);
            field.setAccessible(true);
            pkid = (PK)field.get(model);
        } catch (NoSuchFieldException var6) {
            var6.printStackTrace();
            throw new DAOException(this.entityClass.getSimpleName() + "类没有" + this.pkName + "属性", var6);
        } catch (IllegalAccessException var7) {
            var7.printStackTrace();
            throw new DAOException("非法访问" + this.entityClass.getSimpleName() + "实例的" + this.pkName + "属性", var7);
        }

        M dataModel = this.get(pkid);
        if (dataModel == null) {
            throw new DAOException("更新的数据不存在,实体名：" + this.entityClass.getSimpleName() + ",主键：" + pkid);
        } else {
            BeanUtils.copyProperties(model, dataModel, ignoreProperties);
            this.update(dataModel);
            return dataModel;
        }
    }

    @Override
    public M updateWithNotNullProperties(M model) {
        Field field = null;
        PK pkid = null;

        try {
            field = this.entityClass.getDeclaredField(this.pkName);
            field.setAccessible(true);
            pkid = (PK)field.get(model);
        } catch (NoSuchFieldException var5) {
            throw new DAOException(this.pkName + "属性未定义！", var5);
        } catch (IllegalAccessException var6) {
            throw new DAOException("非法访问了" + this.pkName + "属性！", var6);
        }

        M dataModel = this.get(pkid);
        if (dataModel == null) {
            throw new DAOException("更新的数据不存在!");
        } else {
            BeanUtilsExtend.copyNotNullProperties(model, dataModel);
            this.update(dataModel);
            return dataModel;
        }
    }

    @Override
    public void merge(M model) {
        this.getSession().merge(model);
    }

    @Override
    public void saveOrUpdate(M model) {
        this.getSession().saveOrUpdate(model);
    }

    @Override
    public M saveOrUpdateDynamic(M model, String... ignoreProperties) {
        Field field = null;
        Serializable pkid = null;

        try {
            field = this.entityClass.getDeclaredField(this.pkName);
            field.setAccessible(true);
            pkid = (Serializable)field.get(model);
        } catch (NoSuchFieldException var6) {
            throw new DAOException(this.pkName + "属性未定义！", var6);
        } catch (IllegalAccessException var7) {
            throw new DAOException("非法访问了" + this.pkName + "属性！", var7);
        }

        if (pkid == null) {
            this.save(model);
            return model;
        } else {
            return this.updateDynamic(model, ignoreProperties);
        }
    }

    @Override
    public M saveOrUpdateWithNotNullProperties(M model) {
        Field field = null;
        Serializable pkid = null;

        try {
            field = this.entityClass.getDeclaredField(this.pkName);
            field.setAccessible(true);
            pkid = (Serializable)field.get(model);
        } catch (NoSuchFieldException var5) {
            throw new DAOException(this.pkName + "属性未定义！", var5);
        } catch (IllegalAccessException var6) {
            throw new DAOException("非法访问了" + this.pkName + "属性！", var6);
        }

        if (pkid != null && !pkid.equals("")) {
            return this.updateWithNotNullProperties(model);
        } else {
            this.save(model);
            return model;
        }
    }

    @Override
    public void destroyObject(M model) {
        this.getSession().delete(this.entityClass.getName(), model);
    }

    @Override
    public void destroy(PK id) {
        M entity = this.get(id);
        if (entity != null) {
            this.getSession().delete(this.get(id));
        }

    }

    @Override
    public int deleteList(List<PK> idList) {
        if (idList != null && idList.size() != 0) {
            HashMap map = new HashMap();
            map.put("ids", idList);
            return this.execteBulkWithIn(this.HQL_DELETE_ALL, map, (List)null);
        } else {
            return 0;
        }
    }

    @Override
    public int startUpList(List<PK> idList) {
        if (idList != null && idList.size() != 0) {
            HashMap map = new HashMap();
            map.put("ids", idList);
            return this.execteBulkWithIn(this.HQL_STARTUP_ALL, map, (List)null);
        } else {
            return 0;
        }
    }

    @Override
    public int destroyList(List<PK> idList) {
        if (idList != null && idList.size() != 0) {
            HashMap map = new HashMap();
            map.put("ids", idList);
            return this.execteBulkWithIn(this.HQL_DELETE_ENTITY_ALL, map, (List)null);
        } else {
            return 0;
        }
    }

    @Override
    public M load(PK id) {
        return (M)this.getSession().load(this.entityClass, id);
    }

    @Override
    public M get(PK id) {
        return (M)this.getSession().get(this.entityClass, id);
    }

    @Override
    public boolean exists(PK id) {
        return this.get(id) != null;
    }

    @Override
    public int countAll() {
        Long total = (Long)this.aggregate(this.HQL_COUNT_ALL, (List)null);
        return total.intValue();
    }

    @Override
    public List<M> listAll() {
        return this.list(this.HQL_LIST_ALL, (List)null);
    }

    @Override
    public List<M> listAllWithUndeleted() {
        return this.list(this.HQL_LIST_ALL_UNDELETED, (List)null);
    }

    @Override
    public TableResult listAllByPage(PageInfo pageInfo) {
        return this.listByPage(this.HQL_LIST_ALL, pageInfo, (List)null);
    }

    @Override
    public TableResult listAllUndeletedByPage(PageInfo pageInfo) {
        return this.listByPage(this.HQL_LIST_ALL_UNDELETED, pageInfo, (List)null);
    }

    @Override
    public void flush() {
        this.getSession().flush();
    }

    @Override
    public void clear() {
        this.getSession().clear();
    }

    public void close() {
        this.getSession().close();
    }

    @Override
    public void evict(PK id) {
        this.getSession().evict(this.get(id));
    }

    @Override
    public void evictObject(M model) {
        this.getSession().evict(model);
    }

    public TableResult listByPageWithIn(String hql, PageInfo pageinfo, Map<String, Collection<?>> map, List paramList) {
        Query query = this.getSession().createQuery(hql);
        this.setParameters(query, paramList);
        if (map != null) {
            Iterator var6 = map.entrySet().iterator();

            while(var6.hasNext()) {
                Map.Entry<String, Collection<?>> e = (Map.Entry)var6.next();
                query.setParameterList((String)e.getKey(), (Collection)e.getValue());
            }
        }

        Long total = this.getHqlCountWithPage(hql, paramList, map);
        if (pageinfo != null) {
            int pn = pageinfo.getCurrent();
            int pageSize = pageinfo.getPageSize();
            if (pn > -1 && pageSize > -1) {
                query.setMaxResults(pageSize);
                int start = PageUtil.getPageStart(pn, pageSize);
                if (start != 0) {
                    query.setFirstResult(start);
                }
            }
        }

        List results = query.list();
        return new TableResult(results, total);
    }

    public TableResult listByPage(String hql, PageInfo pageinfo, List paramList) {
        return this.listByPageWithField(hql, pageinfo, paramList, (String)null);
    }

    public TableResult listByPageWithField(String hql, PageInfo pageinfo, List paramList, String fieldName) {
        Query query = this.getSession().createQuery(hql);
        this.setParameters(query, paramList);
        Long total = this.getHqlCountWithPage(hql, paramList, (Map)null, fieldName);
        if (pageinfo != null) {
            int pn = pageinfo.getCurrent();
            int pageSize = pageinfo.getPageSize();
            if (pn > -1 && pageSize > -1) {
                query.setMaxResults(pageSize);
                int start = PageUtil.getPageStart(pn, pageSize);
                if (start != 0) {
                    query.setFirstResult(start);
                }
            }

            if (pn < 0) {
                query.setFirstResult(0);
            }
        }

        List results = query.list();
        return new TableResult(results, total);
    }

    private Long getHqlCountWithPage(String hql, List paramList, Map<String, Collection<?>> map) {
        return this.getHqlCountWithPage(hql, paramList, map, (String)null);
    }

    private Long getHqlCountWithPage(String hql, List paramList, Map<String, Collection<?>> map, String fieldName) {
        int frompoint = hql.toLowerCase().indexOf("from");
        String tmp = hql.substring(frompoint);
        int orderpoint = tmp.toLowerCase().lastIndexOf("order by");
        if (orderpoint > 0) {
            tmp = tmp.substring(0, orderpoint);
        } else {
            tmp = tmp;
        }

        String counthql = null;
        if (fieldName != null) {
            counthql = "select count(" + fieldName + ")  " + tmp + "";
        } else {
            counthql = "select count(*)  " + tmp + "";
        }

        return (Long)this.aggregate(counthql, map, paramList);
    }

    @Override
    public <T> List<T> list(String hql, List paramList) {
        hql = this.processSqlServerFunction(hql);
        Query query = this.getSession().createQuery(hql);
        this.setParameters(query, paramList);
        List<T> results = query.list();
        return results;
    }

    @Override
    public <T> List<T> listWithIn(String hql, Map<String, Collection<?>> map, List paramList) {
        hql = this.processSqlServerFunction(hql);
        Query query = this.getSession().createQuery(hql);
        this.setParameters(query, paramList);
        if (map != null) {
            Iterator var5 = map.entrySet().iterator();

            while(var5.hasNext()) {
                Map.Entry<String, Collection<?>> e = (Map.Entry)var5.next();
                query.setParameterList((String)e.getKey(), (Collection)e.getValue());
            }
        }

        List<T> results = query.list();
        return results;
    }

    @Override
    public <T> T unique(String hql, List paramList) {
        hql = this.processSqlServerFunction(hql);
        Query query = this.getSession().createQuery(hql);
        this.setParameters(query, paramList);
        return (T) query.setMaxResults(1).uniqueResult();
    }

    @Override
    public <T> T uniqueCacheAble(String hql, List paramList, boolean... useQueryCache) {
        hql = this.processSqlServerFunction(hql);
        Query query = this.getSession().createQuery(hql);
        if (useQueryCache != null && useQueryCache.length > 0 && useQueryCache[0]) {
            query.setCacheable(true);
        }

        this.setParameters(query, paramList);
        return (T) query.setMaxResults(1).uniqueResult();
    }

    public <T> T aggregate(String hql, Map<String, Collection<?>> map, List paramList) {
        Query query = this.getSession().createQuery(hql);
        if (paramList != null) {
            this.setParameters(query, paramList);
        }

        if (map != null) {
            Iterator var5 = map.entrySet().iterator();

            while(var5.hasNext()) {
                Map.Entry<String, Collection<?>> e = (Map.Entry)var5.next();
                query.setParameterList((String)e.getKey(), (Collection)e.getValue());
            }
        }

        return (T) query.uniqueResult();
    }

    public <T> T aggregate(String hql, List paramList) {
        Query query = this.getSession().createQuery(hql);
        this.setParameters(query, paramList);
        return (T) query.uniqueResult();
    }

    public int execteBulk(String hql, List paramList) {
        hql = this.processSqlServerFunction(hql);
        Query query = this.getSession().createQuery(hql);
        this.setParameters(query, paramList);
        Object result = query.executeUpdate();
        return result == null ? 0 : (Integer)result;
    }

    public int execteBulkWithIn(String hql, Map<String, Collection<?>> map, List paramList) {
        hql = this.processSqlServerFunction(hql);
        Query query = this.getSession().createQuery(hql);
        this.setParameters(query, paramList);
        if (map != null) {
            Iterator var5 = map.entrySet().iterator();

            while(var5.hasNext()) {
                Map.Entry<String, Collection<?>> e = (Map.Entry)var5.next();
                query.setParameterList((String)e.getKey(), (Collection)e.getValue());
            }
        }

        Object result = query.executeUpdate();
        return result == null ? 0 : (Integer)result;
    }

    protected int execteNativeBulk(String natvieSQL, List paramList) {
        natvieSQL = this.processSqlServerFunction(natvieSQL);
        Query query = this.getSession().createNativeQuery(natvieSQL);
        this.setParameters(query, paramList);
        Object result = query.executeUpdate();
        return result == null ? 0 : (Integer)result;
    }

    protected int execteNativeBulkWithIn(String natvieSQL, Map<String, Collection<?>> map, List paramList) {
        Query query = this.getSession().createNativeQuery(natvieSQL);
        this.setParameters(query, paramList);
        if (map != null) {
            Iterator var5 = map.entrySet().iterator();

            while(var5.hasNext()) {
                Map.Entry<String, Collection<?>> e = (Map.Entry)var5.next();
                query.setParameterList((String)e.getKey(), (Collection)e.getValue());
            }
        }

        Object result = query.executeUpdate();
        return result == null ? 0 : (Integer)result;
    }

    @Override
    public void executeBatchNative(final List<String> sqlList) {
        this.getSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Statement statement = connection.createStatement();
                Throwable var3 = null;

                try {
                    Iterator<String> iterator = sqlList.iterator();
                    int i = 0;

                    while(iterator.hasNext()) {
                        ++i;
                        String sql = (String)iterator.next();
                        statement.addBatch(sql);
                        if (i % 50 == 0) {
                            statement.executeBatch();
                        }
                    }

                    statement.executeBatch();
                } catch (Throwable var14) {
                    var3 = var14;
                    throw var14;
                } finally {
                    if (statement != null) {
                        if (var3 != null) {
                            try {
                                statement.close();
                            } catch (Throwable var13) {
                                var3.addSuppressed(var13);
                            }
                        } else {
                            statement.close();
                        }
                    }

                }

            }
        });
    }

    /** @deprecated */
    @Deprecated
    @Override
    public void executeWithPreparedStatementNative(final String sql, final List<List<Object>> recordList) {
        this.getSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                Throwable var3 = null;

                try {
                    Class<?> clazz = preparedStatement.getClass();
                    if (recordList != null) {
                        Iterator var5 = recordList.iterator();

                        while(var5.hasNext()) {
                            List<Object> paramList = (List)var5.next();
                            int i = 0;
                            Iterator var8 = paramList.iterator();

                            while(var8.hasNext()) {
                                Object value = var8.next();
                                ++i;

                                try {
                                    String methodSuffixName = BaseDaoImpl.this.buildSuffixName(value.getClass().getSimpleName());
                                    Method method = clazz.getMethod("set" + methodSuffixName, Integer.TYPE, value.getClass());
                                    method.setAccessible(true);
                                    method.invoke(preparedStatement, i, value);
                                } catch (NoSuchMethodException var22) {
                                    var22.printStackTrace();
                                } catch (InvocationTargetException var23) {
                                    var23.printStackTrace();
                                } catch (IllegalAccessException var24) {
                                    var24.printStackTrace();
                                }
                            }

                            preparedStatement.addBatch();
                        }
                    }

                    preparedStatement.executeBatch();
                } catch (Throwable var25) {
                    var3 = var25;
                    throw var25;
                } finally {
                    if (preparedStatement != null) {
                        if (var3 != null) {
                            try {
                                preparedStatement.close();
                            } catch (Throwable var21) {
                                var3.addSuppressed(var21);
                            }
                        } else {
                            preparedStatement.close();
                        }
                    }

                }

            }
        });
    }

    private String buildSuffixName(String wrapperName) {
        String type = null;
        Map<String, String> typeMap = new HashMap<String, String>() {
            {
                this.put("Byte", "byte");
                this.put("Short", "short");
                this.put("Integer", "int");
                this.put("Long", "long");
                this.put("Float", "float");
                this.put("Double", "double");
                this.put("Character", "char");
                this.put("Boolean", "boolean");
                this.put("String", "String");
            }
        };
        Set<String> typeSet = typeMap.keySet();
        if (typeSet.contains(wrapperName)) {
            type = (String)typeMap.get(wrapperName);
        }

        return type;
    }

    public TableResult listByNativeByPageWithIn(String nativeSQL, String fieldName, PageInfo info, Map<String, Class<?>> entityMap, Map<String, Type> scalarMap, Map<String, Collection<?>> map, List paramList, boolean... useQueryCache) {
        nativeSQL = this.processSqlServerFunction(nativeSQL);
        NativeQuery query = this.getSession().createNativeQuery(nativeSQL);
        if (useQueryCache != null && useQueryCache.length > 0 && useQueryCache[0]) {
            query.setCacheable(true);
        }

        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        Long total = this.getNativeSqlCountWithPageWithIn(nativeSQL, paramList, map, fieldName);
        Set scalarset;
        Iterator its;
        Map.Entry scalar;
        if (entityMap != null) {
            scalarset = entityMap.entrySet();
            its = scalarset.iterator();

            while(its.hasNext()) {
                scalar = (Map.Entry)its.next();
                query.addEntity((String)scalar.getKey(), (Class)scalar.getValue());
            }
        }

        if (scalarMap != null) {
            scalarset = scalarMap.entrySet();
            its = scalarset.iterator();

            while(its.hasNext()) {
                scalar = (Map.Entry)its.next();
                if (scalar.getValue() != null) {
                    query.addScalar((String)scalar.getKey(), (Type)scalar.getValue());
                } else {
                    query.addScalar((String)scalar.getKey());
                }
            }
        }

        if (map != null) {
            Iterator var14 = map.entrySet().iterator();

            while(var14.hasNext()) {
                Map.Entry<String, Collection<?>> e = (Map.Entry)var14.next();
                query.setParameterList((String)e.getKey(), (Collection)e.getValue());
            }
        }

        this.setParameters(query, paramList);
        if (info != null) {
            int pn = info.getCurrent();
            int pageSize = info.getPageSize();
            if (pn > -1 && pageSize > -1) {
                query.setMaxResults(pageSize);
                int start = PageUtil.getPageStart(pn, pageSize);
                if (start != 0) {
                    query.setFirstResult(start);
                }
            }
        }

        List result = query.list();
        return new TableResult(result, total);
    }

    public TableResult listByNativeByPageWithIn(String nativeSQL, PageInfo info, Map<String, Class<?>> entityMap, Map<String, Type> scalarMap, Map<String, Collection<?>> map, List paramList, boolean... useQueryCache) {
        return this.listByNativeByPageWithIn(nativeSQL, (String)null, info, entityMap, scalarMap, map, paramList, useQueryCache);
    }

    private Long getNativeSqlCountWithPageWithIn(String nativeSQL, List paramList, Map<String, Collection<?>> map) {
        return this.getNativeSqlCountWithPageWithIn(nativeSQL, paramList, map, (String)null);
    }

    private Long getNativeSqlCountWithPageWithIn(String nativeSQL, List paramList, Map<String, Collection<?>> map, String fieldName) {
        int orderpoint = nativeSQL.toLowerCase().lastIndexOf("order by");
        String tmp;
        if (orderpoint > 0) {
            tmp = nativeSQL.substring(0, orderpoint);
        } else {
            tmp = nativeSQL;
        }

        String countsql = null;
        if (fieldName != null) {
            countsql = "select count(" + fieldName + ") n from ( select * from (" + tmp + ") a) b";
        } else {
            countsql = "select count(*) n from ( select * from (" + tmp + ") a) b";
        }

        Map<String, Type> scalarMap = new HashMap();
        scalarMap.put("n", StandardBasicTypes.LONG);
        return (Long)((Map)this.uniqueByNative(countsql, scalarMap, paramList, map)).get("n");
    }

    public TableResult listByNativeByPageWithIn(String nativeSQL, PageInfo info, Map<String, Collection<?>> map, List paramList) {
        return this.listByNativeByPageWithIn(nativeSQL, (String)null, info, (Map)null, (Map)null, map, paramList);
    }

    @Override
    public TableResult listByNativeByPage(String nativeSQL, Map<String, Type> scalarMap, PageInfo info, List paramList) {
        return this.listByNativeByPageWithIn(nativeSQL, (String)null, info, (Map)null, scalarMap, (Map)null, paramList);
    }

    public TableResult listByNativeByPageWithField(String nativeSQL, Map<String, Type> scalarMap, PageInfo info, List paramList, String fieldName) {
        return this.listByNativeByPageWithIn(nativeSQL, fieldName, info, (Map)null, scalarMap, (Map)null, paramList);
    }

    public TableResult listByNativeByPage(String nativeSQL, PageInfo info, List paramList) {
        return this.listByNativeByPageWithIn(nativeSQL, (String)null, info, (Map)null, (Map)null, (Map)null, paramList);
    }

    public TableResult listByNativeByPageWithField(String nativeSQL, PageInfo info, List paramList, String fieldName) {
        return this.listByNativeByPageWithIn(nativeSQL, fieldName, info, (Map)null, (Map)null, (Map)null, paramList);
    }

    public <T> List<T> listByNativeWithIn(String nativeSQL, HashMap<String, Class<?>> entityMap, HashMap<String, Type> scalarMap, Map<String, Collection<?>> map, List paramList, boolean... useQueryCache) {
        nativeSQL = this.processSqlServerFunction(nativeSQL);
        NativeQuery query = this.getSession().createNativeQuery(nativeSQL);
        if (useQueryCache != null && useQueryCache.length > 0 && useQueryCache[0]) {
            query.setCacheable(useQueryCache[0]);
        }

        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        Set scalarset;
        Iterator its;
        Map.Entry scalar;
        if (entityMap != null) {
            scalarset = entityMap.entrySet();
            its = scalarset.iterator();

            while(its.hasNext()) {
                scalar = (Map.Entry)its.next();
                query.addEntity((String)scalar.getKey(), (Class)scalar.getValue());
            }
        }

        if (scalarMap != null) {
            scalarset = scalarMap.entrySet();
            its = scalarset.iterator();

            while(its.hasNext()) {
                scalar = (Map.Entry)its.next();
                if (scalar.getValue() != null) {
                    query.addScalar((String)scalar.getKey(), (Type)scalar.getValue());
                } else {
                    query.addScalar((String)scalar.getKey());
                }
            }
        }

        if (map != null) {
            Iterator var12 = map.entrySet().iterator();

            while(var12.hasNext()) {
                Map.Entry<String, Collection<?>> e = (Map.Entry)var12.next();
                query.setParameterList((String)e.getKey(), (Collection)e.getValue());
            }
        }

        this.setParameters(query, paramList);
        List result = null;

        try {
            result = query.list();
        } catch (IllegalStateException var11) {
            var11.printStackTrace();
        }

        return result;
    }

    @Override
    public <T> List<T> listByNative(String nativeSQL, HashMap<String, Type> scalarMap, List paramList, boolean... useQueryCache) {
        NativeQuery query = this.getSession().createNativeQuery(nativeSQL);
        return this.listByNativeWithIn(nativeSQL, (HashMap)null, scalarMap, (Map)null, paramList, useQueryCache);
    }

    public <T> List<T> listByNative(String nativeSQL, HashMap<String, Class<?>> entityMap, HashMap<String, Type> scalarMap, List paramList, boolean... useQueryCache) {
        return this.listByNativeWithIn(nativeSQL, entityMap, scalarMap, (Map)null, paramList, useQueryCache);
    }

    public <T> List<T> listByNative(String nativeSQL, List paramList, boolean... useQueryCache) {
        return this.listByNativeWithIn(nativeSQL, (HashMap)null, (HashMap)null, (Map)null, paramList, useQueryCache);
    }

    public <T> List<T> listByNativeWithIn(String nativeSQL, Map<String, Collection<?>> map, List paramList, boolean... useQueryCache) {
        return this.listByNativeWithIn(nativeSQL, (HashMap)null, (HashMap)null, map, paramList, useQueryCache);
    }

    public <T> T uniqueByNative(String nativeSQL, Map<String, Type> scalarMap, List paramList, Map<String, Collection<?>> map) {
        nativeSQL = this.processSqlServerFunction(nativeSQL);
        NativeQuery query = this.getSession().createNativeQuery(nativeSQL);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        if (scalarMap != null) {
            Set<Map.Entry<String, Type>> scalarset = scalarMap.entrySet();
            Iterator it = scalarset.iterator();

            while(it.hasNext()) {
                Map.Entry<String, Type> scalar = (Map.Entry)it.next();
                query.addScalar((String)scalar.getKey(), (Type)scalar.getValue());
            }
        }

        if (map != null) {
            Iterator var9 = map.entrySet().iterator();

            while(var9.hasNext()) {
                Map.Entry<String, Collection<?>> e = (Map.Entry)var9.next();
                query.setParameterList((String)e.getKey(), (Collection)e.getValue());
            }
        }

        this.setParameters(query, paramList);
        Object result = query.uniqueResult();
        return (T) result;
    }

    public <T> T uniqueByNative(String nativeSQL, List paramList, Map<String, Collection<?>> map) {
        nativeSQL = this.processSqlServerFunction(nativeSQL);
        NativeQuery query = this.getSession().createNativeQuery(nativeSQL);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        if (map != null) {
            Iterator var5 = map.entrySet().iterator();

            while(var5.hasNext()) {
                Map.Entry<String, Collection<?>> e = (Map.Entry)var5.next();
                query.setParameterList((String)e.getKey(), (Collection)e.getValue());
            }
        }

        this.setParameters(query, paramList);
        Object result = query.uniqueResult();
        return (T) result;
    }

    @Override
    public <T> T uniqueByNative(String nativeSQL, Map<String, Type> scalarMap, List paramList) {
        nativeSQL = this.processSqlServerFunction(nativeSQL);
        NativeQuery query = this.getSession().createNativeQuery(nativeSQL);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        if (scalarMap != null) {
            Set<Map.Entry<String, Type>> scalarset = scalarMap.entrySet();
            Iterator it = scalarset.iterator();

            while(it.hasNext()) {
                Map.Entry<String, Type> scalar = (Map.Entry)it.next();
                query.addScalar((String)scalar.getKey(), (Type)scalar.getValue());
            }
        }

        this.setParameters(query, paramList);
        Object result = query.uniqueResult();
        return (T) result;
    }

    private void setParameters(Query query, List paramList) {
        if (!CollectionUtils.isEmpty(paramList)) {
            for(int i = 0; i < paramList.size(); ++i) {
                if (paramList.get(i) instanceof Date) {
                    query.setTimestamp(i, (Date)paramList.get(i));
                } else {
                    query.setParameter(i, paramList.get(i));
                }
            }
        }

    }

    @Override
    public void evictFromSecondCache(PK id) {
        this.sessionFactory.getCache().evictEntity(this.entityClass, id);
    }

    @Override
    public void evictAllFromSecondCache() {
        this.sessionFactory.getCache().evictEntityRegion(this.entityClass);
    }

    @Override
    public boolean contansInSecondCache(PK id) {
        return this.sessionFactory.getCache().containsEntity(this.entityClass, id);
    }
}
