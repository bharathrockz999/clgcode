package com.clg.repository;

import com.clg.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
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

    Page<Blog> findByApprovedAndCategoriesInAndStatusInAndVisibilityIn(Boolean approved, List<String> category, Pageable pageable, List<String> statusIn, List<Boolean> visibility);

    Page<Blog> findByApprovedAndCrtdByAndCategoriesInAndStatusInAndVisibilityIn(Boolean approved, String userName, List<String> category, Pageable pageable, List<String> statusIn, List<Boolean> visibility);

    Page<Blog> findByApprovedInAndCategoriesInAndStatusInAndVisibilityIn(List<Boolean> approved, List<String> category, Pageable pageable, List<String> statusIn, List<Boolean> visibility);

    Page<Blog> findByApprovedInAndCrtdByAndCategoriesInAndStatusInAndVisibilityIn(List<Boolean> approved, String userName, List<String> category, Pageable pageable, List<String> statusIn, List<Boolean> visibility);

    List<Blog> findByCrtdTmeBetweenAndCrtdBy(Date startDate, Date endDate, String email);
}
