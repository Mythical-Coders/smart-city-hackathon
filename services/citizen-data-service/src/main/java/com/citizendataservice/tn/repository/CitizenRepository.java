package com.citizendataservice.tn.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.citizendataservice.tn.models.Citizen;

public interface CitizenRepository extends MongoRepository<Citizen, String> {
	Citizen findByMatricule(String matricule);
}
