package com.clg.repository;

import com.clg.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BlogRepository extends MongoRepository<Blog, Integer> {
    Page<Blog> findByCrtdBy(String crtdBy, Pageable pageable);
    Page<Blog> findByCategories(String category, Pageable pageable);
    Page<Blog> findByCrtdByAndCategoriesIn(String crtdBy, List<String> category, Pageable pageable);
    Page<Blog> findByCategoriesIn(List<String> category, Pageable pageable);

    Page<Blog> findByApprovedAndCategoriesIn(boolean b, List<String> category, Pageable pageable);

    Page<Blog> findByApprovedAndCrtdByAndCategoriesIn(boolean b, String userName, List<String> category, Pageable pageable);

    Page<Blog> findByApproved(boolean b, Pageable pageable);

    Page<Blog> findByApprovedAndCrtdBy(boolean b, String userName, Pageable pageable);
}
