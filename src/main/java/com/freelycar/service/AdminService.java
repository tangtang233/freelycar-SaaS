package com.freelycar.service;

import com.freelycar.basic.persistence.service.BaseService;
import com.freelycar.entity.Admin;

public interface AdminService extends BaseService<Admin,Integer> {

    Admin findAdminByAccount(String account);
}
