package com.clg.service;

import com.clg.dto.PagableResponse;
import com.clg.entity.Blog;
import com.clg.entity.UserInfo;
import com.clg.model.Profile;
import com.clg.repository.BlogRepository;
import com.clg.sequence.SequenceGeneratorService;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Timestamp;
import java.util.*;

@Service
public class BlogService {

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;
    public Blog createBlog(Blog blog) {
        blog.setId(sequenceGeneratorService.generateSequence(Blog.SEQUENCE_NAME));
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        blog.setCrtdBy(username);
        blog.setCrtdTme(new Date());
       return blogRepository.save(blog);
    }

    public Blog updateBlog(Integer blogid, Blog blog) {
        Optional<Blog> existingOptional = blogRepository.findById(blogid);
        Blog existingBlog = existingOptional.get();
        existingBlog.setSub(blog.getSub());
        existingBlog.setDesc(blog.getDesc());
        existingBlog.setShortIntro(blog.getShortIntro());
        existingBlog.setStatus(blog.getStatus());
        existingBlog.setVisibility(blog.getVisibility());
        existingBlog.setCategories(blog.getCategories());
        existingBlog.setStartDate(blog.getStartDate());
        existingBlog.setEndDate(blog.getEndDate());
        existingBlog.setAttachments(blog.getAttachments());
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        existingBlog.setCrtdBy(username);
        existingBlog.setCrtdTme(new Date());
        blogRepository.save(existingBlog);
        return existingBlog;
    }
    public Blog approveBlog(Integer blogid) {
        Optional<Blog> existingOptional = blogRepository.findById(blogid);
        Blog existingBlog = existingOptional.get();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        existingBlog.setApprovedBy(username);
        existingBlog.setApprvdTme(new Date());
        existingBlog.setApproved(true);
        blogRepository.save(existingBlog);
        return existingBlog;
    }

    public List<Blog> getBlogs() {
        return blogRepository.findAll();
    }
    public Blog getBlogById(Integer blogid) {
        Optional<Blog> existingOptional = blogRepository.findById(blogid);
        return existingOptional.get();
    }

    public void addCommentsToBlog(Blog blog, Map<String,String> comment){
        blog.getComments().putAll(comment);
        blogRepository.save(blog);
    }
    public void addLikeToBlog(Blog blog){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        blog.getUnlikes().remove(username);
        Set<String> likes = blog.getLikes();
        likes.add(username);
        blogRepository.save(blog);
    }
    public void unlikeToBlog(Blog blog){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        blog.getLikes().remove(username);
        Set<String> unLikes = blog.getUnlikes();
        unLikes.add(username);
        blogRepository.save(blog);
    }

    public PagableResponse getBlogsPagable(int pageNumber, int numberOfRecords, String sorting,String userName,String approvedStatus) {
        Page<Blog> page = null;
        Pageable pageable = PageRequest.of(pageNumber, numberOfRecords, Sort.by(sorting).descending());
        if(userName.equalsIgnoreCase("all")){
            page = blogRepository.findByApproved("true".equalsIgnoreCase(approvedStatus),pageable);
        }else{
            page = blogRepository.findByApprovedAndCrtdBy("true".equalsIgnoreCase(approvedStatus),userName,pageable);
        }
        PagableResponse response = new PagableResponse();
        response.setResponse(page.stream().toList());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setCurrentPage(page.getNumber());
        return response ;
    }

    public PagableResponse getBlogsPagableByCategory(int pageNumber, int numberOfRecords, String sorting,String userName,List<String> category,Boolean approved) {
        Page<Blog> page = null;
        Pageable pageable = PageRequest.of(pageNumber, numberOfRecords, Sort.by(sorting).descending());
        if(userName.equalsIgnoreCase("all")){
            page = blogRepository.findByApprovedAndCategoriesIn(approved,category,pageable);
        }else{
            page = blogRepository.findByApprovedAndCrtdByAndCategoriesIn(approved,userName,category,pageable);
        }
        PagableResponse response = new PagableResponse();
        response.setResponse(page.stream().toList());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setCurrentPage(page.getNumber());
        return response ;
    }

    public Blog rejectBlog(Integer blogid) {
        Optional<Blog> existingOptional = blogRepository.findById(blogid);
        Blog existingBlog = existingOptional.get();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        existingBlog.setApprovedBy(username);
        existingBlog.setApprvdTme(new Date());
        existingBlog.setApproved(false);
        blogRepository.save(existingBlog);
        return existingBlog;
    }

    public void deleteBlog(Integer blogid) {
        blogRepository.deleteById(blogid);
    }
    public void deleteComment(Blog blog, String userName) {
        blog.getComments().remove(userName);
        blogRepository.save(blog);
    }
}
