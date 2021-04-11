package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Post;
import com.example.backend.model.Subreddit;
import com.example.backend.model.User;

public interface PostRepository extends JpaRepository<Post, Long> {
	
	
	List<Post> findAllBySubreddit(Subreddit subreddit);
	
	List<Post> findByUser(User user);

}
