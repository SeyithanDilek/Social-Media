package com.example.backend.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.backend.dto.PostRequest;
import com.example.backend.dto.PostResponse;
import com.example.backend.exceptions.PostNotFoundException;
import com.example.backend.exceptions.SocialMediaException;
import com.example.backend.exceptions.SubredditNotFoundException;
import com.example.backend.mapper.PostMapper;
import com.example.backend.model.Post;
import com.example.backend.model.Subreddit;
import com.example.backend.model.User;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.SubredditRepository;
import com.example.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Transactional
public class PostService {
	
	private final SubredditRepository subredditRepository;
	private final AuthService authService;
	private final PostRepository postRepository;
	private final PostMapper postMapper;
	private final UserRepository userRepository;
	
	public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SocialMediaException(postRequest.getSubredditName()));
        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
    }
	
	@Transactional
	public PostResponse getPost(Long id) {
		Post post = postRepository.findById(id)
				.orElseThrow(()-> new PostNotFoundException(id.toString()));
		return postMapper.mapToDto(post);
	}
	
	@Transactional
	public List<PostResponse> getAllPosts(){
		return postRepository.findAll()
				.stream()
				.map(postMapper::mapToDto)
				.collect(toList());
	}
	
	@Transactional
	public List<PostResponse> getPostsBySubreddit(Long subredditId){
		Subreddit subreddit = subredditRepository.findById(subredditId)
							  .orElseThrow(()-> new SubredditNotFoundException(subredditId.toString()));
		List<Post> posts= postRepository.findAllBySubreddit(subreddit);
		return posts.stream().map(postMapper::mapToDto).collect(toList());
	}
	
	@Transactional
	public List<PostResponse> getPostsByUsername(String username){
		User user = userRepository.findByUsername(username)
					 .orElseThrow(()-> new UsernameNotFoundException(username));
		return postRepository.findByUser(user)
				.stream().map(postMapper::mapToDto).collect(toList());
	}
}