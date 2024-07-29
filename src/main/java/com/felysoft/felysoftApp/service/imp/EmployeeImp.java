package com.felysoft.felysoftApp.service.imp;

import com.felysoft.felysoftApp.entity.Charge;
import com.felysoft.felysoftApp.entity.Employee;
import com.felysoft.felysoftApp.repository.ChargeRepository;
import com.felysoft.felysoftApp.repository.EmployeeRepository;
import com.felysoft.felysoftApp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeImp implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ChargeRepository chargeRepository;

    @Override
    public List<Employee> findAll() throws Exception {
        return this.employeeRepository.findEmployeesByEliminatedFalse();
    }

    @Override
    public Employee findById(Long id) {
        return this.employeeRepository.findEmployeeByIdEmployeeAndEliminatedFalse(id);
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
        this.employeeRepository.save(employee);
    }

    @Override
    public void addEmployeeToCharge(Long employeeId, Long chargeId) {
        Employee employee = this.employeeRepository.findById(employeeId).orElse(null);
        Charge charge = this.chargeRepository.findById(chargeId).orElse(null);

        employee.getCharges().add(charge);
        this.employeeRepository.save(employee);
    }
}
