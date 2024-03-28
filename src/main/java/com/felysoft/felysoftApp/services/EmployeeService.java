package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Employee;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmployeeService {
    public List<Employee> findAll() throws Exception;
    public Employee findById(Long id);

    @Transactional
    public void create(Employee employee);

    @Transactional
    @Modifying
    public void update(Employee employee);

    @Transactional
    @Modifying
    public void delete(Employee employee);

    @Transactional
    public void addEmployeeToCharge(Long employeeId, Long chargeId);
}