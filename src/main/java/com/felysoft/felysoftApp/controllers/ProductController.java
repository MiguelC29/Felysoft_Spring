package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.Category;
import com.felysoft.felysoftApp.entities.Inventory;
import com.felysoft.felysoftApp.entities.Product;
import com.felysoft.felysoftApp.entities.Provider;
import com.felysoft.felysoftApp.services.imp.CategoryImp;
import com.felysoft.felysoftApp.services.imp.InventoryImp;
import com.felysoft.felysoftApp.services.imp.ProductImp;
import com.felysoft.felysoftApp.services.imp.ProviderImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/product/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class ProductController {

    @Autowired
    private ProductImp productImp;
    @Autowired
    private CategoryImp categoryImp;
    @Autowired
    private ProviderImp providerImp;
    @Autowired
    private InventoryImp inventoryImp;

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
    public ResponseEntity<Map<String, Object>> create(
            @RequestParam("name") String name,
            @RequestParam("brand") String brand,
            @RequestParam("salePrice") BigDecimal salePrice,
            @RequestParam("expiryDate") Date expiryDate,
            @RequestParam("category") Long categoryId,
            @RequestParam("provider") Long providerId,
            @RequestParam("stockInicial") int stockInicial,
            @RequestParam("image") MultipartFile image) {
        Map<String, Object> response = new HashMap<>();
        try {
            // INSTANCIA OBJETO PRODUCTO
            Product product = new Product();

            // CAMPOS PROPIOS ENTIDAD PRODUCTO
            if (image != null) {
                product.setNameImg(image.getOriginalFilename());
                product.setTypeImg(image.getContentType());
                product.setImage(image.getBytes());
            }

            product.setName(name.toUpperCase());
            product.setBrand(brand.toUpperCase());
            product.setSalePrice(new BigDecimal(salePrice.toString()));
            product.setExpiryDate(new java.sql.Date(expiryDate.getTime()));

            // CAMPOS LLAVES FORANEAS
            Category category = categoryImp.findById(Long.parseLong(categoryId.toString()));
            product.setCategory(category);
            Provider provider = providerImp.findById(Long.parseLong(providerId.toString()));
            product.setProvider(provider);

            this.productImp.create(product);

            // INSTANCIA OBJETO INVENTARIO
            Inventory inventory = new Inventory();
            // CAMPOS PROPIOS ENTIDAD INVENTARIO
            inventory.setStock(Integer.parseInt(String.valueOf(stockInicial)));
            inventory.setState(Inventory.State.DISPONIBLE);
            inventory.setTypeInv(Inventory.TypeInv.PRODUCTOS);
            // Configurar fechas de creación y actualización
            inventory.setDateRegister(new Timestamp(System.currentTimeMillis()));
            inventory.setLastModification(new Timestamp(System.currentTimeMillis()));

            // CAMPOS LLAVES FORANEAS
            inventory.setProduct(product);

            this.inventoryImp.create(inventory);

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
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "salePrice", required = false) BigDecimal salePrice,
            @RequestParam(value = "expiryDate", required = false) Date expiryDate,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "provider", required = false) Long providerId,
            @RequestParam(value = "stockInicial", required = false) Integer stockInicial,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        Map<String, Object> response = new HashMap<>();
        try {
            // INSTANCIA OBJETO PRODUCTO
            Product product = this.productImp.findById(id);

            // CAMPOS PROPIOS ENTIDAD PRODUCTO
            if (product == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Producto no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // Actualizar los campos del producto con los nuevos valores si se proporcionan
            if (name != null) {
                product.setName(name.toUpperCase());
            }
            if (brand != null) {
                product.setBrand(brand.toUpperCase());
            }
            if (salePrice != null) {
                product.setSalePrice(salePrice);
            }
            if (expiryDate != null) {
                product.setExpiryDate(new java.sql.Date(expiryDate.getTime()));
            }
            if (categoryId != null) {
                Category category = categoryImp.findById(categoryId);
                if (category == null) {
                    response.put("status", HttpStatus.NOT_FOUND);
                    response.put("data", "Categoría no encontrada");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                product.setCategory(category);
            }
            if (providerId != null) {
                Provider provider = providerImp.findById(providerId);
                if (provider == null) {
                    response.put("status", HttpStatus.NOT_FOUND);
                    response.put("data", "Proveedor no encontrado");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                product.setProvider(provider);
            }

            // Si se proporciona una nueva imagen, actualizarla
            if (image != null) {
                product.setNameImg(image.getOriginalFilename());
                product.setTypeImg(image.getContentType());
                product.setImage(image.getBytes());
            }

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
            Inventory inventory = this.inventoryImp.findByProduct(product);

            product.setEliminated(true);
            inventory.setEliminated(true);

            this.inventoryImp.delete(inventory);
            this.productImp.delete(product);

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