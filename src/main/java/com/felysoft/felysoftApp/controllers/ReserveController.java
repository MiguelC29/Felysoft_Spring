package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.Book;
import com.felysoft.felysoftApp.entities.Reserve;
import com.felysoft.felysoftApp.entities.User;
import com.felysoft.felysoftApp.services.imp.BookImp;
import com.felysoft.felysoftApp.services.imp.ReserveImp;
import com.felysoft.felysoftApp.services.imp.UserImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.parse;

@RestController
@RequestMapping(path = "/api/reserve/", method = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.HEAD})
@CrossOrigin("*")
public class ReserveController {
    @Autowired
    private ReserveImp reserveImp;
    @Autowired
    private BookImp bookImp;
    @Autowired
    private UserImp userImp;
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
    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String,Object> request){
        Map<String,Object> response= new HashMap<>();

        try{
            //INSTANCIA DEL OBJETO RESERVE
            Reserve reserve = new Reserve();
            //CAMPOS PROPIOS DE LA TABLA RESERVES
            reserve.setDateReserve(LocalDate.from(parse((String) request.get("dateReserve"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            reserve.setDescription(request.get("description").toString());
            reserve.setDeposit(new BigDecimal(request.get("deposit").toString()));
            reserve.setTime(Time.valueOf(request.get("time").toString()));

            //CAMPOS DE LAS LLAVES FORANEAS
            Book book = bookImp.findById(Long.parseLong(request.get("fkIdBook").toString()));
            reserve.setBook(book);
            User user = userImp.findById(Long.parseLong(request.get("fkIdUser").toString()));
            reserve.setUser(user);
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
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Reserve reserve = this.reserveImp.findById(id);

            reserve.setDateReserve(LocalDate.from(parse((String) request.get("dateReserve"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            reserve.setDescription(request.get("description").toString());
            reserve.setDeposit(new BigDecimal(request.get("deposit").toString()));
            reserve.setTime(Time.valueOf(request.get("time").toString()));

            //CAMPOS DE LAS LLAVES FORANEAS
            Book book = bookImp.findById(Long.parseLong(request.get("fkIdBook").toString()));
            reserve.setBook(book);
            User user = userImp.findById(Long.parseLong(request.get("fkIdUser").toString()));
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
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Reserve reserve = this.reserveImp.findById(id);
            reserveImp.delete(reserve);

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
