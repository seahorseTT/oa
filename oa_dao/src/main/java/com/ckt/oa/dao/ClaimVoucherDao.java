package com.ckt.oa.dao;

import com.ckt.oa.entity.ClaimVoucher;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimVoucherDao {
    void insert(ClaimVoucher claimVoucher);

    void update(ClaimVoucher claimVoucher);

    void delete(int id);

    ClaimVoucher select(int id);

    List<ClaimVoucher> selectByCreateSn(String csn); //查询某个人创建的报销单

    List<ClaimVoucher> selectByNextDealSn(String ndsn); //查询某个人能处理的报销单

}
