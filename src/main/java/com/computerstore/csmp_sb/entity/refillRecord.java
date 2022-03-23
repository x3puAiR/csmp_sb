package com.computerstore.csmp_sb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class refillRecord {
    private int id;
    private double price;
    private int amount;
    private int skuId;
    private String dateTime;
    private String type;


}
