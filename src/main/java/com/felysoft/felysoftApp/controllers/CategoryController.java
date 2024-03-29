package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.Category;
import com.felysoft.felysoftApp.services.imp.CategoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController //api rest
@RequestMapping(path = "/api/category/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class CategoryController {

    @Autowired
    private CategoryImp categoryImp;

    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Category> categoryList = this.categoryImp.findAll();
            response.put("status", "success");
            response.put("data", categoryList);
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
            Category category = this.categoryImp.findById(id);
            response.put("status", "success");
            response.put("data", category);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("categoriesByProvider/{id}")
    public ResponseEntity<Map<String, Object>> findByIdProvider(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Category> categoryList = this.categoryImp.findByIdProvider(id);
            response.put("status", "success");
            response.put("data", categoryList);
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
            // System.out.println("@@@@" + request);
            // INSTANCIA OBJETO CATEGORIA
            Category category = new Category();

            // CAMPOS PROPIOS ENTIDAD CATEGORIA
            //category.setIdCategory(Long.parseLong(get("id").toString()));
            category.setName(request.get("name").toString().toUpperCase());

            this.categoryImp.create(category);

            response.put("status", "success");
            response.put("data", "Registro Exitoso");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("add-provider")
    public ResponseEntity<Map<String, Object>> addProviderToCategory(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long categoryId = Long.parseLong(request.get("categoryId").toString());
            Long providerId = Long.parseLong(request.get("providerId").toString());

            // Verificar si la asociación ya existe
            boolean associationExists = this.categoryImp.checkAssociationExists(categoryId, providerId);
            if (associationExists) {
                throw new RuntimeException("Asociación existente");
            }

            this.categoryImp.addProviderToCategory(categoryId, providerId);

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
            Category category = this.categoryImp.findById(id);

            category.setName(request.get("name").toString().toUpperCase());

            this.categoryImp.update(category);

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
            Category category = this.categoryImp.findById(id);

            category.setEliminated(true);

            this.categoryImp.delete(category);

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