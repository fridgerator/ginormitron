package com.fridgerator.ginormitron.transactiondata.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.fridgerator.ginormitron.transactiondata.entities.Retailer;

public interface RetailerRepo extends MongoRepository<Retailer, String> {
    public Optional<Retailer> findById(String id);
    public long count();

    @Query("db.customer.aggregate([{$sample: { size: 1 }}])")
    public List<Retailer> getRandom();
}
