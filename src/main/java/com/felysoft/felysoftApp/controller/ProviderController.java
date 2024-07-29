package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Provider;
import com.felysoft.felysoftApp.service.imp.ProviderImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController //-> proyecto desacoplado  @Controller -> proyecto monolitico
@RequestMapping(path = "/api/provider/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class ProviderController {
    @Autowired
    private ProviderImp providerImp;

    @PreAuthorize("hasAuthority('READ_ALL_PROVIDERS')")
    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Provider> providerList = this.providerImp.findAll();

            response.put("status", "success");
            response.put("data", providerList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_ONE_PROVIDER')")
    @GetMapping("list/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Provider provider = this.providerImp.findById(id);
            response.put("status", "success");
            response.put("data", provider);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_PROVIDERS_BY_CATEGORY')")
    @GetMapping("providersByCategory/{id}")
    public ResponseEntity<Map<String, Object>> findByIdCategory(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Provider> providerList = this.providerImp.findByIdCategory(id);

            response.put("status", "success");
            response.put("data", providerList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_ONE_PROVIDER')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // INSTANCIA OBJETO PROVEEDOR
            Provider provider = Provider.builder()
                    .nit(request.get("nit").toString())
                    .name(request.get("name").toString().toUpperCase())
                    .email(request.get("email").toString().toLowerCase())
                    .phoneNumber(Long.parseLong(request.get("phoneNumber").toString()))
                    .build();

            this.providerImp.create(provider);

            response.put("status", "success");
            response.put("data", "Registro Exitoso");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_PROVIDER')")
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Provider provider = this.providerImp.findById(id);

            provider.setNit(request.get("nit").toString());
            provider.setName(request.get("name").toString().toUpperCase());
            provider.setPhoneNumber(Long.parseLong(request.get("phoneNumber").toString()));
            provider.setEmail(request.get("email").toString().toLowerCase());

            this.providerImp.update(provider);

            response.put("status", "success");
            response.put("data", "Actualización exitosa");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DISABLE_ONE_PROVIDER')")
    @PutMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Provider provider = this.providerImp.findById(id);
            provider.setEliminated(true);

            providerImp.delete(provider);

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
