package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.Book;
import com.felysoft.felysoftApp.entities.Reserve;
import com.felysoft.felysoftApp.entities.Role;
import com.felysoft.felysoftApp.entities.User;
import com.felysoft.felysoftApp.services.imp.ReserveImp;
import com.felysoft.felysoftApp.services.imp.RoleImp;
import com.felysoft.felysoftApp.services.imp.UserImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/user/", method = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.HEAD})
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserImp userImp;
    @Autowired
    private RoleImp roleImp;
    @GetMapping("all")
    public ResponseEntity<Map<String, Object>> findAll(){
        Map<String,Object> response= new HashMap<>();

        try{
            List<User> userList= this.userImp.findAll();
            response.put("status","success");
            response.put("data", userList);
        }catch (Exception e){
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
            //INSTANCIA DEL OBJETO USER
            User user = new User();
            //CAMPOS PROPIOS DE LA TABLA USERS
            user.setNumIdentification((Long) request.get("numIdentification"));
            user.setTypeDoc(request.get("typeDoc").toString());
            user.setNames(request.get("names").toString());
            user.setLastNames(request.get("lastNames").toString());
            user.setAddress(request.get("address").toString());
            user.setPhoneNumber(Integer.parseInt(request.get("phoneNumber").toString()));
            user.setEmail(request.get("email").toString());
            user.setGender(request.get("gender").toString());
            user.setUsername(request.get("username").toString());
            user.setPassword(request.get("password").toString());
            user.setImage(request.get("image").toString().getBytes());
            user.setTypeImg(request.get("typeImg").toString());
            // Configurar fechas de creación y actualización
            user.setDateRegister(new Timestamp(System.currentTimeMillis()));
            user.setLastModification(new Timestamp(System.currentTimeMillis()));
            //CAMPOS DE LAS LLAVES FORANEAS

            Role role = roleImp.findById(Long.parseLong(request.get("fkIdRole").toString()));
            user.setRole(role);
            this.userImp.create(user);

            response.put("status","success");
            response.put("data","Registro Exitoso");
        }catch (Exception e){
            response.put("status",HttpStatus.BAD_GATEWAY);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
