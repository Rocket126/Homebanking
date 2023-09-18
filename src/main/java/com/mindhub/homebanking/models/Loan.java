package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//Luego importamos y aplicamos varias anotaciones JPA para conectar la clase a una tabla de base de datos.
@Entity// le dice a Spring que cree una  tabla de Loan para esta clase.
public class Loan {
    //PRESTAMO
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private double maxAmount;
    @ElementCollection
    private List<Integer> payments;

    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)//campo id_prestamo de la tabla Clientloan
    Set<ClientLoan> clientLoans = new HashSet<>();//anotando este campo con JPA para vincularlo a la tabla de datos

    public Loan() {}//debe definir un constructor predeterminado (sin argumentos) para cualquier clase de entidad.

    // Eso es lo que JPA llamará para crear nuevas instancias.
    public Loan(String name, double maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;

    }

    //agregado recientemente
    public void addCLientLoan(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }


    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public List<Client> getClients(){
        return clientLoans.stream().map(ClientLoan::getClient).collect(Collectors.toList());
    }
}
