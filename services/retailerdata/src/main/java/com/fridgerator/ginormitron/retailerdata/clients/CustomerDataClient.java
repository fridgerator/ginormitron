package com.fridgerator.ginormitron.retailerdata.clients;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "customerData", url = "${customer-data.url}")
public interface CustomerDataClient {
    
    @RequestMapping(method = RequestMethod.GET, value = "/created-count")
    Map<String, Long> getCustomerCounts();
}
