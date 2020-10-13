package com.reportdataservice.tn.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reportdataservice.tn.models.Report;
import com.reportdataservice.tn.repository.ReportRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

//test
//Report data service
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/report")
@Api(value = "ReportResourceAPI", produces = MediaType.APPLICATION_JSON_VALUE, description = "Report Resource")
public class ReportController {
	@Autowired
	private ReportRepository reportRepo;

	@ApiOperation("Get All Report")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully get Report"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@GetMapping("/all")
	public List<Report> GetReports() {
		return reportRepo.findAll();
	}
	@ApiOperation("Get by idUser")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully get Report idUser"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@GetMapping("/idUser/{idUser}")
	public List<Report> GetReportsByIdUser(@PathVariable String idUser) {
		return reportRepo.findByIdUser(idUser);
	}
	@ApiOperation("Get Report")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully get Report by ID"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@GetMapping("/{id}")
	public Report GetReport(@PathVariable String id) {
		return reportRepo.findById(id).orElse(null);
	}


	@ApiOperation("Add Report")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully add Report"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@PostMapping("/")
	public Report postReport(@RequestBody Report Report) {
		return reportRepo.save(Report);
	}

	@ApiOperation("Update Report")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully update Report"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@PutMapping("/")
	public Report putReport(@RequestBody Report newReport) {
		Report oldReport = reportRepo.findById(newReport.getId()).orElse(null);
		oldReport.setIdImage(newReport.getIdImage());
		oldReport.setIdPlace(newReport.getIdPlace());
		oldReport.setIdUser(newReport.getIdUser());

		return reportRepo.save(oldReport);
	}

	@ApiOperation("Delete Report")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved Report"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@DeleteMapping("/{id}")
	public String deleteReport(@PathVariable String id) {
		reportRepo.deleteById(id);
		return id;
	}

}
