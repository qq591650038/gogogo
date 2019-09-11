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
        List<Object> urls = doc.sel("//a[@class='dept_c_content_a']/@href");
        for (Object s : urls) {
            push(Request.build("http://www.zjyy.com.cn/dept/" + s.toString(), DoctorCrawlers::getDoctor));
        }

    }

    public void getDoctor(Response response) {
        JXDocument doc = response.document();
        List<Object> doctorUrls = doc.sel("//p[@class='e_pic']/a/@href");
        for (Object doctorUrl : doctorUrls) {
            push(Request.build("http://www.zjyy.com.cn" + doctorUrl.toString().substring(2), DoctorCrawlers::getDoctorDetail));
        }
    }

    public void getDoctorDetail(Response response) {
        JXDocument doc = response.document();
        String name = doc.selOne("//div[@class='e_pic']/a/img/@name").toString();
        String img = doc.selOne("//div[@class='e_pic']/a/img/@src").toString();
        String id = response.getUrl();
        List<Object> skills = doc.sel("//div[@class='expert_detail']/p");
        List<Object> titles = doc.sel("//div[@class='s_right']/ul/li");
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < titles.size() - 1; i += 2) {
            String str = titles.get(i).toString().replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "\t\n");
            String str1 = titles.get(i + 1).toString().replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "\t\n");
            sb.append(str.trim()).append(',').append(str1.trim());
        }
        String title = sb.toString();
        if(title.endsWith(",")){
            title = title.substring(0, title.length()-1);
        }

        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < skills.size(); i++) {
            String str = skills.get(i).toString().replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "\t\n");
            summary.append(str);
        }
        String skill = String.valueOf(skills.get(skills.size() - 1)).replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "\t\n");
        
        DoctorVo doctorVo = new DoctorVo();
        doctorVo.setDeptNo(Long.parseLong(id.substring(id.lastIndexOf("=") + 1)));
        doctorVo.setNo(Long.parseLong(id.substring(id.indexOf("=") + 1, id.lastIndexOf("&"))));
        doctorVo.setHisId(2119L);
        doctorVo.setName(name);
        doctorVo.setSkill(skill);
        doctorVo.setTitle(title);
        doctorVo.setPlatformId(2119L);
        doctorVo.setImg(img);
        doctorVo.setSummary(summary.toString().trim());
        resp.save(doctorVo);

    }

}
