package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.entity.Book;
import com.felysoft.felysoftApp.entity.Reserve;
import com.felysoft.felysoftApp.entity.User;
import com.felysoft.felysoftApp.service.imp.BookImp;
import com.felysoft.felysoftApp.service.imp.ReserveImp;
import com.felysoft.felysoftApp.service.imp.UserImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/reserve/", method = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
public class ReserveController {
    @Autowired
    private ReserveImp reserveImp;

    @Autowired
    private BookImp bookImp;

    @Autowired
    private UserImp userImp;

    @PreAuthorize("hasAuthority('READ_ALL_RESERVES')")
    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll(){
        Map<String,Object> response= new HashMap<>();
        try{
            List<Reserve> reserveList= this.reserveImp.findAll();

            response.put("status","success");
            response.put("data", reserveList);
        }catch (Exception e){
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_ONE_RESERVE')")
    @GetMapping("list/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Reserve reserve = this.reserveImp.findById(id);

            response.put("status", "success");
            response.put("data", reserve);
        } catch (Exception e) {
            response.put("status", HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_ONE_RESERVE')")
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String,Object> request){
        Map<String,Object> response= new HashMap<>();
        try{
            //INSTANCIA DEL OBJETO RESERVE
            Reserve reserve = Reserve.builder()
                    .dateReserve(LocalDate.parse((String) request.get("dateReserve")))
                    .description(request.get("description").toString().toUpperCase())
                    .deposit(new BigDecimal(request.get("deposit").toString()))
                    .time(Time.valueOf(request.get("time").toString()))
                    .book(bookImp.findById(Long.parseLong(request.get("fkIdBook").toString())))
                    .user(userImp.findById(Long.parseLong(request.get("fkIdUser").toString())))
                    .build();

            this.reserveImp.create(reserve);

            response.put("status","success");
            response.put("data","Registro Exitoso");
        }catch (Exception e){
            response.put("status",HttpStatus.BAD_GATEWAY);
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
            reserve.setTime(Time.valueOf(request.get("time").toString()));

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
