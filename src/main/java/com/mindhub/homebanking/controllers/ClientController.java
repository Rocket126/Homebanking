package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired//para conectarse automaticamente a otra clase, se crea automaticamente una instancia de clientRepository
    private ClientRepository clientRepository;//y almacena en la variable repo

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/rest/clients")//controlador que encuentra lista de todos los clientes
   public List<ClientDTO> getAll() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @RequestMapping("/clients/{id}")//controlador que busca clientes por id
    public ClientDTO getClientID(@PathVariable Long id)
    {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }


    //CREANDO UN NUEVO CLIENTE
    @Autowired
    private PasswordEncoder passwordEncoder;
    //@RequestMapping(path = "/clients", method = RequestMapping.POST)

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client client1 = clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));

        int n = (int) ((Math.random() * 90000000)+10000000);
        accountRepository.save(new Account("VIN-"+n, LocalDateTime.now(),0.0,client1));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    //DEVUELVE EL CLIENTE AUTENTICADO
    @RequestMapping("/clients/current")
    public ClientDTO getAll(Authentication authentication) {

       Client client = clientRepository.findByEmail(authentication.getName());
       return new ClientDTO(client);

    }


}


