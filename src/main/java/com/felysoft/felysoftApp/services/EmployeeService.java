package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Employee;
import com.felysoft.felysoftApp.entities.TypeService;

import java.util.List;

public interface EmployeeService {
    public List<Employee> findAll() throws Exception;
    public Employee findById(Long id);
    public void create(Employee employee);
    public void update(Employee employee);
    public void delete(Employee employee);
}