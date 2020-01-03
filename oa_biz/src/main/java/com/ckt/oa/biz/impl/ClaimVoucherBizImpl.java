package com.ckt.oa.biz.impl;

import com.ckt.oa.biz.ClaimVoucherBiz;
import com.ckt.oa.dao.ClaimVoucherDao;
import com.ckt.oa.dao.ClaimVoucherItemDao;
import com.ckt.oa.dao.DealRecordDao;
import com.ckt.oa.dao.EmployeeDao;
import com.ckt.oa.entity.ClaimVoucher;
import com.ckt.oa.entity.ClaimVoucherItem;
import com.ckt.oa.entity.DealRecord;


import com.ckt.oa.entity.Employee;
import com.ckt.oa.global.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ClaimVoucherBizImpl implements ClaimVoucherBiz {
    @Autowired
    private ClaimVoucherDao claimVoucherDao;
    @Autowired
    private ClaimVoucherItemDao claimVoucherItemDao;
    @Autowired
    private DealRecordDao dealRecordDao;
    @Autowired
    private EmployeeDao employeeDao;


    //保存报销单
    public void save(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        claimVoucher.setCreateTime(new Date());
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Constant.CLAIMVOUCHER_CREATED);
        claimVoucherDao.insert(claimVoucher);

        for (ClaimVoucherItem item : items) {
            item.setClaimVoucherId(claimVoucher.getId());
            claimVoucherItemDao.insert(item);
        }
    }

    public ClaimVoucher get(int id) {
        return claimVoucherDao.select(id);
    }

    public List<ClaimVoucherItem> getItems(int cvid) {
        return claimVoucherItemDao.selectByClaimVoucher(cvid);
    }

    public List<DealRecord> getRecords(int cvid) {
        return dealRecordDao.selectByClaimVoucher(cvid);
    }

    public List<ClaimVoucher> getForSelf(String sn) {
        return claimVoucherDao.selectByCreateSn(sn);
    }

    public List<ClaimVoucher> getForDeal(String sn) {
        return claimVoucherDao.selectByNextDealSn(sn);
    }

    public void update(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        //修改报销单,创建时间不能改变
        //删除不需要的条目,修改已经存在的条目,新增之前不存在的新条目
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Constant.CLAIMVOUCHER_CREATED);
        claimVoucherDao.update(claimVoucher);

        //处理items,获取当前数据库已有的集合
        List<ClaimVoucherItem> olds = claimVoucherItemDao.selectByClaimVoucher(claimVoucher.getId());
        //迭代集合,判断当前获取的集合是否不存在在已有集合中
        for (ClaimVoucherItem old : olds) {
            boolean isHave = false;
            for (ClaimVoucherItem item : items) {
                if (item.getId() == old.getId()) {
                    isHave = true;
                    break;
                }
            }
            if (!isHave) {
                //没有匹配到的item,说明不需要了,删掉
                claimVoucherItemDao.delete(old.getId());
            }
        }
        for (ClaimVoucherItem item : items) {
            item.setClaimVoucherId(claimVoucher.getId());
            if (item.getId() != null && item.getId() > 0) {
                claimVoucherItemDao.update(item);
            } else {
                claimVoucherItemDao.insert(item);
            }
        }

    }

    public void submit(int id) {
        ClaimVoucher claimVoucher = claimVoucherDao.select(id);
        Employee employee = employeeDao.select(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Constant.CLAIMVOUCHER_SUBMIT);
        claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(employee.getDepartmentSn(), Constant.POST_FM).get(0).getSn());
        claimVoucherDao.update(claimVoucher);

        //纪录保存
        DealRecord dealRecord = new DealRecord();
        dealRecord.setDealWay(Constant.DEAL_SUBMIT);
        dealRecord.setDealSn(employee.getSn());
        dealRecord.setClaimVoucherId(id);
        dealRecord.setDealResult(Constant.CLAIMVOUCHER_SUBMIT);
        dealRecord.setDealTime(new Date());
        dealRecord.setComment("无");
        dealRecordDao.insert(dealRecord);


    }

    public void deal(DealRecord dealRecord) {
        ClaimVoucher claimVoucher = claimVoucherDao.select(dealRecord.getClaimVoucherId());
        Employee employee = employeeDao.select(dealRecord.getDealSn());
        dealRecord.setDealTime(new Date());
        if (dealRecord.getDealWay().equals(Constant.DEAL_PASS)) {
            if (claimVoucher.getTotalAmount() <= Constant.LIMIT_CHECK || employee.getPost().equals(Constant.POST_GM)) {
                claimVoucher.setStatus(Constant.CLAIMVOUCHER_APPROVED);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null, Constant.POST_CASHIER).get(0).getSn());
                dealRecord.setDealResult(Constant.CLAIMVOUCHER_APPROVED);
            } else {
                claimVoucher.setStatus(Constant.CLAIMVOUCHER_RECHECK);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null, Constant.POST_GM).get(0).getSn());
                dealRecord.setDealResult(Constant.CLAIMVOUCHER_RECHECK);
            }
        } else if (dealRecord.getDealWay().equals(Constant.DEAL_BACK)) {
            claimVoucher.setStatus(Constant.CLAIMVOUCHER_BACK);
            claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
            dealRecord.setDealResult(Constant.CLAIMVOUCHER_BACK);
        } else if (dealRecord.getDealWay().equals(Constant.DEAL_REJECT)) {
            claimVoucher.setStatus(Constant.CLAIMVOUCHER_TERMINATED);
            claimVoucher.setNextDealSn(null);
            dealRecord.setDealResult(Constant.CLAIMVOUCHER_TERMINATED);

        } else if (dealRecord.getDealWay().equals(Constant.DEAL_PAID)) {
            claimVoucher.setStatus(Constant.CLAIMVOUCHER_PAID);
            claimVoucher.setNextDealSn(null);

            dealRecord.setDealResult(Constant.CLAIMVOUCHER_PAID);
        }
        claimVoucherDao.update(claimVoucher);
        dealRecordDao.insert(dealRecord);
    }
}
