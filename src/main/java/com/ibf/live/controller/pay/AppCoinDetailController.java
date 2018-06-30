package com.ibf.live.controller.pay;

import com.ibf.live.common.controller.BaseController;
import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.common.util.SingletonLoginUtils;
import com.ibf.live.entity.pay.CoinDetail;
import com.ibf.live.service.pay.CoinDetailService;
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

@Controller
@RequestMapping("/api/pay")
public class AppCoinDetailController extends BaseController {
	//private static Logger logger = Logger.getLogger(AppCoinDetailController.class);
	private static Logger logger = LoggerFactory.getLogger(AppCoinDetailController.class);
	@Autowired
	private CoinDetailService coinDetailService;
	/**
	 * 直播间账单列表
	 */
	@RequestMapping("/getCoinDetail")
	@ResponseBody
	public Map<String, Object> getCoinDetail(HttpServletRequest request) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			String currentPage = request.getParameter("currentPage");// 当前页
			if (currentPage == null || currentPage.trim().equals("")) {
				json = this.setJson(false, "页码不能为空", null);
				return json;
			}
			PageEntity page = new PageEntity();
			page.setCurrentPage(Integer.parseInt(currentPage));// 当前页

			page.setPageSize(20);// 每页多少条数据
			String pageSize = request.getParameter("pageSize");
			if (pageSize != null) {
				page.setPageSize(Integer.parseInt(pageSize));
			}
			CoinDetail coin = new CoinDetail();
			String userId = SingletonLoginUtils.getLoginUserId(request)+"";
			coin.setTouid(userId);
			
			String startDate = request.getParameter("startDate");//开始时间
			if (startDate != null && !startDate.trim().equals("")) {
				coin.setStartDate(startDate);
			}
			String endDate = request.getParameter("endDate");//开始时间
			if (endDate != null &&!endDate.trim().equals("")) {
				coin.setEndDate(endDate);
			}
			String sortColumn = request.getParameter("sortColumn");//排序
			if (sortColumn != null && !sortColumn.trim().equals("")) {
				coin.setSort(sortColumn);
				Boolean sortDesc = Boolean.parseBoolean(request.getParameter("sortDesc"));//排序
				if(sortDesc){
					coin.setIsDesc(1);
	            }else{
	            	coin.setIsDesc(0);
	            }
			}
			List<CoinDetail> coinList = coinDetailService.queryCoinListPage(coin, page);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("coinlist", coinList);
			map.put("page", page);
			json = this.setJson(true, "成功", map);
		} catch (Exception e) {
			json = this.setJson(false, "异常", null);
			logger.error("showCourseList()--error", e);
		}
		return json;
	}
}
