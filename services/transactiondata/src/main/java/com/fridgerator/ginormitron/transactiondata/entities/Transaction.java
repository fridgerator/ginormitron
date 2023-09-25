package com.fridgerator.ginormitron.transactiondata.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Transaction {
    private String customerId;
    private String retailerId;

    private String productName;
    private String amount;
    private String datetime;
}
