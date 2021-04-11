package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Comment;
import com.example.backend.model.Post;
import com.example.backend.model.User;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	List<Comment> findByPost(Post post);
	
	List<Comment> findAllByUser(User user);

}
