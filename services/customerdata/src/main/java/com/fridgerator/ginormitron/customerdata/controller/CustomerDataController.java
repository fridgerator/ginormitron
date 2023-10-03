package com.fridgerator.ginormitron.customerdata.controller;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.fridgerator.ginormitron.customerdata.generator.GeneratorCounter;
import com.fridgerator.ginormitron.customerdata.model.Generated;

@RestController
@RequestMapping("/api/v1")
class CustomerDataController {

    @GetMapping("/created-count")
    public Generated getCreatedCount() {
        return new Generated(GeneratorCounter.getGeneratedCount());
    }
}
