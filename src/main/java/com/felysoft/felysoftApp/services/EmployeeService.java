package com.felysoft.felysoftApp.services;

import com.felysoft.felysoftApp.entities.Employee;

import java.util.List;

public interface EmployeeService {
    public List<Employee> findAll() throws Exception;
    public void create(Employee employee);
    public void update(Employee employee);
    public void delete(Employee employee);
}