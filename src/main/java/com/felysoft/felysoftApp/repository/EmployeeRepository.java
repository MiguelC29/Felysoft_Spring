package com.felysoft.felysoftApp.repository;

import com.felysoft.felysoftApp.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findEmployeesByEliminatedFalse();

    Employee findEmployeeByIdEmployeeAndEliminatedFalse(Long id);
}
