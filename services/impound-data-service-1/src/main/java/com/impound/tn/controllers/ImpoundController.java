package com.impound.tn.controllers;

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

import com.impound.tn.models.Impound;
import com.impound.tn.repository.ImpoundRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/impound")
@Api(value = "ImpoundResourceAPI", produces = MediaType.APPLICATION_JSON_VALUE, description = "Impound Resource")
public class ImpoundController {
	@Autowired
	private ImpoundRepository impoundRepo;

	@ApiOperation("Get All Impound")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully get Impound"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@GetMapping("/all")
	public List<Impound> GetImpounds() {
		return impoundRepo.findAll();
	}

	@ApiOperation("Get Impound")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully get Impound by ID"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@GetMapping("/{id}")
	public Impound GetImpound(@PathVariable String id) {
		return impoundRepo.findById(id).orElse(null);
	}
	@ApiOperation("Get Impound by Driver Id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully get Impound by Driver ID"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@GetMapping("/driver/{idDriver}")
	public List<Impound> GetImpoundDriverId(@PathVariable String idDriver) {
		return impoundRepo.findByIdDriver(idDriver);
	}

	@ApiOperation("Add Impound")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully add Impound"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@PostMapping("/")
	public Impound postImpound(@RequestBody Impound Impound) {
		return impoundRepo.save(Impound);
	}

	@ApiOperation("Update Impound")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully update Impound"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@PutMapping("/")
	public Impound putImpound(@RequestBody Impound newImpound) {
		Impound oldImpound = impoundRepo.findById(newImpound.getId()).orElse(null);
		oldImpound.setCin(newImpound.getCin());
		oldImpound.setMatricule(newImpound.getMatricule());
		oldImpound.setTelephone(newImpound.getTelephone());
		oldImpound.setIdDriver(newImpound.getIdDriver());
		oldImpound.setImpoundDate(newImpound.getImpoundDate());
		oldImpound.setReleaseDate(newImpound.getReleaseDate());
		oldImpound.setPaid(newImpound.getPaid());
		oldImpound.setReleased(newImpound.getReleased());
		oldImpound.setIdPlace(newImpound.getIdPlace());
		oldImpound.setPaidDate(newImpound.getPaidDate());

		return impoundRepo.save(oldImpound);
	}
	@ApiOperation("Release")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully Release Impound"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@PutMapping("/release")
	public Impound release(@RequestBody Impound newImpound) {
		Impound oldImpound = impoundRepo.findById(newImpound.getId()).orElse(null);
		oldImpound.setReleaseDate(newImpound.getReleaseDate());
		oldImpound.setReleased(newImpound.getReleased());

		return impoundRepo.save(oldImpound);
	}
	@ApiOperation("Paid")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully Paid Impound"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@PutMapping("/paid")
	public Impound paid(@RequestBody Impound newImpound) {
		Impound oldImpound = impoundRepo.findById(newImpound.getId()).orElse(null);
		oldImpound.setPaid(newImpound.getPaid());
		oldImpound.setPaidDate(newImpound.getPaidDate());

		return impoundRepo.save(oldImpound);
	}

	@ApiOperation("Delete Impound")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved Impound"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@DeleteMapping("/{id}")
	public String deleteImpound(@PathVariable String id) {
		impoundRepo.deleteById(id);
		return id;
	}

}
