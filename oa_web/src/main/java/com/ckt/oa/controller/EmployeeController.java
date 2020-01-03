package com.ckt.oa.controller;

import com.ckt.oa.biz.DepartmentBiz;
import com.ckt.oa.biz.EmployeeBiz;
import com.ckt.oa.entity.Department;
import com.ckt.oa.entity.Employee;
import com.ckt.oa.global.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeBiz employeeBiz;
    @Autowired
    private DepartmentBiz departmentBiz;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Map<String, Object> map) {
        map.put("list", employeeBiz.getAll());
        return "employee_list";
    }

    @RequestMapping(value = "/to_add")
    public String toAdd(Map<String, Object> map) {
        map.put("employee", new Employee());
        //添加员工的时候要指定部门,修改也是
        map.put("dlist", departmentBiz.getAll());
        //员工要选择职位，修改时也是
        map.put("plist", Constant.getPosts());
        return "employee_add";
    }

    @RequestMapping("/add")
    public String add(Employee employee) {
        employeeBiz.add(employee);
        return "redirect:list";
    }

    @RequestMapping(value = "/to_update", params = "sn")
    public String toUpdate(String sn, Map<String, Object> map) {
        map.put("employee", employeeBiz.get(sn));
        map.put("dlist", departmentBiz.getAll());
        map.put("plist", Constant.getPosts());
        return "employee_update";
    }

    @RequestMapping("/update")
    public String update(Employee employee) {
        employeeBiz.edit(employee);
        return "redirect:list";
    }

    @RequestMapping("/remove")
    public String remove(String sn) {
        employeeBiz.remove(sn);
        return "redirect:list";
    }

}
