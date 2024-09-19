package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.dto.AuthenticationRequest;
import com.felysoft.felysoftApp.entity.*;
import com.felysoft.felysoftApp.service.EmailSenderService;
import com.felysoft.felysoftApp.service.imp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    private BrandImp brandImp;

    @Autowired
    private InventoryImp inventoryImp;

    @Autowired
    private EmailSenderService emailSenderService; // Para enviar los correos

    @Autowired
    private RoleImp roleImp;

    @Autowired
    private UserImp userImp;

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
            @RequestParam("category") Long categoryId,
            @RequestParam("provider") Long providerId,
            @RequestParam("brand") Long brandId,
            @RequestParam(value = "stockInicial", required = false) Integer stockInicial,
            @RequestParam("image") MultipartFile image) {
        Map<String, Object> response = new HashMap<>();
        try {
            final boolean isAdmin = AuthenticationRequest.isAdmin();

            Product productByName = this.productImp.findProductByName(name.toUpperCase());

            if (productByName != null) {
                if (productByName.isEliminated()) {
                    response.put("data","Datos Desahibilitados");

                    // Verifica si el rol es de administrador
                    String message = (isAdmin) ? "Información ya registrada pero desahibilitada" : "Información ya registrada pero desahibilitada; Contacte al Administrador";

                    response.put("detail", message);
                } else {
                    response.put("status",HttpStatus.BAD_GATEWAY);
                    response.put("data","El producto ya existe");
                }

                return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
            } else {
                // Construir el objeto Product usando el patrón Builder
                Product.ProductBuilder productBuilder = Product.builder()
                        .name(name.toUpperCase())
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
                Brand brand = brandImp.findById(brandId);

                Product product = productBuilder
                        .category(category)
                        .provider(provider)
                        .brand(brand)
                        .build();

                // Verificar si `stockInicial` no es null
                if (stockInicial != null) {
                    // Construir el objeto Inventory usando el patrón Builder
                    Inventory inventory = Inventory.builder()
                            .stock(stockInicial)
                            .state((stockInicial < 6 ? Inventory.State.BAJO : Inventory.State.DISPONIBLE)) //TODO: falta que desde frontend no se deje ingresa un stock menor o igual a 0
                            .typeInv(Inventory.TypeInv.PRODUCTOS)
                            .dateRegister(new Timestamp(System.currentTimeMillis()))
                            .lastModification(new Timestamp(System.currentTimeMillis()))
                            .product(product)
                            .build();

                    this.productImp.create(product); // Guardar producto
                    this.inventoryImp.create(inventory); // Guardar inventario
                } else {
                    // Si no hay stock inicial, solo se guarda el producto
                    this.productImp.create(product);
                }

                // Respuesta de éxito
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
                    response.put("data", "Marca no encontrado");
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
                product.setBrand(brand);
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
            if (inventory != null) {
                //response.put("status", HttpStatus.NOT_FOUND);
                //response.put("data", "El producto no se encuentra en el inventario");
                //return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                inventory.setEliminated(false);

                this.inventoryImp.update(inventory);
            }
            product.setEliminated(false);

            this.productImp.update(product);

            response.put("status", "success");
            response.put("data", "Habilitado Correctamente");
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
            if (inventory != null) {
                //response.put("status", HttpStatus.NOT_FOUND);
                //response.put("data", "El producto no se encuentra en el inventario");
                //return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                inventory.setEliminated(true);

                this.inventoryImp.delete(inventory);
            }
            product.setEliminated(true);
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

    @Scheduled(cron = "0 0 8 ? * 2-7") // Se ejecuta todos los días a las 8 AM de lunes a sábado
    //@Scheduled(cron = "0 * * * * ?") // Ejecutar cada minuto para pruebas
    //@Scheduled(cron = "0 */5 * * * ?") // Ejecutar cada 5 minutos
    public void checkExpiringProducts() throws Exception {
        List<Product> products = this.productImp.findAll();

        // Configura el umbral para productos próximos a vencerse (por ejemplo, 7 días antes de la fecha de vencimiento)
        int thresholdDays = 7;
        LocalDate today = LocalDate.now();

        // Usamos un Map para almacenar productos próximos a vencerse
        Map<Product, Long> expiringProducts = new HashMap<>();

        for (Product product : products) {
            if (product.getExpiryDate() != null) {
                Inventory inventory = inventoryImp.findByProduct(product);
                if (inventory != null && inventory.getStock() > 0) {
                    // Verificar si la fecha de vencimiento está dentro del rango de próximos a vencer
                    LocalDate expirationDate = product.getExpiryDate().toLocalDate();
                    long daysToExpire = ChronoUnit.DAYS.between(today, expirationDate);

                    // Verificar si el producto está próximo a vencer dentro del umbral
                    if (daysToExpire <= thresholdDays && daysToExpire >= 0) {
                        // Enviar correo de aviso sobre el producto próximo a vencerse
                        //notifyInventoryManagers(inventory, daysToExpire);

                        // Almacena el producto con su número de días restantes
                        expiringProducts.put(product, daysToExpire);
                    }
                }
            }
        }

        if (!expiringProducts.isEmpty()) {
            // Enviar correo con los productos próximos a vencer
            notifyInventoryManagers(expiringProducts);
        }
    }

    private void notifyInventoryManagers(Map<Product, Long> expiringProducts) {
        Optional<Role> role = roleImp.findByName("INVENTORY_MANAGER");
        if (role.isPresent()) {
            List<User> inventoryManagers = userImp.findByRole(role.get());
            for (User user : inventoryManagers) {
                sendExpiringProductsNotification(user, expiringProducts);
            }
        }
    }

    private void sendExpiringProductsNotification(User user, Map<Product, Long> expiringProducts) {
        String recipientAddress = user.getEmail();
        String subject = "Productos Próximos a Vencerse | FELYSOFT";
        StringBuilder messageBuilder = new StringBuilder();

        messageBuilder.append("<html><head><style>")
                .append("body { font-family: sans-serif; background-color: #f5f5f5; color: black; margin: 0; padding: 0; }")
                .append(".email-container { background-color: #ffffff; padding: 20px; border-radius: 8px; margin: 0 auto; max-width: 600px; text-align: center; }") // Centra todo el contenido dentro del contenedor
                .append("table { width: 100%; border-collapse: collapse; margin: 0 auto; }") // Centra la tabla dentro del contenedor
                .append("th, td { padding: 10px; color: black; text-align: center; border: 1px solid #ddd; }") // Centra el contenido de las celdas
                .append("th { background-color: rgb(38, 80, 115); color: #ffffff; }") // Cambia el color del encabezado de la tabla
                .append(".logo { display: block; width: 100px; height: 100px; margin: 0 auto 30px; }") // Centra el logo
                .append("h1, h3, p { color: black; }") // Cambiar color a negro
                .append("h3 span {font-weight: bold; color: rgb(38, 80, 115);}") // Cambiar color a negro
                .append("</style></head><body>")
                .append("<div class='email-container'>")
                .append("<img src=\"https://i.postimg.cc/FznvrwC7/logo.png\" alt=\"Felysoft Logo\" class=\"logo\">")
                .append("<h1>Productos Próximos a Vencerse</h1>")
                .append("<h3>Hola, <span>" + user.getNames() + "</span></h3>")
                .append("<p>A continuación se muestra la lista de productos que están próximos a vencerse:</p>")
                .append("<table>")
                .append("<tr><th>Nombre del Producto</th><th>Fecha de Vencimiento</th><th>Días Restantes</th></tr>");

        for (Map.Entry<Product, Long> entry : expiringProducts.entrySet()) {
            Product product = entry.getKey();
            long daysToExpire = entry.getValue();
            LocalDate expirationDate = product.getExpiryDate().toLocalDate();

            messageBuilder.append("<tr>")
                    .append("<td>").append(product.getName()).append("</td>")
                    .append("<td>").append(expirationDate).append("</td>")
                    .append("<td>").append(daysToExpire).append("</td>")
                    .append("</tr>");
        }

        messageBuilder.append("</table>")
                .append("<p>Por favor, tome las acciones necesarias.</p>")
                .append("</div></body></html>");

        String message = messageBuilder.toString();

        emailSenderService.sendEmail(recipientAddress, subject, message);
    }
}
