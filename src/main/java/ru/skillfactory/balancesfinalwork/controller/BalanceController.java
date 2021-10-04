package ru.skillfactory.balancesfinalwork.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.skillfactory.balancesfinalwork.response.BalanceResponse;
import ru.skillfactory.balancesfinalwork.service.BalanceService;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@RestController
@Validated
@RequestMapping(path = "api")
public class BalanceController {
    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping(path = "getBalance")
    public ResponseEntity<Object> getBalance(@RequestParam("id") @NotNull Long id){
        try {
            BigDecimal balance = balanceService.getBalance(id);
            return BalanceResponse.makeResponse(1,"success", balance, HttpStatus.OK);
        } catch (Exception e) {
            return BalanceResponse.makeResponse(0,e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "takeMoney")
    public ResponseEntity<Object> takeMoney(@RequestParam("id") @NotNull Long id,
                                            @RequestParam("sum") @Digits(integer = 15, fraction = 2)
                                            @NotNull @Positive BigDecimal sum){
        try {
            balanceService.takeMoney(id, sum);
            return BalanceResponse.makeResponse(1,"success",null, HttpStatus.OK);
        } catch (Exception e) {
            return BalanceResponse.makeResponse(0, e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "putMoney")
    public ResponseEntity<Object> putMoney(@RequestParam("id") @NotNull Long id,
                                           @RequestParam("sum") @Digits(integer = 15, fraction = 2)
                                           @NotNull @Positive BigDecimal sum){
        try {
            balanceService.putMoney(id, sum);
            return BalanceResponse.makeResponse(1,"success",null, HttpStatus.OK);
        } catch (Exception e) {
            return BalanceResponse.makeResponse(0, e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        return BalanceResponse.makeResponse(0,"Parameter validation error: " + e.getMessage(),
                null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        return BalanceResponse.makeResponse(0,"Illegal parameter error: " + e.getMessage(),
                null, HttpStatus.BAD_REQUEST);
    }
}
