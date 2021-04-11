package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Subreddit;

public interface SubredditRepository extends JpaRepository<Subreddit, Long>{
	
	Optional<Subreddit> findByName(String name);

}
