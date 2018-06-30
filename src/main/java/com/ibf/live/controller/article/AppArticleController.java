package com.ibf.live.controller.article;

import com.github.pagehelper.PageHelper;
import com.ibf.live.common.controller.BaseController;
import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.entity.article.Article;
import com.ibf.live.entity.article.QueryArticle;
import com.ibf.live.service.article.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author www.inxedu.com
 */
@Controller
@RequestMapping("/api/article")
public class AppArticleController extends BaseController {
	//private static Logger logger = Logger.getLogger(AppArticleController.class);
	private static Logger logger = LoggerFactory.getLogger(AppArticleController.class);

	@Autowired
	private ArticleService articleService;


	/**
	 * 文字列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> list(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			String currentPage = request.getParameter("currentPage");// 当前页
			if (currentPage == null || currentPage.trim().equals("")) {
				json = this.setJson(false, "页码不能为空", null);
				return json;
			}

			PageEntity page = new PageEntity();
			page.setPageSize(8);// 每页多少条数据
			page.setCurrentPage(1);
			/*查询所有文章*/
			QueryArticle queryArticle = new QueryArticle();
			queryArticle.setType(2);// 文章类型 2文章

			String pageSize = request.getParameter("pageSize");

			if(Integer.parseInt(currentPage)>0){
				page.setCurrentPage(Integer.parseInt(currentPage));// 当前页
			}

			if (pageSize != null) {
				page.setPageSize(Integer.parseInt(pageSize));
				PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
			}else {
				PageHelper.startPage(Integer.parseInt(currentPage), 8);
			}
			List<Article> articleList = articleService.queryArticlePage(queryArticle);
			/*查询热门文章 按浏览量排行*/
			QueryArticle query = new QueryArticle();
			query.setType(2);
			query.setCount(8);
			query.setOrderby(1);
			int cout1= articleService.queryAllArticleCount();
			logger.info(Integer.toString(cout1));
			List<Article> hotArticleList = articleService.queryArticleList(query);
			/*显示广告图*/
		//	Map<String, List<WebsiteImages>> websiteImages = websiteImagesService.queryImagesByType();
			Map<String, Object> map = new TreeMap<String, Object>();
			map.put("articleList", articleList);
			map.put("hotArticleList", hotArticleList);
			map.put("page", page);
			json = this.setJson(true, "成功", map);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("toSubjectList()--error", e);
		}
		return json;
	}

	/**
	 * 文章详情
	 */
	@RequestMapping("/info")
	@ResponseBody
	public Map<String, Object> articleInfo(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			String id = request.getParameter("id");// 文章Id
			if (id == null || id.trim().equals("")) {
				json = this.setJson(false, "id不能为空", null);
				return json;
			}
			/*修改文章点击量*/
			Map<String, String> updateMap = new HashMap<String, String>();
			updateMap.put("num", "+1");
			updateMap.put("type", "clickNum");
			updateMap.put("articleId", id + "");
			articleService.updateArticleNum(updateMap);
			/*查询文章详情*/
			Article article = articleService.queryArticleById(Integer.parseInt(id));
			/*查询文章内容*/
			String content = articleService.queryArticleContentByArticleId(Integer.parseInt(id));
			/*查询热门文章 按浏览量排行*/
			QueryArticle query = new QueryArticle();
			query.setType(2);
			query.setCount(8);
			query.setOrderby(1);
			List<Article> hotArticleList = articleService.queryArticleList(query);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("article", article);
			map.put("hotArticleList", hotArticleList);
			map.put("content", content);
			json = this.setJson(true, "成功", map);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("articleInfo()--error", e);
		}
		return json;
	}

}
