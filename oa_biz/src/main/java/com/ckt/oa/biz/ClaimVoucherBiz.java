package com.ckt.oa.biz;

import com.ckt.oa.entity.ClaimVoucher;
import com.ckt.oa.entity.ClaimVoucherItem;
import com.ckt.oa.entity.DealRecord;

import java.util.List;

public interface ClaimVoucherBiz {
    //保存报销单信息
    void save(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items);

    //根据id获取报销单
    ClaimVoucher get(int id);

    //获取报销单条目
    List<ClaimVoucherItem> getItems(int cvid);

    List<DealRecord> getRecords(int cvid);

    //根据员工编号获取个人报销单
    List<ClaimVoucher> getForSelf(String sn);

    //获取待处理报销单
    List<ClaimVoucher> getForDeal(String sn);

    //修改报销单
    void update(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items);

    //提交报销单
    void submit(int id);

    //审核报销单
    void deal(DealRecord dealRecord);
}
