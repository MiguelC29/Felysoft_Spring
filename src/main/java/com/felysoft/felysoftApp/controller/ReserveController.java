package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.*;
import com.felysoft.felysoftApp.service.EmailSenderService;
import com.felysoft.felysoftApp.service.imp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/reserve/")
public class ReserveController {

    @Autowired
    private ReserveImp reserveImp;

    @Autowired
    private InventoryImp inventoryImp;
    @Autowired
    private BookImp bookImp;

    @Autowired
    private UserImp userImp;
    @Autowired
    private RoleImp roleImp;

    @Autowired
    private EmailSenderService emailSenderService;

    @PreAuthorize("hasAuthority('READ_ALL_RESERVES')")
    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Reserve> reserveList = reserveImp.findAll();
            response.put("status", "success");
            response.put("data", reserveList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('READ_ALL_RESERVES_DISABLED')")
    @GetMapping("disabled")
    public ResponseEntity<Map<String, Object>> findAllDisabled() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Reserve> reserveList = reserveImp.findAllDisabled();
            response.put("status", "success");
            response.put("data", reserveList);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('READ_OWN_RESERVES')")
    @GetMapping("reservesByUser")
    public ResponseEntity<Map<String, Object>> findReservesByUser() {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Optional<User> userOptional = userImp.findByEmail(email);
            if (userOptional.isPresent()) {
                List<Reserve> reserveList = reserveImp.findReservesByUser(userOptional.get());
                response.put("status", "success");
                response.put("data", reserveList);
            } else {
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Usuario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('CANCEL_OWN_RESERVE')")
    @PutMapping("cancel/{id}")
    public ResponseEntity<Map<String, Object>> cancel(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Reserve reserve = this.reserveImp.findById(id);

            reserve.setState(Reserve.State.CANCELADA);
            this.reserveImp.cancel(reserve);

            Inventory inventory= this.inventoryImp.findByBook(reserve.getBook());
            if(inventory == null){
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Libro en el Inventario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }else{
                inventory.setState(Inventory.State.DISPONIBLE);
                this.inventoryImp.update(inventory);

            }
            User customer = getCustomer();
            if (customer != null){
                sendCancelReserveEmailCustomer(customer,reserve);
            }
            notifySalepersonCancelReserve(reserve);

            response.put("status", "success");
            response.put("data", "Reserva Cancelada correctamente");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    private void notifySalepersonCancelReserve(Reserve reserve){
        Optional<Role> role = roleImp.findByName("SALESPERSON");
        if (role.isPresent()) {
            List<User> salePersons = userImp.findByRole(role.get());
            for (User user : salePersons) {
                sendCancelReserveEmail(user, reserve);
            }
        }
    }
    private void  sendCancelReserveEmail(User user ,Reserve reserve){
        String recipientAddress = user.getEmail();
        String subject ="Cancelación de Reserva de Libros | FELYSOFT";
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
                .append("<h1>Se Canceló una Reserva</h1>")
                .append("<h3>Hola, <span>" + user.getNames() + "</span></h3>")
                .append("<p>Se cancela la Reserva con los siguientes Datos:</p>")
                .append("<table>")
                .append("<tr><th>Libro</th><th>Fecha de Reserva</th><th>Duración</th><th>Precio</th><th>Usuario</th><th>Estado</th></tr>");

        String nameUser= reserve.getUser().getNames() + ' ' + reserve.getUser().getLastNames();

        String duration =reserve.getTime() + " " + (reserve.getTime() == 1 ? "hora" : "horas");

        messageBuilder.append("<tr>")
                .append("<td>").append(reserve.getBook().getTitle()).append("</td>")
                .append("<td>").append(reserve.getDateReserve()).append("</td>")
                .append("<td>").append(duration).append("</td>")
                .append("<td>").append(reserve.getDeposit()).append("</td>")
                .append("<td>").append(nameUser).append("</td>")
                .append("<td>").append(reserve.getState()).append("</td>")
                .append("</tr>");

        messageBuilder.append("</table></div></body></html>");

        String message = messageBuilder.toString();

        emailSenderService.sendEmail(recipientAddress, subject, message);
    }
    private void  sendCancelReserveEmailCustomer(User user ,Reserve reserve){
        String recipientAddress = user.getEmail();
        String subject ="Cancelaste una Reserva de Libros | FELYSOFT";
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
                .append("<h1>Creaste una nueva Reserva</h1>")
                .append("<h3>Hola, <span>" + user.getNames() + "</span></h3>")
                .append("<p>Cancelaste una reserva con los siguientes Datos:</p>")
                .append("<table>")
                .append("<tr><th>Libro</th><th>Fecha de Reserva</th><th>Duración</th><th>Precio</th></tr>");

        String duration =reserve.getTime() + " " + (reserve.getTime() == 1 ? "hora" : "horas");

        messageBuilder.append("<tr>")
                .append("<td>").append(reserve.getBook().getTitle()).append("</td>")
                .append("<td>").append(reserve.getDateReserve()).append("</td>")
                .append("<td>").append(duration).append("</td>")
                .append("<td>").append(reserve.getDeposit()).append("</td>")
                .append("</tr>");

        messageBuilder.append("</table></div></body></html>");

        String message = messageBuilder.toString();

        emailSenderService.sendEmail(recipientAddress, subject, message);
    }
    @PreAuthorize("hasAuthority('CHANGE_STATE_RESERVE')")
    @PutMapping("changeState/{id}")
    public ResponseEntity<Map<String, Object>> changeState(@PathVariable Long id, @RequestBody Map<String,Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Reserve reserve = this.reserveImp.findById(id);

            reserve.setState(Reserve.State.valueOf(request.get("state").toString()));
            this.reserveImp.update(reserve);

            Inventory inventory= this.inventoryImp.findByBook(reserve.getBook());
            if(inventory == null){
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Libro en el Inventario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }else{
                inventory.setState(Inventory.State.DISPONIBLE);
                this.inventoryImp.update(inventory);

            }
            var message = (reserve.getState()== Reserve.State.CANCELADA) ? "Cancelada": "Finalizada";

            response.put("status", "success");
            response.put("data", "La reserva ha sido %s correctamente".formatted(message));
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_ONE_RESERVE')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Reserve reserve = Reserve.builder()
                    .dateReserve(LocalDate.parse(request.get("dateReserve").toString()))
                    .description(request.get("description").toString().toUpperCase())
                    .deposit(new BigDecimal(request.get("deposit").toString()))
                    .time(Integer.parseInt(request.get("time").toString()))
                    .book(bookImp.findById(Long.parseLong(request.get("fkIdBook").toString())))
                    .user(userImp.findById(Long.parseLong(request.get("fkIdUser").toString())))
                    .state(Reserve.State.RESERVADA)
                    .eliminated(false)
                    .build();

            reserveImp.create(reserve);

            Inventory inventory= this.inventoryImp.findByBook(reserve.getBook());
            if(inventory == null){
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Inventario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }else{
                inventory.setState(Inventory.State.RESERVADO);
                this.inventoryImp.update(inventory);

            }

           User customer = getCustomer();
            if (customer != null){
                sendNewReserveEmailCustomer(customer,reserve);
            }
            notifySalepersonReserve(reserve);

            response.put("status", "success");
            response.put("data", "Registro Exitoso");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private User getCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> userOptional = userImp.findByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            return null;
        }
    }

    private void notifySalepersonReserve(Reserve reserve){
        Optional<Role> role = roleImp.findByName("SALESPERSON");
        if (role.isPresent()) {
            List<User> salePersons = userImp.findByRole(role.get());
            for (User user : salePersons) {
                sendNewReserveEmail(user, reserve);
            }
        }
    }
    private void  sendNewReserveEmail(User user ,Reserve reserve){
        String recipientAddress = user.getEmail();
        String subject ="Nueva Reserva de Libros | FELYSOFT";
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
                .append("<h1>Nueva Reserva</h1>")
                .append("<h3>Hola, <span>" + user.getNames() + "</span></h3>")
                .append("<p>Se realizo la Reserva con los siguientes Datos:</p>")
                .append("<table>")
                .append("<tr><th>Libro</th><th>Fecha de Reserva</th><th>Duración</th><th>Precio</th><th>Usuario</th></tr>");

            String nameUser= reserve.getUser().getNames() + ' ' + reserve.getUser().getLastNames();

            String duration =reserve.getTime() + " " + (reserve.getTime() == 1 ? "hora" : "horas");

            messageBuilder.append("<tr>")
                    .append("<td>").append(reserve.getBook().getTitle()).append("</td>")
                    .append("<td>").append(reserve.getDateReserve()).append("</td>")
                    .append("<td>").append(duration).append("</td>")
                    .append("<td>").append(reserve.getDeposit()).append("</td>")
                    .append("<td>").append(nameUser).append("</td>")
                    .append("</tr>");

        messageBuilder.append("</table></div></body></html>");

        String message = messageBuilder.toString();

        emailSenderService.sendEmail(recipientAddress, subject, message);
    }
    private void  sendNewReserveEmailCustomer(User user ,Reserve reserve){
        String recipientAddress = user.getEmail();
        String subject ="Nueva Reserva de Libros | FELYSOFT";
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
                .append("<h1>Creaste una nueva Reserva</h1>")
                .append("<h3>Hola, <span>" + user.getNames() + "</span></h3>")
                .append("<p>Realizaste una reserva con los siguientes Datos:</p>")
                .append("<table>")
                .append("<tr><th>Libro</th><th>Fecha de Reserva</th><th>Duración</th><th>Precio</th></tr>");

        String duration =reserve.getTime() + " " + (reserve.getTime() == 1 ? "hora" : "horas");

        messageBuilder.append("<tr>")
                .append("<td>").append(reserve.getBook().getTitle()).append("</td>")
                .append("<td>").append(reserve.getDateReserve()).append("</td>")
                .append("<td>").append(duration).append("</td>")
                .append("<td>").append(reserve.getDeposit()).append("</td>")
                .append("</tr>");

        messageBuilder.append("</table></div></body></html>");

        String message = messageBuilder.toString();

        emailSenderService.sendEmail(recipientAddress, subject, message);
    }

    @PreAuthorize("hasAuthority('UPDATE_ONE_RESERVE')")
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Reserve reserve = this.reserveImp.findById(id);

            reserve.setDateReserve(LocalDate.parse((String) request.get("dateReserve")));
            reserve.setDescription(request.get("description").toString().toUpperCase());
            reserve.setDeposit(new BigDecimal(request.get("deposit").toString()));
            reserve.setTime(Integer.parseInt(request.get("time").toString()));

            //CAMPOS DE LAS LLAVES FORANEAS
            Book book = bookImp.findById(Long.parseLong(request.get("fkIdBook").toString()));
            User user = userImp.findById(Long.parseLong(request.get("fkIdUser").toString()));

            reserve.setBook(book);
            reserve.setUser(user);

            this.reserveImp.update(reserve);

            response.put("status", "success");
            response.put("data", "Actualización exitosa");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('UPDATE_ONE_RESERVE_DISABLED')")
    @PutMapping("enable/{id}")
    public ResponseEntity<Map<String, Object>> enable(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Reserve reserve = this.reserveImp.findByIdDisabled(id);

            if (reserve == null){
                response.put("status", HttpStatus.NOT_FOUND);
                response.put("data", "Reserva no encontrada");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                reserve.setEliminated(false);

                this.reserveImp.update(reserve);

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

    @PreAuthorize("hasAuthority('DISABLE_ONE_RESERVE')")
    @PutMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Reserve reserve = this.reserveImp.findById(id);
            reserve.setEliminated(true);

            this.reserveImp.delete(reserve);

            response.put("status", "success");
            response.put("data", "Eliminado Correctamente");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Scheduled(cron = "0 0 8 ? * 2-7") // Se ejecuta todos los días a las 8 AM de lunes a sábado
    //@Scheduled(cron = "0 * * * * ?") // Ejecutar cada minuto para pruebas
    public void sendDayReserveNotify() throws Exception {
        Optional<Role> role = roleImp.findByName("CUSTOMER");

        if (role.isPresent()) {
            List<User> customer = userImp.findByRole(role.get());
            LocalDate today = LocalDate.now();
            if (customer != null) {
                for (User user : customer) {
                    List<Reserve> reserveList = reserveImp.findReservesByUserActive(user, Reserve.State.RESERVADA);
                    for (Reserve reserve : reserveList) {
                        if (reserve.getDateReserve() != null && reserve.getDateReserve().isEqual(today)) {
                            sendDayReserveNotification(user, reserve);
                        }
                    }
                }
            }
        }
    }
    private void sendDayReserveNotification(User user , Reserve reserve) {
        String recipientAddress = user.getEmail();
        String subject = "Recordatorio de Reserva de Libro | FELYSOFT";
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
                .append("<h1>Hoy es tu Reserva</h1>")
                .append("<h3>Hola, <span>" + user.getNames() + "</span></h3>")
                .append("<p>Hoy tienes la siguiente reserva:</p>")
                .append("<table>")
                .append("<tr><th>Libro</th><th>Fecha de Reserva</th><th>Duración</th><th>Precio</th></tr>");


            String duration =reserve.getTime() + " " + (reserve.getTime() == 1 ? "hora" : "horas");

            messageBuilder.append("<tr>")
                    .append("<td>").append(reserve.getBook().getTitle()).append("</td>")
                    .append("<td>").append(reserve.getDateReserve()).append("</td>")
                    .append("<td>").append(duration).append("</td>")
                    .append("<td>").append(reserve.getDeposit()).append("</td>")
                    .append("</tr>");


        messageBuilder.append("</table>")
                .append("<p>Por favor, Recuerda los horarios para realizar tu reserva.</p>")
                .append("<p> Es de 9:40 am a 4:00 pm, Gracias.</p>")
                .append("<p> Dirección: Carrera 96B #25B - 58.</p>")
                .append("</div></body></html>");

        String message = messageBuilder.toString();

        emailSenderService.sendEmail(recipientAddress, subject, message);
    }
}
