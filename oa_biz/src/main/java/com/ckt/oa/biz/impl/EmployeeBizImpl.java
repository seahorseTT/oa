package com.ckt.oa.biz.impl;

import com.ckt.oa.biz.EmployeeBiz;
import com.ckt.oa.dao.EmployeeDao;
import com.ckt.oa.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeBizImpl implements EmployeeBiz {

    @Autowired
    private EmployeeDao employeeDao;

    public void add(Employee employee) {
        //新建员工的时候，要设置个初始密码
        employee.setPassword("000000");
        employeeDao.insert(employee);
    }

    public void edit(Employee employee) {

        employeeDao.update(employee);
    }

    public void remove(String sn) {
        employeeDao.delete(sn);
    }

    public Employee get(String sn) {
        return employeeDao.select(sn);
    }

    public List<Employee> getAll() {
        return employeeDao.selectAll();
    }
}
