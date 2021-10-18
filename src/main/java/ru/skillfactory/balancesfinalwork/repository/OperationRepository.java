package ru.skillfactory.balancesfinalwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillfactory.balancesfinalwork.model.Operation;

import java.util.Date;
import java.util.List;

public interface OperationRepository extends JpaRepository<Operation,Long> {
    List<Operation> findOperationsByCurrentBalanceId(Long id);

    List<Operation> findOperationsByCurrentBalanceIdAndDateAfter(Long id, Date date);

    List<Operation> findOperationsByCurrentBalanceIdAndDateBetween(Long id, Date dateStart, Date dateStop);

    List<Operation> findOperationsByCurrentBalanceIdAndDateBefore(Long id, Date date);
}
