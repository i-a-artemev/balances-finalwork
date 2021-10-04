package ru.skillfactory.balancesfinalwork.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
@Entity
@Table(name="balances")
public class Balance {
    @NotNull
    @Id
    private Long id;
    @PositiveOrZero
    @Digits(integer = 15, fraction = 2)
    private BigDecimal balance;

    public Balance() {
    }

    public Balance(Long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}
