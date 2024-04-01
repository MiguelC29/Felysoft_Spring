package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.Employee;
import com.felysoft.felysoftApp.entities.User;
import com.felysoft.felysoftApp.services.imp.EmployeeImp;
import com.felysoft.felysoftApp.services.imp.UserImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/employee/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class EmployeeController {

    @Autowired
    private EmployeeImp employeeImp;

    @Autowired
    private UserImp userImp;

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

    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // INSTANCIA OBJETO PROVEEDOR
            Employee employee = new Employee();

            // CAMPOS PROPIOS ENTIDAD PROVEEDOR
            employee.setSpecialty(request.get("specialty").toString().toUpperCase());
            employee.setDateBirth(Date.valueOf(request.get("dateBirth").toString()));
            employee.setSalary(new BigDecimal(request.get("salary").toString()));
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

    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // INSTANCIA OBJETO PROVEEDOR
            Employee employee = this.employeeImp.findById(id);

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

    @PutMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Employee employee = this.employeeImp.findById(id);

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