package com.fridgerator.ginormitron.transactiondata.entities;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Setter
@Getter
public class Customer {
    
    @Id
    public String _id;
    
    private String name;
    private String address;
    private String phoneNumber;
    private String state;
    private String timezone;
    private String zipcode;
    private String city;
}
