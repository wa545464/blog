package com.smallchili.blog.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smallchili.blog.dataobject.Article;
import com.smallchili.blog.dataobject.UserDetail;
import com.smallchili.blog.dataobject.UserLogin;
import com.smallchili.blog.dto.ArticleHeadMsgDTO;
import com.smallchili.blog.dto.ArticleUserDetail;
import com.smallchili.blog.dto.Result;
import com.smallchili.blog.error.EmUserError;
import com.smallchili.blog.error.UserException;
import com.smallchili.blog.service.ArticleService;
import com.smallchili.blog.vo.ArticleHeadPageVO;

import top.springdatajpa.zujijpa.Specifications;

/**
* @author xmz
* 2020-03-08
* 
*/
@RestController
@CrossOrigin(allowCredentials = "true" ,allowedHeaders = "*")
public class AticleController extends BaseController{

	@Autowired
	private ArticleService articleService;
	
	
	/**
	 * 2020/3/13
	 * 根据条件动态查询文章
	 * @param article 文章对象
	 * @param page 第几页
	 * @return
	 */
	@GetMapping("/condition/articles")
	public Result<ArticleHeadPageVO> findArticlesByCondition(Article article,
			                 @RequestParam(value = "page",defaultValue="1") Integer page,
			                 HttpSession session){
		
		//构造条件
	     Specification<ArticleUserDetail> spec = Specifications.where(e ->{
	    	 if(article.getBigType() != null){
	    	 e.eq("bigType", article.getBigType());
	    	 }
	    	 if(article.getArticleType()!= null){
	    		e.eq("articleType",article.getArticleType());
	    	 }
	    	 if(article.getUserId() != null){
	    	 e.eq("userId", article.getUserId());
	    	 }
	    	 if(article.getArticleTitle()!= null){
	    		e.like("articleTitle", "%"+article.getArticleTitle()+"%");
	    	}
	     });
		
		//调用查找方法	
        ArticleHeadPageVO pageVO = articleService.findAll(spec, page);
	 
		return   new Result<ArticleHeadPageVO>(EmUserError.SUCCESS,pageVO);
	}
	
	
	
	/**
	 * 插入新文章信息的方法
	 * @param article
	 * @param session
	 * @return
	 */
	@PostMapping("insert/article")
	public Result<Object> insertArticle(Article article ,HttpSession session){
		

		
		//简单校验参数
		if(StringUtils.isEmpty(article.getArticleTitle())
				|| StringUtils.isEmpty(article.getArticleContent())
				|| StringUtils.isEmpty(article.getBigType())
			    || StringUtils.isEmpty(article.getArticleType())
                || StringUtils.isEmpty(article.getUserId())){
						
			throw new UserException(EmUserError.PARAMETER_ERROR);
		}
		
		//调用service新增文章方法
		Article inserArticle = articleService.inserArticle(article);
		if(inserArticle == null){
			throw new UserException(EmUserError.UNKONW_ERROR);
		}
		
		return new Result<Object>(EmUserError.SUCCESS,inserArticle);
	}
	
	
	/**
	 * 更新文章信息
	 * @param article
	 * @param session
	 * @return
	 */
	@PostMapping("update/article")
	public Result<Object> updatetArticle(Article article ,HttpSession session){
		
		//判断用户是否登录
		UserDetail user = (UserDetail) session.getAttribute("user");
		if(user == null){
			throw new UserException(EmUserError.USER_NOT_LOGIN);
		}
		//把session里的userId设置进去
		article.setUserId(user.getUserId());
		
		//简单校验参数
		if(StringUtils.isEmpty(article.getArticleId())
				||StringUtils.isEmpty(article.getArticleTitle())
				|| StringUtils.isEmpty(article.getArticleContent())
				|| StringUtils.isEmpty(article.getBigType())
			    || StringUtils.isEmpty(article.getArticleType())){
						
			throw new UserException(EmUserError.PARAMETER_ERROR);
		}
		
		//调用service新增文章方法
		Article updateArticle = articleService.updateArticle(article);
		if(updateArticle == null){
			throw new UserException(EmUserError.UNKONW_ERROR);
		}
		
		return new Result<Object>(EmUserError.SUCCESS,updateArticle);
	}
	
	
	/**
	 * 
	 * 查找文章信息
	 * @param articleId
	 * @return
	 */
	@GetMapping("article/delail")
	public Object findArticleById(@RequestParam("articleId") Integer articleId){
		
		ArticleUserDetail articleUserDetail = articleService.findArticleById(articleId);
		
		return new Result<ArticleUserDetail>(EmUserError.SUCCESS,articleUserDetail);
	}
	
	
}
