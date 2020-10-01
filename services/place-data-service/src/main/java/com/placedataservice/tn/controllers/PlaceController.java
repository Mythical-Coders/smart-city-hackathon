package com.placedataservice.tn.controllers;

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

import com.placedataservice.tn.models.Place;
import com.placedataservice.tn.repository.PlaceRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/place")
@Api(value = "PlaceResourceAPI", produces = MediaType.APPLICATION_JSON_VALUE, description = "Place Resource")
public class PlaceController {
	@Autowired
	private PlaceRepository placeRepo;

	@ApiOperation("Get All Place")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully get Place"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@GetMapping("/all")
	public List<Place> GetPlaces() {
		return placeRepo.findAll();
	}

	@ApiOperation("Get Place")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully get Place by ID"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@GetMapping("/{id}")
	public Place GetPlace(@PathVariable String id) {
		return placeRepo.findById(id).orElse(null);
	}

	@ApiOperation("Add Place")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully add Place"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@PostMapping("/")
	public Place postPlace(@RequestBody Place Place) {
		return placeRepo.save(Place);
	}

	@ApiOperation("Update Place")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully update Place"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@PutMapping("/")
	public Place putPlace(@RequestBody Place newPlace) {
		Place oldPlace = placeRepo.findById(newPlace.getId()).orElse(null);
		oldPlace.setAddress(newPlace.getAddress());
		oldPlace.setPostCode(newPlace.getPostCode());
		oldPlace.setRegion(newPlace.getRegion());
		oldPlace.setLatitude(newPlace.getLatitude());
		oldPlace.setLongitude(newPlace.getLongitude());
		oldPlace.setRegion(newPlace.getRegion());
		oldPlace.setVille(newPlace.getVille());
		return placeRepo.save(oldPlace);
	}

	@ApiOperation("Delete Place")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved Place"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"),
			@ApiResponse(code = 404, message = "The resource  not found") })
	@DeleteMapping("/{id}")
	public String deletePlace(@PathVariable String id) {
		placeRepo.deleteById(id);
		return id;
	}

}
