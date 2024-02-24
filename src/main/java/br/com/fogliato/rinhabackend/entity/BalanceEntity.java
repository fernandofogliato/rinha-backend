package br.com.fogliato.rinhabackend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "balance")
public class BalanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "balance_id")
    private int balanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id", nullable = false)
    private CustomerEntity customer;

    @Column
    private int balance;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public int getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(int balanceId) {
        this.balanceId = balanceId;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceEntity that = (BalanceEntity) o;
        return balanceId == that.balanceId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(balanceId);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BalanceEntity(int customerId, int balance) {
        this.customer = new CustomerEntity();
        this.customer.setCustomerId(customerId);
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
    }

    public BalanceEntity(int balance, int customerId, int limit) {
        this.customer = new CustomerEntity();
        this.customer.setCustomerId(customerId);
        this.customer.setLimit(limit);
        this.balance = balance;
    }

    public BalanceEntity() {
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }
}
