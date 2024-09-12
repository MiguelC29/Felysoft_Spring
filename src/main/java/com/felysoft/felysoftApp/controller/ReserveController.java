package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Book;
import com.felysoft.felysoftApp.entity.Inventory;
import com.felysoft.felysoftApp.entity.Reserve;
import com.felysoft.felysoftApp.entity.User;
import com.felysoft.felysoftApp.service.imp.BookImp;
import com.felysoft.felysoftApp.service.imp.InventoryImp;
import com.felysoft.felysoftApp.service.imp.ReserveImp;
import com.felysoft.felysoftApp.service.imp.UserImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            response.put("status", "success");
            response.put("data", "Reserva Cancelada correctamente");
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

            response.put("status", "success");
            response.put("data", "Registro Exitoso");
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
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
}
