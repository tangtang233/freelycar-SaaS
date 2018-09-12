package com.freelycar.entity;

import com.sun.istack.internal.NotNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 管理账号表
 * admin
 * @author tangwei
 */
@Entity
@Table(name = "admin")
@DynamicInsert
@DynamicUpdate
public class Admin implements Serializable {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "id")
    @NotNull
	private Integer id;

	@Column(name = "account")
	private String account;

	@Column(name = "comment")
	private String comment;

	//	@JsonDeserialize(using=JsonDateDeserialize.class)
	@Column(name = "createDate")
	private Date createDate;

	@Column(name = "current")
	private Boolean current;

	@Column(name = "name")
	private String name;

	@Column(name = "password")
	private String password;

	@Column(name = "roleId")
	private Integer roleId;

	@Column(name = "staffId")
	private Integer staffId;

	@Column(name = "delStatus",nullable = false,columnDefinition="int default 0")
	private Integer delStatus;


	public Admin() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Boolean getCurrent() {
		return current;
	}

	public void setCurrent(Boolean current) {
		this.current = current;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getStaffId() {
		return staffId;
	}

	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
	}

	public Integer getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

	@Override
	public String toString() {
		return "Admin{" +
				"id=" + id +
				", account='" + account + '\'' +
				", comment='" + comment + '\'' +
				", createDate=" + createDate +
				", current=" + current +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				", roleId=" + roleId +
				", staffId=" + staffId +
				", delStatus=" + delStatus +
				'}';
	}
}
