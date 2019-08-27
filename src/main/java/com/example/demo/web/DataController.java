package com.example.demo.web;

import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpUtil;
import com.example.demo.DeptVo;
import com.example.demo.DoctorVo;
import com.example.demo.jpa.DeptResp;
import com.example.demo.jpa.DoctorResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author: qq895
 * @date: 2019/8/26 16:29
 * @description:
 */
@Controller
@RequestMapping("/data")
public class DataController {
    @Autowired
    DeptResp resp;

    @Autowired
    DoctorResp doctorResp;

    @RequestMapping("/dept")
    public void dept() {
        String result1 = HttpUtil.get("http://www.gzfezx.com/yuanwugongkai/jyfw/ksjs/");
        List<String> titles = ReUtil.findAll("<li><a href=(.*?)\" target=\"_blank\">(.*?)</a></li>", result1, 1);
        for (String title : titles) {
            String urlContext = HttpUtil.get(title.substring(1));
            List<String> t = ReUtil.findAll("<div class=\"Specialli Specialtitle\">(.*?)</div>", urlContext, 1);
            List<String> contexts = ReUtil.findAll("<div class=\"Specialli Specialdian\"></div>(.*?)<div class=\"clear\"></div>", urlContext, 1);
            DeptVo dept = new DeptVo();
            dept.setName(t.get(0));
//            StringBuilder sb = new StringBuilder();
//            for (String s1 : contexts) {
//                String sss = s1.replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "");
//                sb.append(sss);
//                sb.append("\r\n");
//            }
            dept.setSummary(contexts.get(0));
            dept.setHisId(82L);
            resp.save(dept);
        }
    }

    @RequestMapping("/doctor")
    public void doctor() {
        String result1 = HttpUtil.get("http://www.gzfezx.com/zkmy/yscx/");
        String regx = "<tr>(.*?)</tr>";
        String regx2 = "<th>(.*?)</th>";
        String regx3 = "<a href=\"(.*?)\" target=\"_blank\">(.*?)</a>";
        String regx4 = "<p style=\"margin: 0px; padding: 0px;\">(.*?)</p>";
        String regx5 = "<p style=\"text-indent:2em;text-align:left;\">(.*?)</p>";
        List<String> t = ReUtil.findAll(regx, result1, 1);
        for (String s : t) {
            List<String> deptName = ReUtil.findAll(regx2, s, 1);
            if (deptName.size() > 0) {
                DeptVo deptVo = resp.findByName(deptName.get(0).trim());
                List<String> doctorUrl = ReUtil.findAll(regx3, s, 1);
                List<String> doctorName = ReUtil.findAll(regx3, s, 2);
                for (int i = 0; i < doctorUrl.size(); i++) {
                    String con = HttpUtil.get(doctorUrl.get(i));
                    List<String> summary = ReUtil.findAll(regx4, con, 1);
                    System.out.println(summary);
                    if (summary.size() == 0) {
                        summary = ReUtil.findAll(regx5, con, 1);
                        System.out.println(summary);
                    }
                    StringBuilder sb = new StringBuilder();
                    for (String s1 : summary) {
                        String sss = s1.replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "");
                        sb.append(sss);
                        sb.append("\r\n");
                    }

                    DoctorVo doc = new DoctorVo();
                    if (null != deptVo) {
                        doc.setDeptNo(deptVo.getId());
                        doc.setName(doctorName.get(i));
                        doc.setSummary(sb.toString());
                        doc.setHisId(82L);
                        doctorResp.save(doc);
                    }
                }
            }
        }

    }

}
