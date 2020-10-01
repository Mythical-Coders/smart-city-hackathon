package com.impound.tn.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.impound.tn.models.Impound;

public interface ImpoundRepository extends MongoRepository<Impound, String> {

}
