package com.ckt.oa.dao;

import com.ckt.oa.entity.ClaimVoucherItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimVoucherItemDao {
    void insert(ClaimVoucherItem claimVoucherItem);

    void update(ClaimVoucherItem claimVoucherItem);

    void delete(int id);

    List<ClaimVoucherItem> selectByClaimVoucher(int cvid);
}
