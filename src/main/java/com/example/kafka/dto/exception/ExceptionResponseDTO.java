package com.example.kafka.dto.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Builder
@Data
public class ExceptionResponseDTO {
    String message;
    String type;
    HttpStatus httpStatus;
}
