package com.mindhub.homebanking.models;


import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String number;
    private LocalDateTime creationDate;
    private Double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")//Columna de la tabla que almacena el id del cliente en la tabla cuenta
    private Client owner;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)//Anotacion OneToMany menciona una relacion de uno a muchas transacciones
    Set<Transaction> transactions = new HashSet<>();
    //private Transaction transaction;

    public Account() {
    }
    //Long id,
    public Account(String number, LocalDateTime creationDate, Double balance, Client owner) {
        //this.id = id;
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    //public void setId(Long id) {
      //  this.id = id;
    //}

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }
}


