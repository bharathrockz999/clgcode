package com.clg.repository;

import com.clg.entity.Blog;
import com.clg.entity.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepository extends MongoRepository<Blog, Integer> {
}
