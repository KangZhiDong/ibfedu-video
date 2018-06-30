package com.ibf.live.service.impl.message;

import com.ibf.live.common.entity.PageEntity;
import com.ibf.live.common.util.DateUtils;
import com.ibf.live.common.util.FileExportImportUtil;
import com.ibf.live.dao.message.MessageLogDao;
import com.ibf.live.entity.message.MessageLog;
import com.ibf.live.service.message.MessageLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service("messageLogService")
public class MessageLogServiceImpl implements MessageLogService {
	
	@Autowired
	private MessageLogDao messageLogDao;
	
	public List<MessageLog> queryLogListPage(MessageLog query, PageEntity page){
		return messageLogDao.queryLogListPage(query, page);
	}

	public List<MessageLog> queryListPage(MessageLog query, PageEntity page) {
		return messageLogDao.queryListPage(query, page);
	}

	@Override
	public List<MessageLog> queryInfoListPage(MessageLog query, PageEntity page) {
		return messageLogDao.queryInfoListPage(query, page);
	}

	@Override
	public List<MessageLog> queryChatPeopleForExport(MessageLog query,PageEntity page) {
		return messageLogDao.queryChatPeopleForExport(query,page);
	}

	@Override
	public List<MessageLog> queryChatSomedayForExport(MessageLog query,PageEntity page) {
		return messageLogDao.queryChatSomedayForExport(query,page);
	}
	
	@Override
	public void ExportAll(HttpServletRequest request, HttpServletResponse response, MessageLog query) throws Exception{
		
		/**导出聊天人员**/
		//指定文件生成路径
		String dir = request.getSession().getServletContext().getRealPath("/excelfile/user");
		//文件名
		String expName1 = DateUtils.formatDate(query.getCreateTime(),"yyyy-MM-dd")+"_具体聊天的人员";
		//表头信息
        String[] headName1 = { "房间号","账号","名称","手机号","QQ","邮箱","性别", "进入时间","离开时间"};
        //拆分为一万条数据每Excel，防止内存使用太大
		PageEntity page1=new PageEntity();
		page1.setPageSize(10000);
		queryChatPeopleForExport(query, page1);
		int num1=page1.getTotalPageSize();//总页数
		List<File> srcfile = new ArrayList<File>();//生成的excel的文件的list
		for(int i=1;i<=num1;i++){//循环生成num个xls文件
			page1.setCurrentPage(i);
			List<MessageLog> msgLogList=queryChatPeopleForExport(query, page1);
			List<List<String>> list=ListExportJoint(msgLogList);
			File file = FileExportImportUtil.createExcel(headName1, list, expName1+"_"+i,dir);
			srcfile.add(file);
		}
        /**导出人员的聊天记录**/
      //指定文件生成路径
		//文件名
		String expName2 = DateUtils.formatDate(query.getCreateTime(),"yyyy-MM-dd")+"_所有进出聊天记录";
		//表头信息
        String[] headName2 = { "时间","房间号","账号", "名称","手机号","QQ","邮箱","性别","消息内容"};
        //拆分为一万条数据每Excel，防止内存使用太大
		PageEntity page2=new PageEntity();
		page2.setPageSize(10000);
		queryChatSomedayForExport(query, page2);
		int num2=page2.getTotalPageSize();//总页数
		for(int i=1;i<=num2;i++){//循环生成num个xls文件
			page2.setCurrentPage(i);
			List<MessageLog> msgLogList=queryChatSomedayForExport(query, page2);
			List<List<String>> list=InfoJoint(msgLogList);
			File file = FileExportImportUtil.createExcel(headName2, list, expName2+"_"+i,dir);
			srcfile.add(file);
		}
        FileExportImportUtil.createRar(response, dir, srcfile, DateUtils.formatDate(query.getCreateTime(),"yyyy-MM-dd")+"_房间号"+query.getRoomid()+"直播情况");//生成的多excel的压缩包
	}
	
	/**
	 * excel格式拼接
	 * @return
	 */
	public List<List<String>> ListExportJoint(List<MessageLog> msgLogList){
		List<List<String>> list = new ArrayList<List<String>>();
		for (int i = 0; i < msgLogList.size(); i++) {
			// String[] headName = { "房间号","账号","名称","手机号","QQ","邮箱","性别", "进入时间","离开时间"};
			List<String> small = new ArrayList<String>();
			small.add(msgLogList.get(i).getRoomid()+"");
			small.add(msgLogList.get(i).getUserName());
			small.add(msgLogList.get(i).getShowName());
			small.add(msgLogList.get(i).getMobile());
			small.add(msgLogList.get(i).getQq());
			small.add(msgLogList.get(i).getEmail());
			if (msgLogList.get(i).getSex() == 0) {
				small.add("保密");
			} else if (msgLogList.get(i).getSex() == 1) {
				small.add("男");
			} else {
				small.add("女");
			}
			if(msgLogList.get(i).getJoinTime()==null){
				small.add("");
			}else{
				small.add(DateUtils.formatDate(msgLogList.get(i).getJoinTime(), "yyyy-MM-dd HH:mm:ss") );
			}
			if(msgLogList.get(i).getLeftTime()==null){
				small.add("");
			}else{
				small.add(DateUtils.formatDate(msgLogList.get(i).getLeftTime(), "yyyy-MM-dd HH:mm:ss") );
			}
			list.add(small);
		}
		return list;
	}

	/**
	 * excel格式拼接
	 * @return
	 */
	public List<List<String>> InfoJoint(List<MessageLog> msgLogList){
		List<List<String>> list = new ArrayList<List<String>>();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (int i = 0; i < msgLogList.size(); i++) {
			// String[] headName2 = { "时间","房间号","账号", "名称","手机号","QQ","邮箱","性别","消息内容"};
			List<String> small = new ArrayList<String>();
			small.add(format.format(msgLogList.get(i).getCreateTime()));
			small.add(msgLogList.get(i).getRoomid()+"");
			small.add(msgLogList.get(i).getUserName());
			small.add(msgLogList.get(i).getShowName());
			small.add(msgLogList.get(i).getMobile());
			small.add(msgLogList.get(i).getQq());
			small.add(msgLogList.get(i).getEmail());
			if (msgLogList.get(i).getSex() == 0) {
				small.add("保密");
			} else if (msgLogList.get(i).getSex() == 1) {
				small.add("男");
			} else {
				small.add("女");
			}
			small.add(msgLogList.get(i).getMsg());
			list.add(small);
		}
		return list;
	}
}
