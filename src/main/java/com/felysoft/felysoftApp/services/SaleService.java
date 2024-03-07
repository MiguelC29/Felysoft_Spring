package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Sale;
import java.util.List;

public interface SaleService {
    public List<Sale> findAll() throws Exception;
    public Sale findById(Long id);
    public void create(Sale sale);
    public void update(Sale sale);
    public void delete(Sale sale);
}