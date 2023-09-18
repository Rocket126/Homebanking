package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static com.mindhub.homebanking.repositories.ClientLoanRepository.*;

@SpringBootApplication
public class HomebankingApplication {
	@Autowired //Spring inyecta el objeto PasswordEncoder que se crea con el @Bean en la clase WebAuthentication
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(
			ClientRepository clientRepository,
			AccountRepository accountRepository,
			TransactionRepository transactionRepository,
			ClientLoanRepository clientLoanRepository,
			LoanRepository loanRepository,
			CardRepository cardRepository
	) {

		return (args) -> {

/*

			Client melba = new Client();
			melba.setFirstName("melba");
			melba.setLastName("morel");
			melba.setEmail("melba@mindhub.com");
			melba.setPassword(passwordEncoder.encode("1234"));

			clientRepository.save(melba);

			//Cliente Dos
			Client Alan = new Client();
			Alan.setFirstName("Alan");
			Alan.setLastName("poblete");
			Alan.setEmail("Alan@mindhub.com");
			Alan.setPassword(passwordEncoder.encode("1111"));
			clientRepository.save(Alan);


			Account cuenta1 = new Account();//creo un objeto de tipo cuenta que almacenara el id del cliente y cuenta
			cuenta1.setNumber("VIN001");
			cuenta1.setCreationDate(LocalDateTime.now());
			cuenta1.setBalance(5000.0);
			cuenta1.setOwner(melba);

			accountRepository.save(cuenta1);

			Account cuenta2 = new Account();
			//cuenta2.setId(2);
			cuenta2.setNumber("VIN002");
			cuenta2.setCreationDate(LocalDateTime.now().plusDays(1));
			cuenta2.setBalance(7500.0);

			cuenta2.setOwner(melba);

			accountRepository.save(cuenta2);


			Transaction ts1 = new Transaction();
			ts1.setAccount(cuenta1);
			ts1.setAmount(7500.0);
			ts1.setDescription("PizzaHut");
			ts1.setDate(LocalDateTime.now());
			ts1.setType(TransactionType.CREDIT);

			transactionRepository.save(ts1);

			Transaction ts2 = new Transaction();
			ts2.setAccount(cuenta1);
			ts2.setAmount(8500.0);
			ts2.setDescription("Audifonos");
			ts2.setDate(LocalDateTime.now());
			ts2.setType(TransactionType.CREDIT);

			transactionRepository.save(ts2);


			Transaction ts3 = new Transaction();
			ts3.setAccount(cuenta1);
			ts3.setAmount(-18500.0);
			ts3.setDescription("Reloj");
			ts3.setDate(LocalDateTime.now());
			ts3.setType(TransactionType.DEBIT);

			transactionRepository.save(ts3);


			Transaction ts4 = new Transaction();
			ts4.setAccount(cuenta2);
			ts4.setAmount(-1500.0);
			ts4.setDescription("Pulsera");
			ts4.setDate(LocalDateTime.now());
			ts4.setType(TransactionType.DEBIT);

			transactionRepository.save(ts4);

			Transaction ts5 = new Transaction();
			ts5.setAccount(cuenta2);
			ts5.setAmount(-15000.0);
			ts5.setDescription("Polera");
			ts5.setDate(LocalDateTime.now());
			ts5.setType(TransactionType.DEBIT);

			transactionRepository.save(ts5);


			//Añadir un segundo cliente a una cuenta nueva
			Account cuenta3 = new Account();//creo un objeto de tipo cuenta que almacenara el id del cliente y cuenta
			cuenta3.setOwner(Alan);
			cuenta3.setNumber("VIN003");
			cuenta3.setCreationDate(LocalDateTime.now());
			cuenta3.setBalance(5000.0);

			accountRepository.save(cuenta3);


			Account cuenta4 = new Account();//creo un objeto de tipo cuenta que almacenara el id del cliente y cuenta
			cuenta4.setOwner(Alan);
			cuenta4.setNumber("VIN004");
			cuenta4.setCreationDate(LocalDateTime.now().plusDays(1));
			cuenta4.setBalance(7500.0);

			accountRepository.save(cuenta4);

			Transaction ts6 = new Transaction();
			ts6.setAccount(cuenta3);
			ts6.setAmount(15000.0);
			ts6.setDescription("Poleron");
			ts6.setDate(LocalDateTime.now());
			ts6.setType(TransactionType.CREDIT);

			transactionRepository.save(ts6);

			Transaction ts7 = new Transaction();
			ts7.setAccount(cuenta4);
			ts7.setAmount(-15000.0);
			ts7.setDescription("Jeans Levis");
			ts7.setDate(LocalDateTime.now());
			ts7.setType(TransactionType.DEBIT);

			transactionRepository.save(ts7);



			//INSERCIONES DE CLIENTE Y PRESTAMO
			//List<Integer> paymentsHipotecario = ;
			Loan hipotecario = new Loan("Hipotecario",500000.0,List.of(12,24,36,48,60));
			Loan personal = new Loan("Personal", 100000.0, List.of(6,12,24));
			Loan automotriz = new Loan("Automotriz", 300000.0, List.of(6,12,24,36));

			loanRepository.save(hipotecario);
			loanRepository.save(personal);
			loanRepository.save(automotriz);


			//**************************************************************************
			//INSERTANDO UN CLIENTE A UN PRESTAMO EN LA TABLA CLIENTLOAN
			ClientLoan melbaHipotecario = new ClientLoan(400000.0,60,melba,hipotecario);
			ClientLoan melbaPersonal = new ClientLoan(50000.0,12,melba,personal);
			ClientLoan alanPersonal = new ClientLoan(100000.0,24,Alan,personal);
			ClientLoan alanAutomotriz = new ClientLoan(200000.0,36,Alan,automotriz);

			clientLoanRepository.save(melbaHipotecario);
			clientLoanRepository.save(melbaPersonal);
			clientLoanRepository.save(alanPersonal);
			clientLoanRepository.save(alanAutomotriz);


			//USUARIO ADMIN
			Client admin = new Client("admin","admin","admin@mindhub.com",passwordEncoder.encode("1111"));
			clientRepository.save(admin);


			//**********************************************************************
			//ASIGNANDO UNA TARJETA A UN CLIENTE
			Card goldMelba = new Card(melba.getFirstName() ,CardType.DEBIT, CardColor.GOLD,"1111-1111-1111-1111",811,LocalDateTime.now().plusYears(5),LocalDateTime.now(),melba );
			Card titaniumMelba = new Card(melba.getFirstName(),CardType.CREDIT, CardColor.TITANIUM,"0000-0000-0000-0000",000,LocalDateTime.now().plusYears(5),LocalDateTime.now(),melba );

			Card silverAlan = new Card(Alan.getFirstName(),CardType.CREDIT ,CardColor.SILVER,"2222-22222-2222-2222", 333,LocalDateTime.now().plusYears(2),LocalDateTime.now(),Alan);

			cardRepository.save(goldMelba);
			cardRepository.save(titaniumMelba);
			cardRepository.save(silverAlan);
*/




		};
	}
}
