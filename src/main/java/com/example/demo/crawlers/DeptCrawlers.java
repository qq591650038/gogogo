package com.example.demo.crawlers;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import com.example.demo.DeptVo;
import com.example.demo.jpa.DeptResp;
import org.seimicrawler.xpath.JXDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: qq895
 * @date: 2019/9/5 19:52
 * @description:
 */

@Crawler(name = "dept")
public class DeptCrawlers extends BaseSeimiCrawler {
    
    @Autowired
    DeptResp resp;
    
    @Override
    public String[] startUrls() {
        return new String[]{"http://www.zjyy.com.cn/dept/Default.aspx"};
    }

    @Override
    public void start(Response response) {
        JXDocument doc = response.document();
        try {
            List<Object> urls = doc.sel("//a[@class='dept_c_content_a']/@href");
            logger.info("{}", urls.size());
            for (Object s : urls) {
                push(Request.build("http://www.zjyy.com.cn/dept/" + s.toString(), DeptCrawlers::getTitle));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTitle(Response response) {
        JXDocument doc = response.document();
        try {
            String title = doc.selOne("//div[@class='title_left']/text()").toString();
            logger.info("url:{} {}", response.getUrl(), doc.sel("//div[@id='sectionb-1']/div/text()"));
            List<Object> summaryList = doc.sel("//div[@id='sectionb-1']");
            StringBuilder sb = new StringBuilder();
            for (Object summary : summaryList) {
                String sss = summary.toString().replaceAll("<[^>]*>", "\t\n");
                sb.append(sss);
            }
            logger.info("name:{}, summary:{}", title, sb.toString());
            DeptVo deptVo = new DeptVo();
            deptVo.setHisId(2119L);
            deptVo.setName(title);
            deptVo.setSummary(sb.toString());
            deptVo.setPlatformId(2119L);
            deptVo.setNo(Long.parseLong(response.getUrl().substring(response.getUrl().indexOf("=")+1)));
            resp.save(deptVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
