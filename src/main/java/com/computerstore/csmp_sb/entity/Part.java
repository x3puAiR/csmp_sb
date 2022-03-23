package com.computerstore.csmp_sb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Part {
    int skuId, amount;
    double price;
    String name;

}
