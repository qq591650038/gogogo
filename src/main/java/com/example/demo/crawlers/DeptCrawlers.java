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
        List<Object> urls = doc.sel("//a[@class='dept_c_content_a']/@href");
        for (Object s : urls) {
            push(Request.build("http://www.zjyy.com.cn/dept/" + s.toString(), DeptCrawlers::getDept));
        }
    }

    public void getDept(Response response) {
        JXDocument doc = response.document();
        String title = doc.selOne("//div[@class='title_left']/text()").toString();
        List<Object> summaryList = doc.sel("//div[@id='sectionb-1']");
        StringBuilder sb = new StringBuilder();
        for (Object summary : summaryList) {
            String sss = summary.toString().replaceAll("<[^>]*>", "\t\n");
            sb.append(sss.trim());
        }
        DeptVo deptVo = new DeptVo();
        deptVo.setHisId(2119L);
        deptVo.setName(title);
        deptVo.setSummary(sb.toString().trim());
        deptVo.setPlatformId(2119L);
        deptVo.setPid("0");
        deptVo.setNo(Long.parseLong(response.getUrl().substring(response.getUrl().indexOf("=") + 1)));
        resp.save(deptVo);
    }
}
