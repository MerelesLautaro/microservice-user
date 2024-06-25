package com.lautadev.microservice_user.controller;

import com.lautadev.microservice_user.dto.BenefitDTO;
import com.lautadev.microservice_user.dto.UserDTO;
import com.lautadev.microservice_user.model.User;
import com.lautadev.microservice_user.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private IUserService userServ;

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@Valid @RequestBody User user){
        userServ.saveUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestParam String email, @RequestParam String password){
        User user = userServ.loginUser(email, password);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok(userServ.getUsers());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<User> findUser(@PathVariable Long id){
        User user = userServ.findUser(id);

        if(user == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(user);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<UserDTO> findUserAndBenefit(@PathVariable Long id){
        return ResponseEntity.ok(userServ.findUserAndBenefit(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userServ.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<User> editUser(@PathVariable Long id,@Valid @RequestBody User user){
        userServ.editUser(id,user);
        return ResponseEntity.ok(userServ.findUser(user.getIdUser()));
    }

    @PostMapping("/updateTickets/{id}")
    public ResponseEntity<User> updateTickets(@PathVariable Long id, @RequestBody int ticket,
                                              @RequestHeader (value = "X-HTTP-Method-Override")
                                              String methodOverride){
        if ("PATCH".equalsIgnoreCase(methodOverride)){
            userServ.updateTickets(id,ticket,methodOverride);
            User user = userServ.findUser(id);
            if(user == null) ResponseEntity.notFound().build();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
    }

    @PostMapping("/assignBenefit/{id}")
    public ResponseEntity<UserDTO> updateTickets(@PathVariable Long id, @RequestBody Long idBenefit,
                                                 @RequestHeader (value = "X-HTTP-Method-Override")
                                                 String methodOverride){
        if("PATCH".equalsIgnoreCase(methodOverride)){
            userServ.assignBenefit(id,idBenefit,methodOverride);
            return ResponseEntity.ok(userServ.findUserAndBenefit(id));
        } else {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
    }
}
