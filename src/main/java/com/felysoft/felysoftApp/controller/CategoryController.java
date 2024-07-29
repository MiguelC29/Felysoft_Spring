package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Category;
import com.felysoft.felysoftApp.service.imp.CategoryImp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // API REST
@RequestMapping(path = "/api/category/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    private CategoryImp categoryImp;

    @PreAuthorize("hasAuthority('READ_ALL_CATEGORIES')")
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

    @PreAuthorize("hasAuthority('READ_ONE_CATEGORY')")
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

    @PreAuthorize("hasAuthority('CREATE_ONE_CATEGORY')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // System.out.println("@@@@" + request);
            // INSTANCIA OBJETO CATEGORIA
            Category category = this.categoryImp.findCategoryByNameAndEliminated(request.get("name").toString().toUpperCase());

            if(category != null) {
                category.setEliminated(false);
                this.categoryImp.update(category);
            } else {
                Category newcategory = Category.builder()
                        .name(request.get("name").toString().toUpperCase())
                        .build();
                this.categoryImp.create(newcategory);
            }

            response.put("status", "success");
            response.put("data", "Registro Exitoso");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_CATEGORY')")
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

    @PreAuthorize("hasAuthority('DISABLE_ONE_CATEGORY')")
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

    // ASOCIATION CATEGORY - PROVIDER
    @PreAuthorize("hasAuthority('READ_CATEGORIES_BY_PROVIDER')")
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

    @PreAuthorize("hasAuthority('ASSOCIATE_CATEGORY_PROVIDER')")
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
}
