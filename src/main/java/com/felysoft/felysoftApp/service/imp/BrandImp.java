package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Brand;
import com.felysoft.felysoftApp.repository.BrandRepository;
import com.felysoft.felysoftApp.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandImp implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Brand> findAll() throws Exception {
        return this.brandRepository.findBrandsByEliminatedFalse();
    }

    @Override
    public List<Brand> findAllDisabled() throws Exception {
        return this.brandRepository.findBrandsByEliminatedTrue();
    }

    @Override
    public Brand findById(Long id) throws Exception {
        return this.brandRepository.findBrandByIdBrandAndEliminatedFalse(id);
    }

    @Override
    public Brand findByIdDisabled(Long id) throws Exception {
        return this.brandRepository.findBrandByIdBrandAndEliminatedTrue(id);
    }

    @Override
    public Brand findBrandByNameAndEliminated(String name) {
        return this.brandRepository.findBrandByNameAndEliminatedTrue(name);
    }

    @Override
    public Brand findCategoryByName(String name) {
        return this.brandRepository.findBrandByNameAndEliminatedFalse(name);
    }

    @Override
    public void create(Brand brand) throws Exception {
        this.brandRepository.save(brand);
    }

    @Override
    public void update(Brand brand) throws Exception {
        this.brandRepository.save(brand);
    }

    @Override
    public void delete(Brand brand) throws Exception {
        this.brandRepository.save(brand);
    }
}
