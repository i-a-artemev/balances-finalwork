package ru.skillfactory.balancesfinalwork.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

public class BalanceResponse {
    public static ResponseEntity<Object> makeResponse(int status, String message, Object responseData, HttpStatus httpStatus){
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        map.put("message", message);
        map.put("data", responseData);
        return new ResponseEntity<>(map, httpStatus);
    }
}
