package ru.skillfactory.balancesfinalwork.model;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "operations")
public class Operation {

    public enum Type{debit, credit};

    @NotNull
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "operation_sequence")
    @SequenceGenerator(name="operation_sequence", sequenceName="operation_sequence", allocationSize = 1)
    private Long id;
    @NotNull
    private Date date;
    @Column(name = "current_balance_id")
    @NotNull
    private Long currentBalanceId;
    @Column(name = "type")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(name = "correspond_balance_id")
    @NotNull
    private Long correspondBalanceId;
    @Positive
    @Digits(integer = 15, fraction = 2)
    private BigDecimal sum;

    public Operation(Long id, Date date, Long currentBalanceId, Type type, Long correspondBalanceId, BigDecimal sum) {
        this.id = id;
        this.date = date;
        this.currentBalanceId = currentBalanceId;
        this.type = type;
        this.correspondBalanceId = correspondBalanceId;
        this.sum = sum;
    }

    public Operation() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getCurrentBalanceId() {
        return currentBalanceId;
    }

    public void setCurrentBalanceId(Long currentBalanceId) {
        this.currentBalanceId = currentBalanceId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Long getCorrespondBalanceId() {
        return correspondBalanceId;
    }

    public void setCorrespondBalanceId(Long correspondBalanceId) {
        this.correspondBalanceId = correspondBalanceId;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public void setAll(Date date, Long currentBalanceId, Type type, Long correspondBalanceId, BigDecimal sum){
        this.date = date;
        this.currentBalanceId = currentBalanceId;
        this.type = type;
        this.correspondBalanceId = correspondBalanceId;
        this.sum = sum;
    }

    public static Map<String, Object> responceOperation(Operation operation){
        Map<String, Object> map = new HashMap<>();
        map.put("date", operation.getDate());
        if (operation.getType() == Type.debit){
            if (Objects.equals(operation.getCurrentBalanceId(), operation.getCorrespondBalanceId())){
                map.put("type", "put money");
            }
            else{
                map.put("type", "incoming transfer");
            }
        }
        else{
            if (Objects.equals(operation.getCurrentBalanceId(), operation.getCorrespondBalanceId())){
                map.put("type", "take money");
            }
            else{
                map.put("type", "outgoing transfer");
            }
        }
        map.put("sum", operation.getSum());
        return map;
    }
}
