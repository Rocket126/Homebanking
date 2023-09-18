package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientRepository clientRepository;
    //Para conectarse automaticamente a otra clase se crea automaticamente una instancia de clientRepository, y almacena
    //las propiades y metodos de Client


    @Transactional
    @PostMapping("/transactions")//clients/current/
    public ResponseEntity<Object> register(Authentication authentication,
            @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
           @RequestParam Double amount, @RequestParam String description) {

        if (amount.isNaN() || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Faltan campos para la transferencia", HttpStatus.FORBIDDEN);
        }

       //Verificar que los números de cuenta no sean iguales
      if(fromAccountNumber.equals(toAccountNumber))
      {
          return new ResponseEntity<>("Las cuentas a transferir no deben ser iguales", HttpStatus.FORBIDDEN);
      }

      //Verificar que exista la cuenta de origen
        Account cuentaOrigen1 = accountRepository.findByNumber(fromAccountNumber);
        if ( cuentaOrigen1 == null) {//VIN-0001
            return new ResponseEntity<>("La cuenta de ORIGEN no existe", HttpStatus.FORBIDDEN);
        }

       //Verificar que exista la cuenta de destino
        Account cuentaDestino2 = accountRepository.findByNumber(toAccountNumber);
       if (cuentaDestino2 == null) {//VIN-0001
           return new ResponseEntity<>("La cuenta de DESTINO no existe", HttpStatus.FORBIDDEN);
       }

       //Verificar que la cuenta de origen tenga el monto disponible.
       Double monto = accountRepository.findByNumber(fromAccountNumber).getBalance();
       if (monto == null || monto == 0 ) {
           return new ResponseEntity<>("La cuenta de origen no tiene monto disponible para transferir", HttpStatus.FORBIDDEN);
       }

        //Verificar que el valor pasado por el usuario para que sea menor al almacenado en la BD
        monto = accountRepository.findByNumber(fromAccountNumber).getBalance();
       if(amount > monto)
        {
            return new ResponseEntity<>("El monto a transferir no debe sobrepasar al monto de la cuenta", HttpStatus.FORBIDDEN);
        }

        //si el valor pasado es menor a cero "negativo", no puede transferir
        if(amount <= 0)
        {
            return new ResponseEntity<>("El monto no debe menor al de la cuenta", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de origen pertenezca al cliente autenticado
        Client client1 = clientRepository.findByEmail(authentication.getName());

        //la variable cuentaOrigen1 esta creada despues de la validacion vacias o null de parametros
        boolean cuentaDueno = client1.getAccounts().contains(cuentaOrigen1);
        if(cuentaDueno == false)
        {
            return new ResponseEntity<>("La cuenta no pertenece al usuario", HttpStatus.FORBIDDEN);
        }
        //long a = cuentaOrigenC.getOwner().getId();//obtengo el id del usuario por medio de la cuenta
        //long b = client1.getId();//obtengo el id del usuario actual por medio de client
        //si los id son distintos la cuenta no pertenece al cliente autenticado, ya que una cuenta
        //esta sujeta a un id de un cliente


       //**********************************************************************
      //A la cuenta de origen se le restará el monto indicado en la petición
       // y a la cuenta de destino se le sumará el mismo monto.

       Double montoActualParaOrigen =  accountRepository.findByNumber(fromAccountNumber).getBalance() - amount;
       Account cuentaOrigen = accountRepository.findByNumber(fromAccountNumber);
       cuentaOrigen.setBalance(montoActualParaOrigen);


       //se le agrega un monto nuevo a la cuenta de destino
       Double montoParaDestino =  accountRepository.findByNumber(toAccountNumber).getBalance() + amount;
       Account cuentaDestino = accountRepository.findByNumber(toAccountNumber);
       cuentaDestino.setBalance(montoParaDestino);//Se actualiza el campo balance con el nuevo valor sumado



       //Se deben crear dos transacciones, una con el tipo de transacción “DEBIT” asociada a la cuenta
        // de origen y la otra con el tipo de transacción “CREDIT” asociada a la cuenta de destino.

        //crear las transacciones asociandolas a las cuentas correspondientes pasadas por parametros
        //a al repositorio account repository para obetener el id de esa cuenta

        //montoActualParaOrigen
        transactionRepository.save(new Transaction(TransactionType.DEBIT,-(amount),
                description+" "+toAccountNumber,LocalDateTime.now(),accountRepository.findByNumber(fromAccountNumber)));


        //montoParaDestino
        transactionRepository.save(new Transaction(TransactionType.CREDIT,amount,
                description+" "+fromAccountNumber,LocalDateTime.now(),accountRepository.findByNumber(toAccountNumber)));

        return new ResponseEntity<>(HttpStatus.CREATED);


    }

    /*

    //DEVUELVE EL CLIENTE AUTENTICADO
    @RequestMapping("/clients/transactions")
    public ClientDTO getAll(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        return new ClientDTO(client);

    }
    */


}
