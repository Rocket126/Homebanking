package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> register(Authentication authentication,
      @RequestBody LoanApplicationDTO loanApplicationDTO) {

        //validamos los parametros que no esten vacios y no sean un valor distinto
        if ( loanApplicationDTO.getAmount().isNaN() || loanApplicationDTO.getPayments() == null || loanApplicationDTO.getToAccountNumber().isEmpty())
        {
            return new ResponseEntity<>("DATOS PERDIDOS", HttpStatus.FORBIDDEN);
        }

        //creo el objeto de tipo cuenta  y busco en base al parametro del front en la tabla cuentas (si existe);
        Account cuenta = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());

        //Verificar que el préstamo exista EN LA TABLA LOAN
         Loan existe = loanRepository.findById(loanApplicationDTO.getLoanId());
        if(existe == null) {
            return new ResponseEntity<>("EL PRESTAMO NO EXISTE", HttpStatus.FORBIDDEN);
        }

        //Verificar que el monto solicitado no exceda el monto máximo del préstamo
        Double montoPrestamo = loanRepository.findById(loanApplicationDTO.getLoanId()).getMaxAmount();
        if(loanApplicationDTO.getAmount() > montoPrestamo)
        {
            return new ResponseEntity<>("El monto a solicitar excede el monto del prestamo", HttpStatus.FORBIDDEN);
        }

        //Verifica que la cantidad de cuotas se encuentre entre las disponibles del préstamo
        boolean verificarCuota = existe.getPayments().contains(loanApplicationDTO.getPayments());
        if(verificarCuota == false)
        {
            return new ResponseEntity<>("la cantidad de cuotas no se encuentra en el prestamo", HttpStatus.FORBIDDEN);
        }


        //Verificar que la cuenta de destino pertenezca al cliente autenticado
        Client cliente = clientRepository.findByEmail(authentication.getName());
        boolean vCuentaDestino = cliente.getAccounts().contains(cuenta);
        if(vCuentaDestino == false)
        {
            return new ResponseEntity<>("La cuenta de destino no pertenece al cliente", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de destino exista
        Account verificarCD = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
        if(verificarCD == null)
        {
            return new ResponseEntity<>("La cuenta de destino no existe", HttpStatus.FORBIDDEN);
        }



        //Se debe crear una solicitud de préstamo con el monto solicitado sumando el 20% del mismo
        Double montoFinal = loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount() * 0.2);
        Loan datosPrestamo = loanRepository.findById(loanApplicationDTO.getLoanId());
        //instermos en la tabla clientloan
        ClientLoan clientLoan = new ClientLoan(montoFinal,loanApplicationDTO.getPayments(),cliente,datosPrestamo);
        clientLoanRepository.save(clientLoan);


        //Se debe crear una transacción “CREDIT” asociada a la cuenta de destino (el monto debe quedar positivo) con la descripción concatenando el nombre del préstamo y la frase “loan approved”
        Account cuentaDestino = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
        String nombrePrestamo = loanRepository.findById(loanApplicationDTO.getLoanId()).getName();

        transactionRepository.save(new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(),
                nombrePrestamo+" Prestamo Aprobado", LocalDateTime.now(), cuentaDestino));

        cuentaDestino.setBalance(loanApplicationDTO.getAmount() + cuentaDestino.getBalance());
        accountRepository.save(cuentaDestino);


        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    //BUSCO Y RETORNO TODOS LOS LOANS(PRESTAMOS) del usuario autenticado
    @GetMapping("/loans")
    public List<LoanDTO> getAll(Authentication authentication) {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());

    }


}
