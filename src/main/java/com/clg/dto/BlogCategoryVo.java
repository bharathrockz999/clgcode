package com.clg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogCategoryVo {
    Map<String,String> param;
    List<String> category;
    Boolean approvedStatus;
}
