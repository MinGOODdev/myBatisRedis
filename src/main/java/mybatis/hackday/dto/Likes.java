package mybatis.hackday.dto;

import lombok.Data;

@Data
public class Likes {

    private int id;
    private int categoryId;
    private int postNo;
    private int commentId;
    private int userId;

}
