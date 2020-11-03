package com.yakovlev.Algorithms.Patterns.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.yakovlev.Algorithms.Patterns.domain.Message;

public interface MessageRepo extends CrudRepository<Message, Long> {

	List<Message> findByTag(String tag);
}
