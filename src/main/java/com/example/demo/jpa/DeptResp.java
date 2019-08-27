package com.example.demo.jpa;

import com.example.demo.DeptVo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: qq895
 * @date: 2019/8/26 15:49
 * @description:
 */
public interface DeptResp extends JpaRepository<DeptVo, Long> {
    /**
     * 
     * @param name
     * @return
     */
    DeptVo findByName(String name);
    
}
