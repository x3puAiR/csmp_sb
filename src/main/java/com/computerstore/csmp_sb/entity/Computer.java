package com.computerstore.csmp_sb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Computer {
    int skuId, amount;
    String name;
    double price;

}
