package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Employee;
import com.felysoft.felysoftApp.entity.User;
import com.felysoft.felysoftApp.service.imp.EmployeeImp;
import com.felysoft.felysoftApp.service.imp.UserImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee/")
public class EmployeeController {

    @Autowired
    private EmployeeImp employeeImp;

    @Autowired
    private UserImp userImp;

    @PreAuthorize("hasAuthority('READ_ALL_EMPLOYEES')")
    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Employee> employeeList = this.employeeImp.findAll();

            response.put("status", "success");
            response.put("data", employeeList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_ALL_EMPLOYEES_DISABLED')")
    @GetMapping("disabled")
    public ResponseEntity<Map<String, Object>> findAllDisabled() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Employee> employeeList = this.employeeImp.findAllDisabled();

            response.put("status", "success");
            response.put("data", employeeList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_ONE_EMPLOYEE')")
    @GetMapping("list/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Employee employee = this.employeeImp.findById(id);

            response.put("status", "success");
            response.put("data", employee);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_ONE_EMPLOYEE')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // INSTANCIA OBJETO PROVEEDOR
            Employee employee = Employee.builder()
                    .specialty(request.get("specialty").toString().toUpperCase())
                    .dateBirth(Date.valueOf(request.get("dateBirth").toString()))
                    .salary(new BigDecimal(request.get("salary").toString()))
                    .build();

            // CAMPOS LLAVES FORANEAS
            User user = userImp.findById(Long.parseLong(request.get("fkIdUser").toString()));
            employee.setUser(user);

            this.employeeImp.create(employee);

            response.put("status", "success");
            response.put("data", "Registro Exitoso");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //@PreAuthorize("hasAuthority('READ_ALL_USERS')") ------
    @PostMapping("add-employee")
    public ResponseEntity<Map<String, Object>> addEmployeeToCharge(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<>();
        try {
            Long employeeId = Long.parseLong(request.get("employeeId").toString());
            Long chargeId = Long.parseLong(request.get("chargeId").toString());

            this.employeeImp.addEmployeeToCharge(employeeId, chargeId);

            response.put("status", "success");
            response.put("data", "Asociación Exitosa");

        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_EMPLOYEE')")
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // INSTANCIA OBJETO PROVEEDOR
            Employee employee = this.employeeImp.findById(id);

            if (employee == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Empleado no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // CAMPOS PROPIOS ENTIDAD PROVEEDOR
            employee.setSpecialty(request.get("specialty").toString().toUpperCase());
            employee.setDateBirth(Date.valueOf(request.get("dateBirth").toString()));
            employee.setSalary(new BigDecimal(request.get("salary").toString()));

            // CAMPOS LLAVES FORANEAS
            User user = userImp.findById(Long.parseLong(request.get("fkIdUser").toString()));
            employee.setUser(user);
            this.employeeImp.update(employee);

            response.put("status", "success");
            response.put("data", "Actualización exitosa");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_EMPLOYEE')")
    @PutMapping("enable/{id}")
    public ResponseEntity<Map<String, Object>> enable(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Employee employee = this.employeeImp.findByIdDisabled(id);

            if (employee == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Empleado no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            employee.setEliminated(false);

            employeeImp.update(employee);

            response.put("status", "success");
            response.put("data", "Habilitado Correctamente");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DISABLE_ONE_EMPLOYEE')")
    @PutMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Employee employee = this.employeeImp.findById(id);

            if (employee == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Empleado no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            employee.setEliminated(true);

            employeeImp.delete(employee);

            response.put("status", "success");
            response.put("data", "Eliminado Correctamente");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
