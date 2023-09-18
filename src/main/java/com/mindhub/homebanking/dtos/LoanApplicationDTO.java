package com.mindhub.homebanking.dtos;

import java.util.List;

public class LoanApplicationDTO {
    //Este DTO debe tener id del préstamo, monto, cuotas y número de cuenta de destino

    private long LoanId;
    private Double amount;
    private Integer payments;
    private String toAccountNumber;//Numero de la cuenta

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(long id, Double amount, Integer payments, String number) {
        this.LoanId = id;
        this.amount = amount;
        this.payments = payments;
        this.toAccountNumber = number;
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public long getLoanId() {
        return LoanId;
    }

    public void setLoanId(long loanId) {
        LoanId = loanId;
    }

    public Integer getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }



}



