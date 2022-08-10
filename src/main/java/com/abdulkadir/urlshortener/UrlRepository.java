package com.abdulkadir.urlshortener;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UrlRepository extends MongoRepository<Url,String> {
    @Query("{'abdulkadir': ?0}")
    Optional<Url> findByHashUrl(String s);

}
