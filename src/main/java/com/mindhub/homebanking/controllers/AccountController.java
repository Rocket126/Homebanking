package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;

import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired//para conectarse automaticamente a otra clase, se crea automaticamente una instancia de AccountRepository
    private AccountRepository accountRepository;//y almacena en la variable accountRepository

    @RequestMapping("/accounts")//controlador que encuentra lista de cuentas (/api/accounts)
    //requestmapping agrega LA URL al despachador(dispatcher)
    public List<AccountDTO> getAll() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @RequestMapping("/accounts/{id}")//controlador que busca cuentas por id
    public AccountDTO getAccountID(@PathVariable Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }



    //CREACION  DE CUENTA PARA UN CLIENTE. RUTA
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;
    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> register(Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());

        int cantidadCuentas = client.getAccounts().size();
        if (cantidadCuentas >= 3) {
            return new ResponseEntity<>("El cliente no puede poseer mas de 3 cuentas", HttpStatus.FORBIDDEN);
        }


        int n = (int) ((Math.random() * 90000000)+10000000);
        accountRepository.save(new Account("VIN-"+n, LocalDateTime.now(), 0.0 , client));

        return new ResponseEntity<>(HttpStatus.CREATED);
        }


        //BUSCO Y RETORNO TODAS LAS CUENTAS DEL CLIENTE QUE ESTA EN SESSION
        @GetMapping("/clients/current/accounts")
        public List<AccountDTO> getAccounts(Authentication authentication) {
            Client client = this.clientRepository.findByEmail(authentication.getName());
            return client.getAccounts().stream().map(AccountDTO::new).collect(toList());
        }

}
