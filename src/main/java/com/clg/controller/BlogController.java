package com.clg.controller;

import com.clg.dto.BlogCategoryVo;
import com.clg.dto.PagableResponse;
import com.clg.entity.Blog;
import com.clg.model.Profile;
import com.clg.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
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

    @DeleteMapping("/delete/{blogid}")
    public ResponseEntity<String> deleteBlog(@PathVariable Integer blogid) {
         blogService.deleteBlog(blogid);
        return ResponseEntity.ok("deleted");
    }

    @PostMapping("/approve/{blogid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Blog> ApproveBlog(@PathVariable Integer blogid) {
        Blog blogcreated = blogService.approveBlog(blogid);
        return ResponseEntity.ok(blogcreated);
    }
    @PostMapping("/reject/{blogid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Blog> rejectBlog(@PathVariable Integer blogid) {
        Blog blogcreated = blogService.rejectBlog(blogid);
        return ResponseEntity.ok(blogcreated);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Blog> > getAllBlogs() {
        List<Blog> blogs = blogService.getBlogs();
        return ResponseEntity.ok(blogs);
    }
    @PostMapping("/addcomment/{blogid}/{comment}")
    public ResponseEntity<String> createBlog(@PathVariable Integer blogid,@PathVariable String comment) {
       Blog blog = blogService.getBlogById(blogid);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        blogService.addCommentsToBlog(blog,username,comment);
        return ResponseEntity.ok("successfully added comment");
    }
    @PostMapping("/like/{blogid}")
    public ResponseEntity<String> likeBlog(@PathVariable Integer blogid) {
        Blog blog = blogService.getBlogById(blogid);
        blogService.addLikeToBlog(blog);
        return ResponseEntity.ok("Liked");
    }
    @PostMapping("/unlike/{blogid}")
    public ResponseEntity<String> unlikeBlog(@PathVariable Integer blogid) {
        Blog blog = blogService.getBlogById(blogid);
        blogService.unlikeToBlog(blog);
        return ResponseEntity.ok("unliked");
    }
    @PostMapping("/page/get")
    public ResponseEntity<PagableResponse> getPagableBlogs(@RequestBody Map<String,String> param ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
       String userName =  param.get("userName") == null ?  username : param.get("userName");
        return ResponseEntity.ok(blogService.getBlogsPagable(Integer.parseInt(param.get("pageNumber")), Integer.parseInt(param.get("noOfRecords")), "crtdTme",userName,param.get("approvedStatus")));
    }

    @PostMapping("/page/get/category")
    public ResponseEntity<PagableResponse> getPagableBlogsByCategory(@RequestBody BlogCategoryVo blogCategoryVo) {
        Map<String,String> param = blogCategoryVo.getParam();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        String userName =  param.get("userName") == null ?  username : param.get("userName");
        return ResponseEntity.ok(blogService.getBlogsPagableByCategory(Integer.parseInt(param.get("pageNumber")), Integer.parseInt(param.get("noOfRecords")), "crtdTme",userName,blogCategoryVo.getCategory(),blogCategoryVo.getApprovedStatus(),blogCategoryVo.getStatus(),blogCategoryVo.getVisibility()));
    }

    @DeleteMapping("/deleteComment/{blogid}/{userName}")
    public ResponseEntity<String> deleteComment(@PathVariable Integer blogid, @PathVariable String userName) {
        Blog blog = blogService.getBlogById(blogid);
        blogService.deleteComment(blog,userName);
        return ResponseEntity.ok("successfully deleted comment");
    }

    @GetMapping("/dashboard/year/{fromYear}/{createdBy}")
    public ResponseEntity<Map<Integer, Long>> dashboard(@PathVariable Integer fromYear, @PathVariable String createdBy) throws ParseException {
        Map<Integer, Long> monthCount =  blogService.countDocumentsByMonth(fromYear+"-01-01 00:00:00",fromYear+"-12-31 23:59:59",createdBy);
       return ResponseEntity.ok(monthCount);
    }

    @GetMapping("/request/{blogId}")
    public ResponseEntity<String> requestBlog(@PathVariable Integer blogId) {
        Blog blogcreated = blogService.getBlogById(blogId);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        blogService.requestBlog(blogcreated,username);
        return ResponseEntity.ok("requested success");
    }

    @GetMapping("/approverequest/{blogId}/{mailId}")
    public ResponseEntity<String> requestApproveBlog(@PathVariable Integer blogId, @PathVariable String mailId) {
        Blog blogcreated = blogService.getBlogById(blogId);
        blogService.requestApproveBlog(blogcreated,mailId);
        return ResponseEntity.ok("approved project requet");
    }
    @GetMapping("/getapprovedAndUnApproved")
    public ResponseEntity<Map<String,Object>> getApprovUnApprovedeBlog() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Map<String,Object> approvedAndRejectedCount = blogService.getApporvedAndUnApproved(username);
        return ResponseEntity.ok(approvedAndRejectedCount);
    }


}
