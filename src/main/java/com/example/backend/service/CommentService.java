package com.example.backend.service;

import java.util.List;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.dto.CommentsDto;
import com.example.backend.exceptions.PostNotFoundException;
import com.example.backend.mapper.CommentMapper;
import com.example.backend.model.Comment;
import com.example.backend.model.NotificationEmail;
import com.example.backend.model.Post;
import com.example.backend.model.User;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {
	
	private final CommentMapper commentMapper;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private static final String POST_URL= "";
	private MailContentBuilder mailContentBuilder;
	private final MailService mailService;
	
	public void createComment(CommentsDto commentsDto) {
		Post post= postRepository.findById(commentsDto.getId())
				.orElseThrow(()-> new PostNotFoundException(commentsDto.getPostId().toString()));
		Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
		commentRepository.save(comment);
		String message = mailContentBuilder.build(post.getUser().getUsername() +"posted a comment on your post."
				+POST_URL);
		sendCommentNotification(message, post.getUser());
	}
	
	private void sendCommentNotification(String message , User user) {
		mailService.sendMail(new NotificationEmail(user.getUsername() + 
				"commented on your post",user.getEmail(),message ));
	}
	
	public List<CommentsDto> getCommentsByPost(Long postId){
		Post post= postRepository.findById(postId).orElseThrow(()-> 
				new PostNotFoundException(postId.toString()));
		return commentRepository.findByPost(post).stream()
								.map(commentMapper::mapToDto).collect(toList());
		
	}
	
	public List<CommentsDto> getCommentsByUser(String username){
		User user = userRepository.findByUsername(username)
					.orElseThrow(()-> new UsernameNotFoundException(username));
		return commentRepository.findAllByUser(user)
				.stream()
				.map(commentMapper::mapToDto)
				.collect(toList());
		
	}
}