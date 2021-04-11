package com.example.backend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.dto.SubredditDto;
import com.example.backend.exceptions.SocialMediaException;
import com.example.backend.mapper.SubredditMapper;
import com.example.backend.model.Subreddit;
import com.example.backend.repository.SubredditRepository;
import java.util.List;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class SubredditService {
	
	   private final SubredditRepository subredditRepository;
	    private final SubredditMapper subredditMapper;

	    @Transactional
	    public SubredditDto save(SubredditDto subredditDto) {
	        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
	        subredditDto.setId(save.getId());
	        return subredditDto;
	    }

	    @Transactional(readOnly = true)
	    public List<SubredditDto> getAll() {
	        return subredditRepository.findAll()
	                .stream()
	                .map(subredditMapper::mapSubredditToDto)
	                .collect(toList());
	    }

	    public SubredditDto getSubreddit(Long id) {
	        Subreddit subreddit = subredditRepository
	        		.findById(id)
	                .orElseThrow(() -> new SocialMediaException("No subreddit found with ID - " + id));
	        return subredditMapper.mapSubredditToDto(subreddit);
	    }
	}
