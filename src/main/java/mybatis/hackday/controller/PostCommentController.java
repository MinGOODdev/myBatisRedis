package mybatis.hackday.controller;

import mybatis.hackday.dto.Category;
import mybatis.hackday.dto.Comment;
import mybatis.hackday.dto.Post;
import mybatis.hackday.model.DefaultResponse;
import mybatis.hackday.model.StatusEnum;
import mybatis.hackday.service.CategoryService;
import mybatis.hackday.service.CommentService;
import mybatis.hackday.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("board")
public class PostCommentController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CategoryService categoryService;

    // Get all comment in post
    @GetMapping("{categoryId}/{postNo}")
    public ResponseEntity<DefaultResponse> commentListByPost(@PathVariable int categoryId, @PathVariable int postNo) {
        DefaultResponse res = new DefaultResponse();
        Category category = categoryService.findById(categoryId);
        Post post = postService.findByCategoryIdAndNo(category.getId(), postNo);
        postService.updateHit(post);
        List<Comment> commentList = commentService.findByCategoryIdAndPostNo(category.getId(), post.getNo());

        if(commentList.size() == 0) {
            res.setData(post);
            res.setMsg("해당 카테고리의 선택된 게시글 내용");
            res.setStatusEnum(StatusEnum.SUCCESS);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        else {
            res.setData(commentList);
            res.setMsg("각각의 게시글 전체 내용(댓글 포함)");
            res.setStatusEnum(StatusEnum.SUCCESS);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
    }

}
