package com.clg.repository;

import com.clg.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepository extends MongoRepository<Blog, Integer> {
    Page<Blog> findByCrtdBy(String crtdBy, Pageable pageable);
}
