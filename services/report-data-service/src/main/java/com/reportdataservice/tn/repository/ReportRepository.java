package com.reportdataservice.tn.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.reportdataservice.tn.models.Report;

public interface ReportRepository extends MongoRepository<Report, String> {
}
