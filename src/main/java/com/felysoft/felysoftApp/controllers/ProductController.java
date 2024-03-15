package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.Category;
import com.felysoft.felysoftApp.entities.Product;
import com.felysoft.felysoftApp.entities.Provider;
import com.felysoft.felysoftApp.services.imp.CategoryImp;
import com.felysoft.felysoftApp.services.imp.ProductImp;
import com.felysoft.felysoftApp.services.imp.ProviderImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/product/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductImp productImp;
    @Autowired
    private CategoryImp categoryImp;
    @Autowired
    private ProviderImp providerImp;

    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> productList = this.productImp.findAll();
            response.put("status", "success");
            response.put("data", productList);
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
            Product product = this.productImp.findById(id);
            response.put("status", "success");
            response.put("data", product);
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
            // INSTANCIA OBJETO PRODUCTO
            Product product = new Product();

            // CAMPOS PROPIOS ENTIDAD PRODUCTO
            product.setImage(request.get("image").toString().getBytes());
            product.setTypeImg(request.get("typeImg").toString());
            product.setName(request.get("name").toString().toUpperCase());
            product.setBrand(request.get("brand").toString().toUpperCase());
            product.setSalePrice(new BigDecimal(request.get("salePrice").toString()));
            product.setExpiryDate(new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse((String) request.get("expiryDate")).getTime()));

            // CAMPOS LLAVES FORANEAS
            Category category = categoryImp.findById(Long.parseLong(request.get("fkIdCategory").toString()));
            product.setCategory(category);
            Provider provider = providerImp.findById(Long.parseLong(request.get("fkIdProvider").toString()));
            product.setProvider(provider);

            this.productImp.create(product);

            response.put("status", "success");
            response.put("data", "Registro Exitoso");
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
            // INSTANCIA OBJETO PRODUCTO
            Product product = this.productImp.findById(id);

            // CAMPOS PROPIOS ENTIDAD PRODUCTO
            product.setImage(request.get("image").toString().getBytes());
            product.setTypeImg(request.get("typeImg").toString());
            product.setName(request.get("name").toString().toUpperCase());
            product.setBrand(request.get("brand").toString().toUpperCase());
            product.setSalePrice(new BigDecimal(request.get("salePrice").toString()));
            product.setExpiryDate(new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse((String) request.get("expiryDate")).getTime()));

            // CAMPOS LLAVES FORANEAS
            Category category = categoryImp.findById(Long.parseLong(request.get("fkIdCategory").toString()));
            product.setCategory(category);
            Provider provider = providerImp.findById(Long.parseLong(request.get("fkIdProvider").toString()));
            product.setProvider(provider);

            this.productImp.update(product);

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
            Product product = this.productImp.findById(id);

            product.setEliminated(true);

            productImp.delete(product);

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