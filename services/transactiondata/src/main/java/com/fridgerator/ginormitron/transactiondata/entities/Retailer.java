package com.fridgerator.ginormitron.transactiondata.entities;

import org.springframework.data.annotation.Id;

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
public class Retailer {

    @Id
    public String id;

    private String name;
    private String address;
    private String phoneNumber;
    private String state;
    private String timezone;
    private String zipcode;
    private String city;
}
