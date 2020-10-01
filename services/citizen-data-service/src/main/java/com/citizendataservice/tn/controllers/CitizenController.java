package com.citizendataservice.tn.controllers;

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

import com.citizendataservice.tn.models.Citizen;
import com.citizendataservice.tn.repository.CitizenRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

//Citizen data service
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/citizen")
@Api(value = "CitizenResourceAPI", produces = MediaType.APPLICATION_JSON_VALUE, description = "Citizen Resource")
public class CitizenController {
	@Autowired
	private CitizenRepository citizenRepo;

	@ApiOperation("Get All Citizen")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully get Citizen"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@GetMapping("/all")
	public List<Citizen> GetCitizens() {
		return citizenRepo.findAll();
	}

	@ApiOperation("Get Citizen")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully get Citizen by ID"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@GetMapping("/{id}")
	public Citizen GetCitizen(@PathVariable String id) {
		return citizenRepo.findById(id).orElse(null);
	}
	
	@ApiOperation("Get Citizen with Matricule")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully get Citizen by ID"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@GetMapping("/matricule/{matricule}")
	public Citizen GetCitizenMatricule(@PathVariable String matricule) {
		return citizenRepo.findByMatricule(matricule);
	}

	@ApiOperation("Add Citizen")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully add Citizen"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@PostMapping("/")
	public Citizen postCitizen(@RequestBody Citizen Citizen) {
		return citizenRepo.save(Citizen);
	}

	@ApiOperation("Update Citizen")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully update Citizen"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@PutMapping("/")
	public Citizen putCitizen(@RequestBody Citizen newCitizen) {
		Citizen oldCitizen = citizenRepo.findById(newCitizen.getId()).orElse(null);
		oldCitizen.setMatricule(newCitizen.getMatricule());
		oldCitizen.setTelephone(newCitizen.getTelephone());

		return citizenRepo.save(oldCitizen);
	}

	@ApiOperation("Delete Citizen")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved Citizen"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@DeleteMapping("/{id}")
	public String deleteCitizen(@PathVariable String id) {
		citizenRepo.deleteById(id);
		return id;
	}

}
