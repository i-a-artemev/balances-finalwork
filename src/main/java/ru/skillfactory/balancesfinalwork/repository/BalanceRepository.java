package ru.skillfactory.balancesfinalwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.skillfactory.balancesfinalwork.model.Balance;

public interface BalanceRepository extends JpaRepository<Balance,Long> {
}
