package com.example.demo;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.jpa.DeptResp;
import com.example.demo.jpa.DoctorResp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Document;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    DeptResp resp;

    @Autowired
    DoctorResp doctorResp;

    @Test
    public void contextLoads() {
        String result1 = HttpUtil.get("http://www.gzfezx.com/zkmy/yscx/");
        String regx = "<tr>(.*?)</tr>";
        String regx2 = "<th>(.*?)</th>";
        String regx3 = "<a href=\"(.*?)\" target=\"_blank\">(.*?)</a>";
        String regx4 = "<p style=\"margin: 0px; padding: 0px;\">(.*?)</p>";
        String regx5 = "<p style=\"text-indent:2em;text-align:left;\">(.*?)</p>";
        String regx6 = "<td class=\"Specialeft\" style=\"margin: 0px; padding: 0px; width: 193px; line-height: 25px; text-align: left;\">(.*?)</td>";
        String regx7 = "<img src=\"(.*?)\" (.*?)\" />";
        List<String> t = ReUtil.findAll(regx, result1, 1);
        for (String s : t) {
            List<String> deptName = ReUtil.findAll(regx2, s, 1);
            if (deptName.size() > 0) {
                //System.out.println(resp.findByName(deptName.get(0).trim()));
                DeptVo deptVo = resp.findByName(deptName.get(0).trim());
                List<String> doctorUrl = ReUtil.findAll(regx3, s, 1);
                List<String> doctorName = ReUtil.findAll(regx3, s, 2);
                for (int i = 0; i < doctorUrl.size(); i++) {
                    String con = HttpUtil.get(doctorUrl.get(i));
                    List<String> summary = ReUtil.findAll(regx4, con, 1);
                    List<String> title = ReUtil.findAll(regx6, con, 1);
                    List<String> img = ReUtil.findAll(regx7, con, 1);
                    System.out.println(img);
                    if (summary.size() == 0) {
                        summary = ReUtil.findAll(regx5, con, 1);
                    }
                    StringBuilder sb = new StringBuilder();
                    for (String s1 : summary) {
                        String sss = s1.replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "\t\n");
                        sb.append(sss);
                    }

                    DoctorVo doc = new DoctorVo();
                    if (null != deptVo) {
                        doc.setDeptNo(deptVo.getNo());
                        doc.setName(doctorName.get(i));
                        doc.setSummary(sb.toString());
                        if (title.size() > 1) {
                            String sss = title.get(1).replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "");
                            doc.setTitle(sss);
                        }
                        if (img.size() > 1) {
                            doc.setImg(img.get(1));
                        }
                        doc.setHisId(82L);
                        doctorResp.save(doc);
                    }
                }

//                System.out.println(doctorUrl);
//                System.out.println(doctorName);
            }
        }

    }

}
