package com.example.backend.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.dto.CommentsDto;
import com.example.backend.service.CommentService;
import lombok.AllArgsConstructor;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {

	private final CommentService commentService;
	
	@PostMapping
	public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto){
		commentService.createComment(commentsDto);
		return new ResponseEntity<>(HttpStatus.CREATED);	
	}
	
	@GetMapping("/by-user/{postId}")
	public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@RequestParam("postId") Long postId){
		return status(OK).body(commentService.getCommentsByPost(postId));
	}
	
	@GetMapping("/by-user/{username}")
	public ResponseEntity<List<CommentsDto>> getAllCommentsByUser(@RequestParam("username")String username){
		return status(OK).body(commentService.getCommentsByUser(username));
	}
	
}