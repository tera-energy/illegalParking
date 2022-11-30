package com.teraenergy.illegalparking.model.entity.receipt.repository;

import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */
public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {

    Receipt findByReceiptSeqAndIsDel(Integer receiptSeq, Boolean isDel);

    List<Receipt> findAllByIsDel(Boolean isDel);
}
