package com.example.backend.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.backend.dto.VoteDto;
import com.example.backend.exceptions.PostNotFoundException;
import com.example.backend.exceptions.SocialMediaException;
import com.example.backend.model.Post;
import com.example.backend.model.Vote;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.VoteRepository;
import lombok.AllArgsConstructor;
import static com.example.backend.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {
	 private final VoteRepository voteRepository;
	    private final PostRepository postRepository;
	    private final AuthService authService;

	    @Transactional
	    public void vote(VoteDto voteDto) {
	        Post post = postRepository.findById(voteDto.getPostId())
	                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
	        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
	        if (voteByPostAndUser.isPresent() &&
	                voteByPostAndUser.get().getVoteType()
	                        .equals(voteDto.getVoteType())) {
	            throw new SocialMediaException("You have already "
	                    + voteDto.getVoteType() + "'d for this post");
	        }
	        if (UPVOTE.equals(voteDto.getVoteType())) {
	            post.setVoteCount(post.getVoteCount() + 1);
	        } else {
	            post.setVoteCount(post.getVoteCount() - 1);
	        }
	        voteRepository.save(mapToVote(voteDto, post));
	        postRepository.save(post);
	    }

	    private Vote mapToVote(VoteDto voteDto, Post post) {
	        return Vote.builder()
	                .voteType(voteDto.getVoteType())
	                .post(post)
	                .user(authService.getCurrentUser())
	                .build();
	    }

}
