package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private long id;//id de la tabla clientLoan
    private long loanId; //id del prestamo de la tabla prestamos
    private String name;//nombre del monto (Hipotecario)
    private double amount;//monto solicitado, y se obtiene de ClientLoan, mas abajo con el contructor.
    private int payments;//cuotas a pagar


    public ClientLoanDTO(ClientLoan clientLoan) {//el constructor ClientLoanDTO recibe un objeto ClientLoan
        //del cual se obtienen los valores

        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.amount = clientLoan.getAmount();//se obtiene de la tabla ClienteLoan
        this.name = clientLoan.getLoan().getName();
        this.payments = clientLoan.getPayments();//se obtiene de la tabla ClienteLoan
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }


}


