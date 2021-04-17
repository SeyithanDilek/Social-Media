package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Post;
import com.example.backend.model.User;
import com.example.backend.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long>{
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);


}
