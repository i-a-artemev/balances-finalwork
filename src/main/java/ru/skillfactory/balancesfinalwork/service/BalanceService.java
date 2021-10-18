package ru.skillfactory.balancesfinalwork.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillfactory.balancesfinalwork.model.Balance;
import ru.skillfactory.balancesfinalwork.model.Operation;
import ru.skillfactory.balancesfinalwork.repository.BalanceRepository;
import ru.skillfactory.balancesfinalwork.repository.OperationRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final OperationRepository operationRepository;

    public BalanceService(BalanceRepository balanceRepository, OperationRepository operationRepository) {
        this.balanceRepository = balanceRepository;
        this.operationRepository = operationRepository;
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

    @Transactional
    public void takeMoney(long id, BigDecimal sum) throws Exception {
        Optional<Balance> balance = balanceRepository.findById(id);
        if (balance.isEmpty()){
            throw new Exception(String.format("There is no balance with id=%d",id));
        }
        BigDecimal currentBalance = balance.get().getBalance();
        if (currentBalance.compareTo(sum) < 0){
            throw new Exception("There is not enough money");
        }
        Operation operation = new Operation();
        java.sql.Date sqlCurrenDate = new java.sql.Date((new Date()).getTime());
        balance.get().setBalance(currentBalance.subtract(sum));
        balanceRepository.save(balance.get());
        operation.setAll(sqlCurrenDate, balance.get().getId(), Operation.Type.credit, balance.get().getId(),sum);
        operationRepository.save(operation);
    }

    @Transactional
    public void putMoney(long id, BigDecimal sum) throws Exception {
        Optional<Balance> balance = balanceRepository.findById(id);
        if (balance.isEmpty()){
            throw new Exception(String.format("There is no balance with id=%d",id));
        }
        BigDecimal currentBalance = balance.get().getBalance();
        balance.get().setBalance(currentBalance.add(sum));
        Operation operation = new Operation();
        java.sql.Date sqlCurrenDate = new java.sql.Date((new Date()).getTime());
        balanceRepository.save(balance.get());
        operation.setAll(sqlCurrenDate, balance.get().getId(), Operation.Type.debit, balance.get().getId(),sum);
        operationRepository.save(operation);
    }

    public List<Operation> getOperationList(long id, Date dateAfter, Date dateBefore){
        List<Operation> operations = null;
        if (dateAfter != null && dateBefore != null){
            operations = operationRepository.findOperationsByCurrentBalanceIdAndDateBetween(id, dateAfter, dateBefore);
        }
        if(dateAfter == null && dateBefore != null) {
            operations = operationRepository.findOperationsByCurrentBalanceIdAndDateBefore(id, dateBefore);
        }
        if (dateAfter != null && dateBefore == null){
            operations = operationRepository.findOperationsByCurrentBalanceIdAndDateAfter(id,dateAfter);
        }
        if (dateAfter == null && dateBefore == null){
            operations = operationRepository.findOperationsByCurrentBalanceId(id);
        }
        return operations;
    }

    @Transactional
    public void transferMoney(long senderId, long receiverId, BigDecimal sum) throws Exception {
        Optional<Balance> senderBalance = balanceRepository.findById(senderId);
        if (senderBalance.isEmpty()){
            throw new Exception(String.format("There is no sender with id=%d",senderId));
        }
        Optional<Balance> receiverBalance = balanceRepository.findById(receiverId);
        if (receiverBalance.isEmpty()){
            throw new Exception(String.format("There is no receiver with id=%d",senderId));
        }
        if (senderBalance.get().getBalance().compareTo(sum) < 0){
            throw new Exception(String.format("There is not enough money on sender account with id=%d",senderId));
        }
        senderBalance.get().setBalance(senderBalance.get().getBalance().subtract(sum));
        receiverBalance.get().setBalance(receiverBalance.get().getBalance().add(sum));
        balanceRepository.save(senderBalance.get());
        balanceRepository.save(receiverBalance.get());
        Operation operationSender = new Operation();
        Operation operationReceiver = new Operation();
        java.sql.Date sqlCurrenDate = new java.sql.Date((new Date()).getTime());
        operationSender.setAll(sqlCurrenDate, senderBalance.get().getId(), Operation.Type.credit, receiverBalance.get().getId(),sum);
        operationRepository.save(operationSender);
        operationReceiver.setAll(sqlCurrenDate, receiverBalance.get().getId(), Operation.Type.debit, senderBalance.get().getId(),sum);
        operationRepository.save(operationReceiver);
    }

}
