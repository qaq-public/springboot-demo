package com.qaq.demo.models.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.qaq.demo.models.User;

public interface UserRepository extends MongoRepository<User, String> {
}
