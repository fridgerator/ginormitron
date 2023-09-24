package com.fridgerator.ginormitron.customerdata.model;

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
public class Customer {
    private String name;
    private String address;
    private String phoneNumber;
    private String state;
    private String timezone;
    private String zipcode;
    private String city;
}
