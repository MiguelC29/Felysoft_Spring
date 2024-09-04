package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.dto.AuthenticationRequest;
import com.felysoft.felysoftApp.entity.*;
import com.felysoft.felysoftApp.service.imp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product/")
public class ProductController {
    @Autowired
    private ProductImp productImp;

    @Autowired
    private CategoryImp categoryImp;

    @Autowired
    private ProviderImp providerImp;

    @Autowired
    private InventoryImp inventoryImp;

    @Autowired
    private BrandImp brandImp;

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

    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS_DISABLED')")
    @GetMapping("disabled")
    public ResponseEntity<Map<String, Object>> findAllDisabled() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> productList = this.productImp.findAllDisabled();

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
            @RequestParam("salePrice") BigDecimal salePrice,
            @RequestParam("expiryDate") Date expiryDate,
            @RequestParam("brand") Long brandId,
            @RequestParam("category") Long categoryId,
            @RequestParam("provider") Long providerId,
            @RequestParam("stockInicial") int stockInicial,
            @RequestParam("image") MultipartFile image) {
        Map<String, Object> response = new HashMap<>();
        try {
            final boolean isAdmin = AuthenticationRequest.isAdmin();

            Product productByName = this.productImp.findProductByNameAndEliminated(name.toUpperCase());

            if (productByName != null) {
                response.put("status",HttpStatus.BAD_GATEWAY);
                response.put("data","Datos Desahibilitados");

                // Verifica si el rol es de administrador
                String message = (isAdmin) ? "Información ya registrada pero desahibilitada" : "Información ya registrada pero desahibilitada; Contacte al Administrador";

                response.put("detail", message);

                return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
            } else {
                // Construir el objeto Product usando el patrón Builder
                Product.ProductBuilder productBuilder = Product.builder()
                        .name(name.toUpperCase())
                        .salePrice(salePrice)
                        .expiryDate(new Date(expiryDate.getTime()));

                if (image != null) {
                    String contentType = image.getContentType();

                    // Lista de tipos MIME permitidos
                    List<String> allowedContentTypes = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp");

                    if (allowedContentTypes.contains(contentType)) {
                        productBuilder.nameImg(image.getOriginalFilename())
                                .typeImg(contentType)
                                .image(image.getBytes());
                    } else {
                        response.put("error_message", "Tipo Imagen Incorrecto");
                        throw new IllegalArgumentException("Tipo de archivo no permitido. Solo se permiten imágenes JPEG, JPG, PNG, WEBP.");
                    }
                }

                // Obtener las llaves foráneas
                Category category = categoryImp.findById(categoryId);
                Provider provider = providerImp.findById(providerId);
                Brand brand = brandImp.findById(brandId);

                Product product = productBuilder
                        .category(category)
                        .provider(provider)
                        .brand(brand)
                        .build();

                // Construir el objeto Inventory usando el patrón Builder
                Inventory inventory = Inventory.builder()
                        .stock(stockInicial)
                        .state((stockInicial < 6 ? Inventory.State.BAJO : Inventory.State.DISPONIBLE))
                        .typeInv(Inventory.TypeInv.PRODUCTOS)
                        .dateRegister(new Timestamp(System.currentTimeMillis()))
                        .lastModification(new Timestamp(System.currentTimeMillis()))
                        .product(product)
                        .build();

                this.productImp.create(product);
                this.inventoryImp.create(inventory);

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

    @PreAuthorize("hasAuthority('UPDATE_ONE_PRODUCT')")
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
                                                      @RequestParam(value = "name", required = false) String name,
                                                      @RequestParam(value = "salePrice", required = false) BigDecimal salePrice,
                                                      @RequestParam(value = "expiryDate", required = false) Date expiryDate,
                                                      @RequestParam(value = "category", required = false) Long categoryId,
                                                      @RequestParam(value = "provider", required = false) Long providerId,
                                                      @RequestParam(value = "brand", required = false) Long brandId,
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

            if (brandId != null) {
                Brand brand = brandImp.findById(brandId);
                if (brand == null) {
                    response.put("status", HttpStatus.NOT_FOUND);
                    response.put("data", "Marca no encontrada");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                product.setBrand(brand);
            }

            // Si se proporciona una nueva imagen, actualizarla
            if (image != null) {
                String contentType = image.getContentType();

                // Lista de tipos MIME permitidos
                List<String> allowedContentTypes = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/webp");

                if (allowedContentTypes.contains(contentType)) {
                    product.setNameImg(image.getOriginalFilename());
                    product.setTypeImg(contentType);
                    product.setImage(image.getBytes());
                } else {
                    response.put("error_message", "Tipo Imagen Incorrecto");
                    throw new IllegalArgumentException("Tipo de archivo no permitido. Solo se permiten imágenes JPEG, JPG, PNG, WEBP.");
                }
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

    @PreAuthorize("hasAuthority('UPDATE_ONE_PRODUCT_DISABLED')")
    @PutMapping("enable/{id}")
    public ResponseEntity<Map<String, Object>> enable(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = this.productImp.findByIdDisabled(id);
            if (product == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Producto no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            Inventory inventory = this.inventoryImp.findByProductDisabled(product);
            if (inventory == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "El producto no se encuentra en el inventario");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                product.setEliminated(false);
                inventory.setEliminated(false);

                this.productImp.update(product);
                this.inventoryImp.update(inventory);

                response.put("status", "success");
                response.put("data", "Habilitado Correctamente");
            }
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
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

    // LIST PRODUCTS - PROVIDER
    @PreAuthorize("hasAuthority('READ_PRODUCTS_BY_PROVIDER')")
    @GetMapping("productsByProvider/{id}")
    public ResponseEntity<Map<String, Object>> findByIdProvider(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> productList = this.productImp.findByIdProvider(id);
            response.put("status", "success");
            response.put("data", productList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
