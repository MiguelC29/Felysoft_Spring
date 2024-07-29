package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.entity.Employee;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll() throws Exception;

    Employee findById(Long id);

    @Transactional
    void create(Employee employee);

    @Transactional
    @Modifying
    void update(Employee employee);

    @Transactional
    @Modifying
    void delete(Employee employee);

    @Transactional
    void addEmployeeToCharge(Long employeeId, Long chargeId);
}
