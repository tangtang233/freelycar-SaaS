package com.freelycar.service;

import com.freelycar.entity.Admin;
import org.springframework.stereotype.Service;

public interface IAdminService {

    Admin findAdminByAccount(String account);
}
