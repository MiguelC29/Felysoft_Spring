package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Sale;
import com.felysoft.felysoftApp.repositories.SaleRepository;
import com.felysoft.felysoftApp.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleImp implements SaleService{

    @Autowired
    private SaleRepository saleRepository;

    @Override
    public List<Sale> findAll() throws Exception {
        return this.saleRepository.findSaleByEliminatedFalse();
    }

    @Override
    public Sale findById(Long id){
        return this.saleRepository.findById(id).orElse(null);
    }
    @Override
    public void create(Sale sale) {
        this.saleRepository.save(sale);
    }

    @Override
    public void update(Sale sale) {
        this.saleRepository.save(sale);
    }

    @Override
    public void delete(Sale sale) {
        this.saleRepository.save(sale);
    }
}