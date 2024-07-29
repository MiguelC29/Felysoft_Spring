package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Service;
import com.felysoft.felysoftApp.entity.TypeService;
import com.felysoft.felysoftApp.service.imp.ServiceImp;
import com.felysoft.felysoftApp.service.imp.TypeserviceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/service/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class ServiceController {
    @Autowired
    private ServiceImp serviceImp;

    @Autowired
    private TypeserviceImp typeServiceImp;

    @PreAuthorize("hasAuthority('READ_ALL_SERVICES')")
    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Service> serviceList = this.serviceImp.findAll();

            response.put("status", "success");
            response.put("data", serviceList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage().toUpperCase());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_ONE_SERVICE')")
    @GetMapping("list/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Service service = this.serviceImp.findById(id);

            response.put("status", "success");
            response.put("data", service);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage().toUpperCase());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_ONE_SERVICE')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            Service service = Service.builder()
                    .state(Service.State.valueOf(request.get("state").toString().toUpperCase()))
                    .priceAdditional(new BigDecimal(request.get("priceAdditional").toString()))
                    .total(new BigDecimal(request.get("total").toString()))
                    .dateCreation(new Timestamp(System.currentTimeMillis()))
                    .dateModification(new Timestamp(System.currentTimeMillis()))
                    .typeService(typeServiceImp.findById(Long.parseLong(request.get("fkIdTypeService").toString())))
                    .build();

            this.serviceImp.create(service);

            response.put("status", "success");
            response.put("data", "Registro Exitoso");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage().toUpperCase());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_SERVICE')")
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Service service = this.serviceImp.findById(id);

            service.setState(Service.State.valueOf(request.get("state").toString().toUpperCase()));
            service.setPriceAdditional(new BigDecimal(request.get("priceAdditional").toString()));
            service.setTotal(new BigDecimal(request.get("total").toString()));
            service.setDateModification(new Timestamp(System.currentTimeMillis()));

            TypeService typeService = typeServiceImp.findById(Long.parseLong(request.get("fkIdTypeService").toString()));
            service.setTypeService(typeService);

            this.serviceImp.update(service);

            response.put("status", "success");
            response.put("data", "Actualización exitosa");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage().toUpperCase());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DISABLE_ONE_SERVICE')")
    @PutMapping(value = "delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Service service = this.serviceImp.findById(id);
            service.setEliminated(true);

            this.serviceImp.delete(service);

            response.put("status", "success");
            response.put("data", "Eliminado Correctamente");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage().toUpperCase());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
