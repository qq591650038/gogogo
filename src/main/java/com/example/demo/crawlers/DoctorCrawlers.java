package com.example.demo.crawlers;

import cn.hutool.json.JSONObject;
import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import com.example.demo.DeptVo;
import com.example.demo.DoctorVo;
import com.example.demo.jpa.DeptResp;
import com.example.demo.jpa.DoctorResp;
import org.seimicrawler.xpath.JXDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: qq895
 * @date: 2019/9/5 19:52
 * @description:
 */

@Crawler(name = "doctor")
public class DoctorCrawlers extends BaseSeimiCrawler {

    @Autowired
    DoctorResp resp;

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
                push(Request.build("http://www.zjyy.com.cn/dept/" + s.toString(), DoctorCrawlers::getDoctor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDoctor(Response response) {
        JXDocument doc = response.document();
        List<Object> doctorUrls = doc.sel("//p[@class='e_pic']/a/@href");
        for (Object doctorUrl : doctorUrls) {
            push(Request.build("http://www.zjyy.com.cn"+doctorUrl.toString().substring(2), DoctorCrawlers::getDoctorDetail));
        }
    }
    
    public void getDoctorDetail(Response response){
        JXDocument doc = response.document();
        String name = doc.selOne("//div[@class='e_pic']/a/img/@name").toString();
        String img = doc.selOne("//div[@class='e_pic']/a/img/@src").toString();
        String id = response.getUrl();
        List<Object> skills = doc.sel("//div[@class='expert_detail']/p");
        List<Object> titles = doc.sel("//div[@class='s_right']/ul/li");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < titles.size()-1; i+=2) {
            String str = titles.get(i).toString().replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "\t\n");
            String str1 = titles.get(i+1).toString().replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "\t\n");
            sb.append(str).append(',').append(str1);
        }

        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < skills.size()-1; i++) {
            String str = skills.get(i).toString().replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "\t\n");
            summary.append(str);
        }

        DoctorVo doctorVo = new DoctorVo();
        doctorVo.setDeptNo(Long.parseLong(id.substring(id.lastIndexOf("=") + 1)));
        doctorVo.setId(Long.parseLong(id.substring(id.indexOf("=") + 1, id.lastIndexOf("&"))));
        doctorVo.setHisId(2119L);
        doctorVo.setName(name);
        doctorVo.setSkill(String.valueOf(skills.get(skills.size()-1)));
        doctorVo.setTitle(sb.toString());
        doctorVo.setPlatformId(2119L);
        doctorVo.setImg(img);
        doctorVo.setSummary(summary.toString());
        resp.save(doctorVo);
        
    }
    
}
