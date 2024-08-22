package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.dto.AuthenticationRequest;
import com.felysoft.felysoftApp.entity.Category;
import com.felysoft.felysoftApp.entity.Provider;
import com.felysoft.felysoftApp.service.imp.CategoryImp;
import com.felysoft.felysoftApp.service.imp.ProviderImp;
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
@RequestMapping("/api/category/")
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    private CategoryImp categoryImp;

    @Autowired
    private ProviderImp providerImp;

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

    @PreAuthorize("hasAuthority('READ_ALL_CATEGORIES_DISABLED')")
    @GetMapping("disabled")
    public ResponseEntity<Map<String, Object>> findAllDisabled() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Category> categoryList = this.categoryImp.findAllDisabled();
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
            final boolean isAdmin = AuthenticationRequest.isAdmin();

            Category categoryByName = this.categoryImp.findCategoryByNameAndEliminated(request.get("name").toString().toUpperCase());

            if (categoryByName != null) {
                response.put("status",HttpStatus.BAD_GATEWAY);
                response.put("data","Datos Desahibilitados");

                // Verifica si el rol es de administrador
                String message = (isAdmin) ? "Información ya registrada pero desahibilitada" : "Información ya registrada pero desahibilitada; Contacte al Administrador";

                response.put("detail", message);

                return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
            } else {
                // INSTANCIA OBJETO CATEGORIA
                Category newcategory = Category.builder()
                        .name(request.get("name").toString().toUpperCase())
                        .build();
                this.categoryImp.create(newcategory);

                response.put("status", "success");
                response.put("data", "Registro Exitoso");
            }
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

            if (category == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Categoría no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

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

    @PreAuthorize("hasAuthority('UPDATE_ONE_CATEGORY_DISABLED')")
    @PutMapping("enable/{id}")
    public ResponseEntity<Map<String, Object>> enable(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Category category = this.categoryImp.findByIdDisabled(id);
            if (category == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Categoría no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            category.setEliminated(false);

            this.categoryImp.update(category);

            response.put("status", "success");
            response.put("data", "Habilitado Correctamente");
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

            if (category == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Categoría no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

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
    @PreAuthorize("hasAuthority('READ_ALL_CATEGORY_PROVIDER_ASSOCIATIONS')")
    @GetMapping("categoryProviderAssociations")
    public ResponseEntity<Map<String, Object>> findCategoryProviderAssociations() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Object[]> categoryList = this.categoryImp.findCategoryProviderNames();
            response.put("status", "success");
            response.put("data", categoryList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

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
                response.put("associationType", "category-provider");
                response.put("entity1", "categoría");
                response.put("entity2", "proveedor");
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

    @PreAuthorize("hasAuthority('DELETE_ONE_ASSOCIATION')")
    @PutMapping("deleteAssociation")
    public ResponseEntity<Map<String, Object>> deleteAssociation(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Category category = this.categoryImp.findCategoryByName(request.get("categoryName").toString());
            Provider provider = this.providerImp.findProviderByName(request.get("providerName").toString());

            if (category == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Categoría no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            if (provider == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Proveedor no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            category.getProviders().remove(provider);
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
