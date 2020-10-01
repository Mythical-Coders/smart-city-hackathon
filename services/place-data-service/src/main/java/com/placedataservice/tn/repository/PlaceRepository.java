package com.placedataservice.tn.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.placedataservice.tn.models.Place;

public interface PlaceRepository extends MongoRepository<Place, String> {

}
