package com.sky.service;

import com.sky.dto.PasswordEditDTO;
import com.sky.context.BaseContext;
import com.sky.entity.Employee;
import com.sky.mapper.EmployeeMapper;

public interface PasswordEditService {
    void passedit(PasswordEditDTO dto);
}

