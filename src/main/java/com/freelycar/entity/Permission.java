package com.freelycar.entity;

import com.sun.istack.internal.NotNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 权限表
 * permission
 * @author tangwei
 */
@Entity
@Table(name = "permission")
@DynamicInsert
@DynamicUpdate
public class Permission implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull
    private Integer id;

    @Column(name = "permission")
    private String permission;

    @Column(name = "roleId")
    private Integer roleId;

    @Column(name = "delStatus",nullable = false,columnDefinition="int default 0")
    private Integer delStatus;

    public Permission() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", permission='" + permission + '\'' +
                ", roleId=" + roleId +
                ", delStatus=" + delStatus +
                '}';
    }
}
