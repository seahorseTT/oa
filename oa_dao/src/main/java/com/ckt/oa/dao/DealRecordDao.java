package com.ckt.oa.dao;

import com.ckt.oa.entity.DealRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealRecordDao {
    void insert(DealRecord dealRecord);

    List<DealRecord> selectByClaimVoucher(int cvid);
}
