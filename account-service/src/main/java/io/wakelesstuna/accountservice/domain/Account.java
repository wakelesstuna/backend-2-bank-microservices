package io.wakelesstuna.accountservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "account")
@Data
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = AUTO)
    private UUID id;

    private UUID holderId;

    private double balance;

    public Account(UUID holderId) {
        this.holderId = holderId;
        this.balance = 0;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public boolean withdraw(double amount) {
        double newBalance = this.balance - amount;
        if (newBalance < 0) return false;
        this.balance = newBalance;
        return true;
    }

}
