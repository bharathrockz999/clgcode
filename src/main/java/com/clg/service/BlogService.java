package com.clg.service;

import com.clg.dto.PagableResponse;
import com.clg.entity.Blog;
import com.clg.entity.MonthlyCount;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BlogService {

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    void setmapkeydotreplacement(MappingMongoConverter mappingmongoconverter) {
        mappingmongoconverter.setMapKeyDotReplacement("_");
    }

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

    public void addCommentsToBlog(Blog blog,String username,String comment){
        blog.getComments().put(username,comment);
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

    public PagableResponse getBlogsPagableByCategory(int pageNumber, int numberOfRecords, String sorting,String userName,List<String> category,List<Boolean> approved,List<String> statusIn, List<Boolean> visibility) {
        Page<Blog> page = null;
        Pageable pageable = PageRequest.of(pageNumber, numberOfRecords, Sort.by(sorting).descending());
        if(userName.equalsIgnoreCase("all")){
            page = blogRepository.findByApprovedInAndCategoriesInAndStatusInAndVisibilityIn(approved,category,pageable,statusIn,visibility);
        }else{
            page = blogRepository.findByApprovedInAndCrtdByAndCategoriesInAndStatusInAndVisibilityIn(approved,userName,category,pageable,statusIn,visibility);
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
    public Map<Integer, Long> countDocumentsByMonth(String startDate, String endDate, String email) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date  from = simpleDateFormat.parse(startDate);
        Date  to = simpleDateFormat.parse(endDate);
        List<Blog> documents = blogRepository.findByCrtdTmeBetweenAndCrtdBy(from, to,email);
        Map<Integer, Long> countsByMonth = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        for (Blog document : documents) {
            calendar.setTime(document.getCrtdTme());
            int month = calendar.get(Calendar.MONTH)+1;
            countsByMonth.put(month, countsByMonth.getOrDefault(month, 0L) + 1);
        }
        return countsByMonth;
    }

    public void requestBlog(Blog blogcreated, String mailId) {
        blogcreated.getProjectRequests().put(mailId,false);
        blogRepository.save(blogcreated);
    }

    public void requestApproveBlog(Blog blogcreated, String mailId) {
        blogcreated.getProjectRequests().put(mailId,true);
        blogRepository.save(blogcreated);
    }

    public Map<String, Object> getApporvedAndUnApproved(String mailId) {
        Map<String, Object> map = new HashMap<>();
        List<Blog> blogs = blogRepository.findAll();
        List<Blog> top5LikedBlogs = new ArrayList<>();
        List<Blog> top5UnLikedBlogs = new ArrayList<>();
        Collections.sort(blogs, new Comparator<Blog>() {
            @Override
            public int compare(Blog list1, Blog list2) {
                return Integer.compare(list2.getLikes().size(), list2.getLikes().size()); // Sort in descending order
            }
        });
        for(int i = 0 ; i<=4 ; i++){
            top5LikedBlogs.add(blogs.get(i));

        }
        Collections.sort(blogs, new Comparator<Blog>() {
            @Override
            public int compare(Blog list1, Blog list2) {
                return Integer.compare(list2.getUnlikes().size(), list2.getUnlikes().size()); // Sort in descending order
            }
        });
        for(int i = 0 ; i<=4 ; i++){
            top5UnLikedBlogs.add(blogs.get(i));

        }
        List<Blog> requested = new ArrayList<>();
        List<Blog> approved = new ArrayList<>();
        for(Blog blog : blogs){
                    Map<String,Boolean> requests = blog.getProjectRequests();
                    if(requests.get(mailId) != null){
                        if(requests.get(mailId)){
                            approved.add(blog);     
                        }else{
                            requested.add(blog);
                        }         
                    }
        }
        map.put("approved",approved);
        map.put("approved count", approved.size());
        map.put("pending count", requested.size());
        map.put("pending",requested);
        map.put("top5liked",top5LikedBlogs);
        map.put("top5Unliked",top5UnLikedBlogs);
        return  map;
    }
}
