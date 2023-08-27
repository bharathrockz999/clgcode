package com.clg.controller;

import com.clg.entity.Blog;
import com.clg.model.Profile;
import com.clg.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    BlogService blogService;
    @PostMapping("/create")
    public ResponseEntity<Blog> createBlog(@RequestBody Blog blog) {
        Blog blogcreated = blogService.createBlog(blog);
        return ResponseEntity.ok(blogcreated);
    }
    @PostMapping("/update/{blogid}")
    public ResponseEntity<Blog> updateBlog(@PathVariable Integer blogid, @RequestBody Blog blog) {
        Blog blogcreated = blogService.updateBlog(blogid,blog);
        return ResponseEntity.ok(blogcreated);
    }
    @PostMapping("/approve/{blogid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Blog> ApproveBlog(@PathVariable Integer blogid) {
        Blog blogcreated = blogService.approveBlog(blogid);
        return ResponseEntity.ok(blogcreated);
    }
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Blog> > getAllBlogs() {
        List<Blog> blogs = blogService.getBlogs();
        return ResponseEntity.ok(blogs);
    }
    @PostMapping("/addcomment/{blogid}")
    public ResponseEntity<String> createBlog(@PathVariable Integer blogid, @RequestBody Map<String,String> userIdComments) {

       Blog blog = blogService.getBlogById(blogid);
        blogService.addCommentsToBlog(blog,userIdComments);
        return ResponseEntity.ok("successfully added comment");
    }
    @PostMapping("/like/{blogid}")
    public ResponseEntity<String> likeBlog(@PathVariable Integer blogid) {
        Blog blog = blogService.getBlogById(blogid);
        blogService.addLikeToBlog(blog);
        return ResponseEntity.ok("Liked");
    }
    @PostMapping("/like/{blogid}")
    public ResponseEntity<String> unlikeBlog(@PathVariable Integer blogid) {
        Blog blog = blogService.getBlogById(blogid);
        blogService.unlikeToBlog(blog);
        return ResponseEntity.ok("UNLiked");
    }
}
