package ru.skillfactory.balancesfinalwork.service;

import org.springframework.stereotype.Service;
import ru.skillfactory.balancesfinalwork.model.Balance;
import ru.skillfactory.balancesfinalwork.repository.BalanceRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public BigDecimal getBalance(long id) throws Exception {
        Optional<Balance> balance = balanceRepository.findById(id);
        if (balance.isPresent()){
            return balance.get().getBalance();
        }
        else{
            throw new Exception(String.format("There is no balance with id=%d",id));
        }
    }

    public void takeMoney(long id, BigDecimal sum) throws Exception {
        Optional<Balance> balance = balanceRepository.findById(id);
        if (balance.isEmpty()){
            throw new Exception(String.format("There is no balance with id=%d",id));
        }
        BigDecimal currentBalance = balance.get().getBalance();
        if (!(currentBalance.compareTo(sum) < 0)) {
            balance.get().setBalance(currentBalance.subtract(sum));
            balanceRepository.save(balance.get());
        }
        else {
            throw new Exception("There is not enough money");
        }
    }

    public void putMoney(long id, BigDecimal sum) throws Exception {
        Optional<Balance> balance = balanceRepository.findById(id);
        if (balance.isEmpty()){
            throw new Exception(String.format("There is no balance with id=%d",id));
        }
        BigDecimal currentBalance = balance.get().getBalance();
        balance.get().setBalance(currentBalance.add(sum));
        balanceRepository.save(balance.get());
    }

}
