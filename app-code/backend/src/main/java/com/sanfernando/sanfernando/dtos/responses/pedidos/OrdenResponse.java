package com.sanfernando.sanfernando.dtos.responses.pedidos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenResponse {
    private int id;
    private String requestDate;
    private String deliveryDate;
    private String department;
    private String representative;
    private String employee;
    private String orderType;
    private String ticketCode;
    private String status;
}
