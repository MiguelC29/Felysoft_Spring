package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Payment;
import com.felysoft.felysoftApp.entity.*;
import com.felysoft.felysoftApp.service.EmailSenderService;
import com.felysoft.felysoftApp.service.imp.*;
import com.felysoft.felysoftApp.service.imp.DetailImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sale/")
public class SaleController {
    @Autowired
    private SaleImp saleImp;

    @Autowired
    private PaymentImp paymentImp;

    @Autowired
    private DetailImp detailImp;

    @Autowired
    private InventoryImp inventoryImp;

    @Autowired
    private ProductImp productImp;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserImp userImp;

    @Autowired
    private RoleImp roleImp;

    @PreAuthorize("hasAuthority('READ_ALL_SALES')")
    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Sale> saleList = this.saleImp.findAll();

            response.put("status", "success");
            response.put("data", saleList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_ALL_SALES_DISABLED')")
    @GetMapping("disabled")
    public ResponseEntity<Map<String, Object>> findAllDisabled() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Sale> saleList = this.saleImp.findAllDisabled();
            response.put("status", "success");
            response.put("data", saleList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_ONE_SALE')")
    @GetMapping("list/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Sale sale = this.saleImp.findById(id);

            response.put("status", "success");
            response.put("data", sale);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_ONE_SALE')")
    @PostMapping("create")
    @Transactional
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // TODO: HACER QUE SI DA UN ERRRO HAGA UN ROLLBACK DEL REGISTRO DE PAGOS Y DEMAS
            // INSTANCIA OBJETO PAGO
            Payment payment = Payment.builder()
                    .methodPayment(Payment.MethodPayment.valueOf(request.get("methodPayment").toString().toUpperCase()))
                    .state(Payment.State.valueOf(request.get("state").toString().toUpperCase()))
                    .total(new BigDecimal(request.get("total").toString()))
                    .date(new Timestamp(System.currentTimeMillis()))
                    .build();

            this.paymentImp.create(payment);

            Sale sale = Sale.builder()
                    .dateSale(payment.getDate())
                    .totalSale(payment.getTotal())
                    .payment(payment)
                    .build();

            this.saleImp.create(sale);

            // Registrar los detalles de la compra
            List<Map<String, Object>> detailsRequest = (List<Map<String, Object>>) request.get("details");

            for (Map<String, Object> detailRequest : detailsRequest) {
                if (detailRequest.get("idProduct") != null) {
                    Detail detail = new Detail();
                    // Si es un producto, necesitamos cantidad y precio unitario
                    Product product = productImp.findById(Long.parseLong(detailRequest.get("idProduct").toString()));
                    if (product == null) {
                        response.put("status", HttpStatus.NOT_FOUND);
                        response.put("data", "Producto no encontrado");
                        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                    }
                    var cantidad = Integer.parseInt(detailRequest.get("quantity").toString());
                    detail.setProduct(product);
                    detail.setQuantity(cantidad);

                    Inventory inventory = inventoryImp.findByProduct(product);
                    if (cantidad > inventory.getStock()) {
                        response.put("status", HttpStatus.BAD_GATEWAY);
                        response.put("data", "No hay suficientes productos");
                        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
                    } else {
                        inventory.setStock(inventory.getStock() - cantidad);
                        updateInventoryState(inventory);
                        inventoryImp.update(inventory);

                        // Verificar si el stock se agotó
                        if (inventory.getStock() == 0) {
                            notifyInventoryManagers(inventory, true);
                        } else if (inventory.getStock() == 5 || inventory.getStock() < 2) {
                            notifyInventoryManagers(inventory, false);
                        }
                    }
                    detail.setUnitPrice(new BigDecimal(detailRequest.get("unitPrice").toString()));
                    detail.setEliminated(false);
                    detail.setSale(sale);

                    this.detailImp.create(detail);  // Registrar cada detalle en la base de datos
                }
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

    private void updateInventoryState(Inventory inventory) {
        if (inventory.getStock() <= 0) {
            inventory.setState(Inventory.State.AGOTADO);
        } else if (inventory.getStock() < 6) {
            inventory.setState(Inventory.State.BAJO);
        } else {
            inventory.setState(Inventory.State.DISPONIBLE);
        }
    }

    private void notifyInventoryManagers(Inventory inventory, boolean isOff) {
        Optional<Role> role = roleImp.findByName("INVENTORY_MANAGER");
        if (role.isPresent()) {
            List<User> inventoryManagers = userImp.findByRole(role.get());
            for (User user : inventoryManagers) {
                if (isOff) {
                    sendSoldOutStockEmail(user, inventory.getProduct());
                } else {
                    sendLowStockEmail(user, inventory);
                }
            }
        }
    }

    private void sendLowStockEmail(User user, Inventory inventory) {
        String recipientAddress = user.getEmail();
        String subject = "Stock Bajo | FELYSOFT";
        String confirmationUrl = "https://felysoft-react.vercel.app/compras";

        String message = "<html>" +
                "<head>" +
                "    <style>" +
                "        body {" +
                "            font-family: sans-serif;" +
                "            background-color: #f5f5f5;" +
                "            color: #333;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            text-align: center;" + // Centra el texto en todo el cuerpo
                "        }" +
                "        .email-container {" +
                "            background-color: #f5f5f5;" +
                "            min-height: 100vh;" +
                "            padding: 20px;" +
                "            text-align: center;" + // Centra el texto dentro del contenedor
                "        }" +
                "        .email-content {" +
                "            background-color: #ffffff;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);" +
                "            padding: 30px;" +
                "            text-align: center;" +
                "            max-width: 450px;" +
                "            margin: 0 auto;" +
                "            display: inline-block;" + // Asegura que la tabla no se expanda más allá del contenido
                "        }" +
                "        h1 {" +
                "            font-size: 24px;" +
                "            font-weight: bold;" +
                "            color: rgb(38, 80, 115);" +
                "            margin-bottom: 20px;" +
                "        }" +
                "        h3 {" +
                "            font-size: 18px;" +
                "            margin-bottom: 20px;" +
                "            color: black;" +
                "        }" +
                "        h3 span {" +
                "            font-weight: bold;" +
                "            color: rgb(38, 80, 115);" +
                "        }" +
                "        .logo {" +
                "            width: 100px;" +
                "            height: 100px;" +
                "            margin-bottom: 30px;" +
                "        }" +
                "        .description {" +
                "            line-height: 1.6;" +
                "            margin-bottom: 30px;" +
                "            color: black;" +
                "        }" +
                "        p {" +
                "            color: black;" +
                "        }" +
                "        .button {" +
                "            background-color: rgb(38, 80, 115);" +
                "            color: #fff;" +
                "            padding: 15px 30px;" +
                "            border: none;" +
                "            border-radius: 5px;" +
                "            font-size: 16px;" +
                "            cursor: pointer;" +
                "            text-decoration: none;" +
                "            display: inline-block;" +
                "            transition: background-color 0.3s ease;" +
                "        }" +
                "        .button:hover {" +
                "            background-color: #1a3d5b;" +
                "        }" +
                "        .critical {" +
                "            color: #d9534f;" +  // Rojo para resaltar
                "            font-weight: bold;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <table class=\"email-container\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">" +
                "        <tr>" +
                "            <td align=\"center\" valign=\"top\">" + // Centra el contenido
                "                <table class=\"email-content\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">" +
                "                    <tr>" +
                "                        <td>" +
                "                            <img src=\"https://i.postimg.cc/FznvrwC7/logo.png\" alt=\"Felysoft Logo\" class=\"logo\">" +
                "                            <h1>Stock Bajo</h1>" +
                "                            <h3>Hola, <span>" + user.getNames() + "</span></h3>" +
                "                            <p>El producto <strong>\"" + inventory.getProduct().getName() + "\"</strong> tiene un stock de <strong>" + inventory.getStock() + "</strong> unidades.</p>";
                // Si el stock es menor a 2, añadir un mensaje crítico
                if (inventory.getStock() < 2) {
                    message +=
                            "                <p class=\"critical\">¡Atención! El stock de este producto es crítico. Solo quedan <strong>" + inventory.getStock() + "</strong> unidades. Por favor, reabastezca inmediatamente para evitar el agotamiento.</p>";
                } else {
                    message +=
                            "                <p>Se recomienda reabastecer este producto antes de que se agote.</p>";
                }

        message +=
                "                            <a class=\"button\" href=\"" + confirmationUrl + "\" style=\"color: white;\">Gestionar Productos</a>" +
                "                            <p><strong>Fecha de Notificación:</strong> " + LocalDate.now() + "</p>" +
                "                        </td>" +
                "                    </tr>" +
                "                </table>" +
                "            </td>" +
                "        </tr>" +
                "    </table>" +
                "</body>" +
                "</html>";

        emailSenderService.sendEmail(recipientAddress, subject, message);
    }

    private void sendSoldOutStockEmail(User user, Product product) {
        String recipientAddress = user.getEmail();
        String subject = "Stock agotado | FELYSOFT";
        String confirmationUrl = "https://felysoft-react.vercel.app/compras";

        String message = "<html>" +
                "<head>" +
                "    <style>" +
                "        body {" +
                "            font-family: sans-serif;" +
                "            background-color: #f5f5f5;" +
                "            color: black;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            text-align: center;" + // Centra el texto en todo el cuerpo
                "        }" +
                "        .email-container {" +
                "            background-color: #f5f5f5;" +
                "            min-height: 100vh;" +
                "            padding: 20px;" +
                "            text-align: center;" + // Centra el texto dentro del contenedor
                "        }" +
                "        .email-content {" +
                "            background-color: #ffffff;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);" +
                "            padding: 30px;" +
                "            text-align: center;" +
                "            max-width: 450px;" +
                "            margin: 0 auto;" +
                "            display: inline-block;" + // Asegura que la tabla no se expanda más allá del contenido
                "        }" +
                "        h1 {" +
                "            font-size: 24px;" +
                "            font-weight: bold;" +
                "            color: #961a1a;" +
                "            margin-bottom: 20px;" +
                "        }" +
                "        h3 {" +
                "            font-size: 18px;" +
                "            margin-bottom: 20px;" +
                "            color: black;" +
                "        }" +
                "        h3 span {" +
                "            font-weight: bold;" +
                "            color: #333;" +
                "        }" +
                "        .logo {" +
                "            width: 100px;" +
                "            height: 100px;" +
                "            margin-bottom: 30px;" +
                "        }" +
                "        .description {" +
                "            line-height: 1.6;" +
                "            margin-bottom: 30px;" +
                "            color: black;" +
                "        }" +
                "        p {" +
                "            color: black;" +
                "        }" +
                "        .button {" +
                "            background-color: rgb(38, 80, 115);" +
                "            color: #fff;" +
                "            padding: 15px 30px;" +
                "            border: none;" +
                "            border-radius: 5px;" +
                "            font-size: 16px;" +
                "            cursor: pointer;" +
                "            text-decoration: none;" +
                "            display: inline-block;" +
                "            transition: background-color 0.3s ease;" +
                "        }" +
                "        .button:hover {" +
                "            background-color: #1a3d5b;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <table class=\"email-container\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">" +
                "        <tr>" +
                "            <td align=\"center\" valign=\"top\">" + // Centra el contenido
                "                <table class=\"email-content\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">" +
                "                    <tr>" +
                "                        <td>" +
                "                            <img src=\"https://i.postimg.cc/FznvrwC7/logo.png\" alt=\"Felysoft Logo\" class=\"logo\">" +
                "                            <h1>Stock Agotado</h1>" +
                "                            <h3>Hola, <span>" + user.getNames() + "</span></h3>" +
                "                            <p class=\"description\">El producto <strong>\"" + product.getName() + "\"</strong> ha agotado su stock.</p>" +
                "                            <a class=\"button\" href=\"" + confirmationUrl + "\" style=\"color: white;\">Gestionar Productos</a>" +
                "                            <p><strong>Fecha de Notificación:</strong> " + LocalDate.now() + "</p>" +
                "                        </td>" +
                "                    </tr>" +
                "                </table>" +
                "            </td>" +
                "        </tr>" +
                "    </table>" +
                "</body>" +
                "</html>";

        emailSenderService.sendEmail(recipientAddress, subject, message);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_SALE')")
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Sale sale = this.saleImp.findById(id);

            //FECHA
            // Convertir la cadena de fecha a LocalDateTime con formato específico
            sale.setDateSale(new Timestamp(System.currentTimeMillis()));

            //TOTAL
            sale.setTotalSale(new BigDecimal(request.get("totalSale").toString()));

            //FORÁNEAS
            Payment payment = paymentImp.findById(Long.parseLong(request.get("fkIdPayment").toString()));
            sale.setPayment(payment);

            this.saleImp.update(sale);

            response.put("status", "success");
            response.put("data", "Actualización exitosa");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_SALE_DISABLED')")
    @PutMapping(value = "enable/{id}")
    public ResponseEntity<Map<String, Object>> enable(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Sale sale = this.saleImp.findByIdDisabled(id);
            if (sale == null) {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Venta no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            sale.setEliminated(false);
            saleImp.update(sale);

            response.put("status", "success");
            response.put("data", "Habilitada Correctamente");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DISABLE_ONE_SALE')")
    @PutMapping(value = "delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Sale sale = this.saleImp.findById(id);
            sale.setEliminated(true);

            this.saleImp.delete(sale);

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
