package com.smallchili.blog.dataobject;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

/**
 * 文章评论表的实体类
 *
 */
@Entity
@Data
public class ArticleComment {
    
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer commentId;
	
	private Integer userId;
	
	private Integer articleId;
	
	private String commentContent;
	
	private Integer commentStar;
	
	private Integer commentStatus;
	
	private Date createTime;

	@ManyToOne
	@JoinColumn(name="userId",insertable= false,updatable = false)
	private UserDetail replyer;
	
	
}
