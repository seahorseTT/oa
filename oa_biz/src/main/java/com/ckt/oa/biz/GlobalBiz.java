package com.ckt.oa.biz;

import com.ckt.oa.entity.Employee;

/*
 * 个人业务
 * */
public interface GlobalBiz {
    //登陆操作
    Employee login(String sn, String password);
    //退出操作不在业务层处理，放在表现层，因为退出是对Session的操作；同理，个人信息也放在表现层

    //修改密码
    void changePassword(Employee employee);

}
