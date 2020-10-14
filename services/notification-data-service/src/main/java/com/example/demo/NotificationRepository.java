package com.example.demo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String>{
	List<Notification> findByIdReceiver(String idReceiver);
}
