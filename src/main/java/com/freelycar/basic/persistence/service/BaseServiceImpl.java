package com.freelycar.basic.persistence.service;

import com.freelycar.basic.persistence.dao.BaseDao;
import com.freelycar.basic.wrapper.PageInfo;
import com.freelycar.basic.wrapper.TableResult;
import org.hibernate.service.spi.ServiceException;

import java.io.Serializable;
import java.util.List;

/**
 * @author tangwei
 * @param <M>
 * @param <PK>
 */
public abstract class BaseServiceImpl<M extends Serializable, PK extends Serializable> implements BaseService<M, PK> {
    protected BaseDao<M, PK> baseDao;

    public BaseServiceImpl() {
    }

    public abstract void setBaseDao(BaseDao<M, PK> baseDao);

    @Override
    public M save(M model) {
        this.baseDao.save(model);
        return model;
    }

    @Override
    public void merge(M model) {
        this.baseDao.merge(model);
    }

    @Override
    public void saveOrUpdate(M model) {
        this.baseDao.saveOrUpdate(model);
    }

    @Override
    public M saveOrUpdateDynamic(M model, String... ignoreProperties) {
        try {
            return this.baseDao.saveOrUpdateDynamic(model, ignoreProperties);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public M saveOrUpdateWithNotNullProperties(M model) {
        try {
            return this.baseDao.saveOrUpdateWithNotNullProperties(model);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void update(M model) {
        this.baseDao.update(model);
    }

    @Override
    public M updateDynamic(M model, String... ignoreProperties) throws Exception {
        return this.baseDao.updateDynamic(model, ignoreProperties);
    }

    @Override
    public M updateWithNotNullProperties(M model) throws Exception {
        try {
            return this.baseDao.updateWithNotNullProperties(model);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public M load(PK id) {
        return this.baseDao.load(id);
    }

    @Override
    public void destroy(PK id) {
        this.baseDao.destroy(id);
    }

    @Override
    public void destroyObject(M model) {
        this.baseDao.destroyObject(model);
    }

    @Override
    public int deleteList(List<PK> idList) {
        return this.baseDao.deleteList(idList);
    }

    @Override
    public int startUpList(List<PK> idList) {
        return this.baseDao.startUpList(idList);
    }

    @Override
    public int destroyList(List<PK> idList) {
        return this.baseDao.destroyList(idList);
    }

    @Override
    public M get(PK id) {
        return this.baseDao.get(id);
    }

    @Override
    public int countAll() {
        return this.baseDao.countAll();
    }

    @Override
    public List<M> listAll() {
        return this.baseDao.listAll();
    }

    @Override
    public List<M> listAllWithUndeleted() {
        return this.baseDao.listAllWithUndeleted();
    }

    @Override
    public TableResult listAllByPage(PageInfo pageInfo) {
        return this.baseDao.listAllByPage(pageInfo);
    }

    @Override
    public TableResult listAllUndeletedByPage(PageInfo pageInfo) {
        return this.baseDao.listAllUndeletedByPage(pageInfo);
    }

    @Override
    public void flush() {
        this.baseDao.flush();
    }

    @Override
    public void clear() {
        this.baseDao.clear();
    }

    @Override
    public void evictFromSecondCache(PK id) {
        this.baseDao.evictFromSecondCache(id);
    }

    @Override
    public void evictAllFromSecondCache() {
        this.baseDao.evictAllFromSecondCache();
    }

    @Override
    public boolean contansInSecondCache(PK id) {
        return this.baseDao.contansInSecondCache(id);
    }
}
