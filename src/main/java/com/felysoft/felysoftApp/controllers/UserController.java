package com.felysoft.felysoftApp.controllers;

import com.felysoft.felysoftApp.entities.Role;
import com.felysoft.felysoftApp.entities.User;
import com.felysoft.felysoftApp.services.imp.RoleImp;
import com.felysoft.felysoftApp.services.imp.UserImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/user/", method = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.HEAD})
@CrossOrigin("http://localhost:8086/api/user/all")
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
    @GetMapping("list/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = this.userImp.findById(id);
            response.put("status", "success");
            response.put("data", user);
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
            //INSTANCIA DEL OBJETO USER
            User user = new User();
            //CAMPOS PROPIOS DE LA TABLA USERS
            user.setNumIdentification(Long.valueOf((Integer) request.get("numIdentification")));
            user.setTypeDoc(User.TypeDoc.valueOf(request.get("typeDoc").toString().toUpperCase()));
            user.setNames(request.get("names").toString().toUpperCase());
            user.setLastNames(request.get("lastNames").toString().toUpperCase());
            user.setAddress(request.get("address").toString().toUpperCase());
            user.setPhoneNumber(Integer.parseInt(request.get("phoneNumber").toString()));
            user.setEmail(request.get("email").toString().toLowerCase());
            user.setGender(User.Gender.valueOf(request.get("gender").toString().toUpperCase()));
            user.setUsername(request.get("username").toString());
            user.setPassword(request.get("password").toString());
            //CONFIGURA LO DE LAS IMAGENES
            if (request.containsKey("image") && request.get("image") != null) {
                user.setImage(request.get("image").toString().getBytes());
            }

            if (request.containsKey("typeImg") && request.get("typeImg") != null) {
                user.setTypeImg(request.get("typeImg").toString());
            }
            // Configurar fechas de creación y actualización
            user.setDateRegister(new Timestamp(System.currentTimeMillis()));
            user.setLastModification(new Timestamp(System.currentTimeMillis()));

            //CAMPOS DE LAS LLAVES FORANEAS
//            Role role = roleImp.findById(Long.parseLong(request.get("fkIdRole").toString()));
//            user.setRole(role);
//            this.userImp.create(user);

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
            User user = this.userImp.findById(id);

            user.setNumIdentification(Long.valueOf((Integer) request.get("numIdentification")));
            user.setTypeDoc(User.TypeDoc.valueOf(request.get("typeDoc").toString().toUpperCase()));
            user.setNames(request.get("names").toString().toUpperCase());
            user.setLastNames(request.get("lastNames").toString().toUpperCase());
            user.setAddress(request.get("address").toString().toUpperCase());
            user.setPhoneNumber(Integer.parseInt(request.get("phoneNumber").toString()));
            user.setEmail(request.get("email").toString().toLowerCase());
            user.setGender(User.Gender.valueOf(request.get("gender").toString().toUpperCase()));
            user.setUsername(request.get("username").toString());
            user.setPassword(request.get("password").toString());
            //CONFIGURA LO DE LAS IMAGENES
            if (request.containsKey("image") && request.get("image") != null) {
                user.setImage(request.get("image").toString().getBytes());
            }

            if (request.containsKey("typeImg") && request.get("typeImg") != null) {
                user.setTypeImg(request.get("typeImg").toString());
            }
            // Configurar fechas de creación y actualización
            user.setDateRegister(new Timestamp(System.currentTimeMillis()));
            user.setLastModification(new Timestamp(System.currentTimeMillis()));
//            //CAMPOS DE LAS LLAVES FORANEAS
//            Role role = roleImp.findById(Long.parseLong(request.get("fkIdRole").toString()));
//            user.setRole(role);
//            this.userImp.update(user);

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
            User user = this.userImp.findById(id);
            user.setEliminated(true);
            this.userImp.delete(user);

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
