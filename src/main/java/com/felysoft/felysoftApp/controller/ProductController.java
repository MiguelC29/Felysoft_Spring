package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Category;
import com.felysoft.felysoftApp.entity.Inventory;
import com.felysoft.felysoftApp.entity.Product;
import com.felysoft.felysoftApp.entity.Provider;
import com.felysoft.felysoftApp.service.imp.CategoryImp;
import com.felysoft.felysoftApp.service.imp.InventoryImp;
import com.felysoft.felysoftApp.service.imp.ProductImp;
import com.felysoft.felysoftApp.service.imp.ProviderImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/product/", method = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.HEAD})
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

    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
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

    @PreAuthorize("hasAuthority('READ_ONE_PRODUCT')")
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

    @PreAuthorize("hasAuthority('CREATE_ONE_PRODUCT')")
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
            // Construir el objeto Product usando el patrón Builder
            Product.ProductBuilder productBuilder = Product.builder()
                    .name(name.toUpperCase())
                    .brand(brand.toUpperCase())
                    .salePrice(salePrice)
                    .expiryDate(new Date(expiryDate.getTime()));

            if (image != null) {
                productBuilder.nameImg(image.getOriginalFilename())
                        .typeImg(image.getContentType())
                        .image(image.getBytes());
            }

            // Obtener las llaves foráneas
            Category category = categoryImp.findById(categoryId);
            Provider provider = providerImp.findById(providerId);

            Product product = productBuilder
                    .category(category)
                    .provider(provider)
                    .build();

            this.productImp.create(product);

            // Construir el objeto Inventory usando el patrón Builder
            Inventory inventory = Inventory.builder()
                    .stock(stockInicial)
                    .state(Inventory.State.DISPONIBLE)
                    .typeInv(Inventory.TypeInv.PRODUCTOS)
                    .dateRegister(new Timestamp(System.currentTimeMillis()))
                    .lastModification(new Timestamp(System.currentTimeMillis()))
                    .product(product)
                    .build();

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

    @PreAuthorize("hasAuthority('UPDATE_ONE_PRODUCT')")
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
                                                      @RequestParam(value = "name", required = false) String name,
                                                      @RequestParam(value = "brand", required = false) String brand,
                                                      @RequestParam(value = "salePrice", required = false) BigDecimal salePrice,
                                                      @RequestParam(value = "expiryDate", required = false) Date expiryDate,
                                                      @RequestParam(value = "category", required = false) Long categoryId,
                                                      @RequestParam(value = "provider", required = false) Long providerId,
                                                      //@RequestParam(value = "stockInicial", required = false) Integer stockInicial,
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
                product.setExpiryDate( new Date(expiryDate.getTime()));
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
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DISABLE_ONE_PRODUCT')")
    @PutMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = this.productImp.findById(id);
            if (product == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Producto no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            Inventory inventory = this.inventoryImp.findByProduct(product);
            if (inventory == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "El producto no se encuentra en el inventario");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                product.setEliminated(true);
                inventory.setEliminated(true);

                this.inventoryImp.delete(inventory);
                this.productImp.delete(product);

                response.put("status", "success");
                response.put("data", "Eliminado Correctamente");
            }
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
