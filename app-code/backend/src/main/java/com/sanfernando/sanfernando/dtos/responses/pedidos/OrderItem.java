package com.sanfernando.sanfernando.dtos.responses.pedidos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
  private String productCode;
  private String name;
  private String quantity;
  private String unit;
}
