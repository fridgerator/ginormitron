package com.fridgerator.ginormitron.retailerdata.conroller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fridgerator.ginormitron.retailerdata.clients.CustomerDataClient;

@RestController
@RequestMapping("/api/v1")
class RetailerDataController {
    private final CustomerDataClient customerDataClient;

    RetailerDataController(CustomerDataClient customerDataClient) {
        this.customerDataClient = customerDataClient;
    }

    @GetMapping("/customers-created-count")
    public Map<String, Long> getCustomersCreatedCount() {
        return customerDataClient.getCustomerCounts();
    }
}
