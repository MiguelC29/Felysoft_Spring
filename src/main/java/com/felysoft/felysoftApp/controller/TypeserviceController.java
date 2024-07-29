package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.TypeService;
import com.felysoft.felysoftApp.service.imp.TypeserviceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/typeservice/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class TypeserviceController {
    @Autowired
    private TypeserviceImp typeServiceImp;

    @PreAuthorize("hasAuthority('READ_ALL_TYPE_SERVICES')")
    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll(){
        Map<String, Object> response = new HashMap<>();
        try {
            List<TypeService> typeServiceList = this.typeServiceImp.findAll();

            response.put("status", "success");
            response.put("data", typeServiceList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage().toUpperCase());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_ONE_TYPE_SERVICE')")
    @GetMapping("list/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            TypeService typeService = this.typeServiceImp.findById(id);

            response.put("status", "success");
            response.put("data", typeService);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage().toUpperCase());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_ONE_TYPE_SERVICE')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            TypeService typeService = TypeService.builder()
                    .name(request.get("name").toString().toUpperCase())
                    .description(request.get("description").toString().toUpperCase())
                    .price(new BigDecimal(request.get("price").toString()))
                    .build();

            this.typeServiceImp.create(typeService);

            response.put("status", "success");
            response.put("data", "Registro Exitoso");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage().toUpperCase());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_TYPE_SERVICE')")
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            TypeService typeService = this.typeServiceImp.findById(id);

            if (typeService == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "El tipo de servicio con el ID proporcionado no fue encontrado.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            typeService.setName(request.get("name").toString().toUpperCase());
            typeService.setDescription(request.get("description").toString().toUpperCase());

            BigDecimal price = new BigDecimal(request.get("price").toString().toUpperCase());
            typeService.setPrice(price);

            this.typeServiceImp.update(typeService);

            response.put("status", "success");
            response.put("data", "Actualización exitosa");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage().toUpperCase());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DISABLE_ONE_TYPE_SERVICE')")
    @PutMapping(value = "delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            TypeService typeService = this.typeServiceImp.findById(id);
            typeService.setEliminated(true);

            this.typeServiceImp.delete(typeService);

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
