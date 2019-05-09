package com.aision.iot.platform.parent.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author yim
 * @description baseEntity
 * @date 2019/4/26
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@ToString
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 5245806560130511760L;

    /**
     * 表ID
     */
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    protected String tid;
    /**
     * 删除标记，表数据，不作物理删除
     */
    @JsonIgnore
    @Column(name = "is_delete")
    protected Boolean delete = false;
    /**
     * 创建人
     */
    @CreatedBy
    @Column(name = "create_by")
    protected String createBy;
    /**
     * 创建时间@Temporal
     */
    @CreatedDate
    @Column(name = "create_time")
    protected Date createTime;
    /**
     * 修改人
     */
    @LastModifiedBy
    @Column(name = "update_by")
    protected String updateBy;
    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "update_time")
    protected Date updateTime;

}
