package com.ckt.oa.biz.impl;

import com.ckt.oa.biz.GlobalBiz;
import com.ckt.oa.dao.EmployeeDao;
import com.ckt.oa.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GlobalBizImpl implements GlobalBiz {
    @Autowired
    private EmployeeDao employeeDao;

    public Employee login(String sn, String password) {
        Employee employee = employeeDao.select(sn);
        if (employee != null && employee.getPassword().equals(password)) {
            return employee;
        }
        return null;
    }

    public void changePassword(Employee employee) {
        employeeDao.update(employee);
    }
}
