package com.happybot.vcoupon.model.retrofit;

import com.happybot.vcoupon.model.Comment;

import java.util.List;

/**
 * Created by Nguyễn Phương Tuấn on 18-Jan-17.
 */

public class CommentListResponse extends ResponseObject {
    List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }
}