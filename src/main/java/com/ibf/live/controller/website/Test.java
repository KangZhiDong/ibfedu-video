package com.ibf.live.controller.website;

import com.ibf.live.common.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kang on 2018/6/22.
 */
@Controller
@RequestMapping("/api/website")
public class Test extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(Test.class);

    @RequestMapping("/test")
    @ResponseBody
    public Map<String, Object> test(HttpServletRequest request) {
        Map<String, Object> json = new HashMap<String, Object>();

        try{
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("hello", "world");

            json=this.setJson(true, "成功", map);

        }catch(Exception e){
            json=this.setJson(false, "异常", null);
            logger.error("toSubjectList()--error",e);
        }
		return json;


    }
}
