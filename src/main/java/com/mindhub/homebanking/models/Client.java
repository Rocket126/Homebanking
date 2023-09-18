package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;//nuevo dato de entrada password

    public Client() { }

    public Client(String first, String last, String email,String password) {
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.password = password;
    }

    @OneToMany(mappedBy="owner", fetch=FetchType.EAGER)//Anotacion OneToMany mennciona una relacion de uno a muchos
    Set<Account> accounts = new HashSet<>();//la coleccion Set recupera los resultados duplicados de account a cliente, pero ignorando las entradas duplicadas

    //el campo client en la tabla ClientLoan, es la tabla propietaria de la relacion por tener llave foranea
     @OneToMany(mappedBy="client", fetch=FetchType.EAGER)//hay una relacion de uno a muchos de clientes a prestamos(Loan) pero usando la tabla ClientLoan.
     Set<ClientLoan> clientLoans = new HashSet<>();

     //De una a muchas tarjetas
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)//hay una relacion de uno a muchos de clientes a tarjetas(Card).
    Set<Card> cards = new HashSet<>();

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    //agregado recientemente
    public void addCLientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public Set<Card> getCards() {
        return cards;
    }
    public void addCard(Card card){
        card.setClient(this);
        cards.add(card);
    }

    public Set<Account> getAccounts() {
        return accounts;
    }
    public void addAccount(Account account) {//El metodo addAccount nos permite adherir una cuenta a una persona
        account.setOwner(this);
        accounts.add(account);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String toString() {
        return firstName + " " + lastName;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
