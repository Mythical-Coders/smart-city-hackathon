package com.reportplacedataservice.tn.controllers;

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

import com.reportplacedataservice.tn.models.ReportPlace;
import com.reportplacedataservice.tn.repository.ReportPlaceRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

//test
//ReportPlace data service
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/reportPlace")
@Api(value = "ReportPlaceResourceAPI", produces = MediaType.APPLICATION_JSON_VALUE, description = "ReportPlace Resource")
public class ReportPlaceController {
	@Autowired
	private ReportPlaceRepository reportPlaceRepo;

	@ApiOperation("Get All ReportPlace")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully get ReportPlace"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@GetMapping("/all")
	public List<ReportPlace> GetReportPlaces() {
		return reportPlaceRepo.findAll();
	}

	@ApiOperation("Get ReportPlace")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully get ReportPlace by ID"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@GetMapping("/{id}")
	public ReportPlace GetReportPlace(@PathVariable String id) {
		return reportPlaceRepo.findById(id).orElse(null);
	}

	@ApiOperation("Add ReportPlace")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully add ReportPlace"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@PostMapping("/")
	public ReportPlace postReportPlace(@RequestBody ReportPlace ReportPlace) {
		return reportPlaceRepo.save(ReportPlace);
	}

	@ApiOperation("Update ReportPlace")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully update ReportPlace"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@PutMapping("/")
	public ReportPlace putReportPlace(@RequestBody ReportPlace newReportPlace) {
		ReportPlace oldReportPlace = reportPlaceRepo.findById(newReportPlace.getId()).orElse(null);
		oldReportPlace.setType(newReportPlace.getType());
		oldReportPlace.setLatitude(newReportPlace.getLatitude());
		oldReportPlace.setLongitude(newReportPlace.getLongitude());
		oldReportPlace.setAdress(newReportPlace.getAdress());


		return reportPlaceRepo.save(oldReportPlace);
	}

	@ApiOperation("Delete ReportPlace")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved ReportPlace"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@DeleteMapping("/{id}")
	public String deleteReportPlace(@PathVariable String id) {
		reportPlaceRepo.deleteById(id);
		return id;
	}

}
