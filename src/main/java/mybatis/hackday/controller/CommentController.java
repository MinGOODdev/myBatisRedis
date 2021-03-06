package mybatis.hackday.controller;

import mybatis.hackday.dto.Category;
import mybatis.hackday.dto.Comment;
import mybatis.hackday.dto.Post;
import mybatis.hackday.model.CommentModel;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("board")
public class CommentController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CategoryService categoryService;

    // Create Comment (댓글 등록) *
    @PostMapping("{categoryId}/{postNo}/comment")
    public ResponseEntity<DefaultResponse> createComment(@PathVariable int categoryId, @PathVariable int postNo, @RequestBody CommentModel commentModel) {
        DefaultResponse res = new DefaultResponse();
        Category category = categoryService.findById(categoryId);
        Post post = postService.findByCategoryIdAndNo(category.getId(), postNo);
        commentService.insert(category.getId(), post.getNo(), commentModel);

        res.setData(commentModel);
        res.setMsg("댓글 등록 성공");
        res.setStatusEnum(StatusEnum.SUCCESS);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("{categoryId}/{postNo}/{commentId}")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResponseEntity<DefaultResponse> deleteCommentWithLikes(@PathVariable int categoryId, @PathVariable int postNo, @PathVariable int commentId) throws IllegalAccessException {
        DefaultResponse res = new DefaultResponse();
        Category category = categoryService.findById(categoryId);
        Post post = postService.findByCategoryIdAndNo(category.getId(), postNo);
        Comment comment = commentService.findByCategoryIdAndPostNoAndId(categoryId, postNo, commentId);
        commentService.deleteByCategoryIdAndPostNoAndId(category.getId(), post.getNo(), commentId);

        res.setData(comment);
        res.setMsg("해당 댓글 삭제");
        res.setStatusEnum(StatusEnum.SUCCESS);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
