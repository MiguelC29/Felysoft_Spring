package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Sale;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SaleService {
    List<Sale> findAll() throws Exception;

    Sale findById(Long id);

    @Transactional
    void create(Sale sale);

    @Transactional
    @Modifying
    void update(Sale sale);

    @Transactional
    @Modifying
    void delete(Sale sale);

    @Transactional
    void addDetailToSale(Long saleId, Long detailId);
}
