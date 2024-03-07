package com.felysoft.felysoftApp.services.imp;

import com.felysoft.felysoftApp.entities.Employee;
import com.felysoft.felysoftApp.repositories.EmployeeRepository;
import com.felysoft.felysoftApp.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeImp implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> findAll() throws Exception {
        return this.employeeRepository.findAll();
    }

    @Override
    public Employee findById(Long id) {
        return this.employeeRepository.findById(id).orElse(null);
    }

    @Override
    public void create(Employee employee) {
        this.employeeRepository.save(employee);
    }

    @Override
    public void update(Employee employee) {
        this.employeeRepository.save(employee);
    }

    @Override
    public void delete(Employee employee) {
        this.employeeRepository.delete(employee);
    }
}