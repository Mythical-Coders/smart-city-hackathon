package com.impound.tn.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.impound.tn.models.Impound;

public interface ImpoundRepository extends MongoRepository<Impound, String> {
	List<Impound> findByIdDriver(String idDriver);

}	
