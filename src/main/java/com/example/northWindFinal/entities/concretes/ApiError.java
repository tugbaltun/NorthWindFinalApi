package com.example.northWindFinal.entities.concretes;

import lombok.Data;

@Data
public class ApiError {
  private int status;
  private String message;

}
