package com.aision.iot.platform.parent.service;


import com.aision.iot.platform.parent.entity.BaseEntity;
import com.aision.iot.platform.parent.jpa.BaseRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author yim
 * @description BaseService
 * @date 2019/4/26
 */
public abstract class BaseService<T extends BaseEntity, ID extends Serializable> {
    /**
     * 具体dao类实现
     *
     * @return
     */
    protected abstract BaseRepository<T, ID> initBaseRepository();

    /**
     * 对子类实现的initJpaSpecificationExecutor方法回值的值进行空值验证
     *
     * @return Repository的实现
     */
    private BaseRepository<T, ID> getBaseRepository() {
        BaseRepository<T, ID> baseRepository = initBaseRepository();
        if (baseRepository == null) {
            throw new RuntimeException("initBaseRepository方法返回空值");
        }
        return baseRepository;
    }

    /**
     * 保存
     *
     * @param entity
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public T save(T entity) {
        return getBaseRepository().save(entity);
    }

    /**
     * 批量保存
     *
     * @param entities
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Iterable<T> save(Iterable<T> entities) {
        return getBaseRepository().saveAll(entities);
    }

    /**
     * 物理删除
     *
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void delete(ID id) {
        getBaseRepository().deleteById(id);
    }

    /**
     * 按ID确定数据是否存在
     *
     * @param id
     * @return
     */
    public boolean exists(ID id) {
        return getBaseRepository().existsById(id);
    }

    /**
     * 统计数据库里的表行数
     *
     * @return
     */
    public long count() {
        return getBaseRepository().count();
    }

    /**
     * 按ID查找
     *
     * @param id
     * @return
     */
    public T findOne(ID id) {
        return getBaseRepository().findById(id).get();
    }

    /**
     * 取所有表数据
     *
     * @return
     */
    public Iterable<T> findAll() {
        return getBaseRepository().findAll();
    }

    /**
     * 按ID批量查找
     *
     * @param ids
     * @return
     */
    public Iterable<T> findAll(Iterable<ID> ids) {
        return getBaseRepository().findAllById(ids);
    }

    /**
     * 带排序的取全表数据
     *
     * @param sort
     * @return
     */
    public Iterable<T> findAll(Sort sort) {
        return getBaseRepository().findAll(sort);
    }

    /**
     * 按查询条件取全表分页数据
     *
     * @param spec
     * @return
     */
    public List<T> findAll(Specification<T> spec, Sort sort) {
        if (spec == null && sort == null) {
            return getBaseRepository().findAll();
        } else if (spec == null) {
            return getBaseRepository().findAll(sort);
        } else if (sort == null) {
            return getBaseRepository().findAll(spec);
        }
        return getBaseRepository().findAll(spec, sort);
    }

    /**
     * 按查询条件取全表数据
     *
     * @param spec
     * @return
     */
    public List<T> findAll(Specification<T> spec) {
        if (spec == null) {
            return getBaseRepository().findAll();
        }
        return getBaseRepository().findAll(spec);
    }
}
