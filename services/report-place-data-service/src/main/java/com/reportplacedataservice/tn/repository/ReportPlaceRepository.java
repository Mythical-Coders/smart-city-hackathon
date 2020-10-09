package com.reportplacedataservice.tn.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.reportplacedataservice.tn.models.ReportPlace;

public interface ReportPlaceRepository extends MongoRepository<ReportPlace, String> {
}
