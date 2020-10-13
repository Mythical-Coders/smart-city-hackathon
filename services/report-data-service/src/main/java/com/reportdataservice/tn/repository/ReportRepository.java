package com.reportdataservice.tn.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.reportdataservice.tn.models.Report;

public interface ReportRepository extends MongoRepository<Report, String> {
	List<Report> findByIdUser(String idUser);

}
