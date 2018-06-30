package com.ibf.live.dao;

import com.ibf.live.dao.article.ArticleDao;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by kang on 2018/6/29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleTest {
    @Autowired
    private ArticleDao articleDao;

}
