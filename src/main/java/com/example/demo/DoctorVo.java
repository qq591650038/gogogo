package com.example.demo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: qq895
 * @date: 2019/8/26 16:35
 * @description:
 */
@Entity
@Table(name = "t_hc_doctor")
public class DoctorVo implements Serializable {
    @Id
    private Long id;
    
    private String name;
    
    private Long deptNo;
    
    private String summary;
    
    private Long hisId;
    
    private String title;
    
    private String img;

    private Long platformId;
    
    private String pid;
    
    private String hasChild;
    
    private String skill;

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public Long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Long platformId) {
        this.platformId = platformId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getHasChild() {
        return hasChild;
    }

    public void setHasChild(String hasChild) {
        this.hasChild = hasChild;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(Long deptNo) {
        this.deptNo = deptNo;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getHisId() {
        return hisId;
    }

    public void setHisId(Long hisId) {
        this.hisId = hisId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "DoctorVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deptNo=" + deptNo +
                ", summary='" + summary + '\'' +
                ", hisId=" + hisId +
                ", title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", platformId=" + platformId +
                ", pid='" + pid + '\'' +
                ", hasChild='" + hasChild + '\'' +
                ", skill='" + skill + '\'' +
                '}';
    }
}
