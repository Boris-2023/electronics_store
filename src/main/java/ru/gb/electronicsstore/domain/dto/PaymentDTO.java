package ru.gb.electronicsstore.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentDTO {
    private String user;
    private Long order;
    private Double amount;
}
