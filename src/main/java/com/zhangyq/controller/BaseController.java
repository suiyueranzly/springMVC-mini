package com.zhangyq.controller;

import com.zhangyq.annotion.Controller;
import com.zhangyq.annotion.Qualifier;
import com.zhangyq.annotion.RequestMapping;
import com.zhangyq.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyq on 2017/8/25.
 */
@Controller("base")
public class BaseController {
    @Qualifier("baseService")
    private BaseService baseService;

    /**
     * 插入
     */
    @RequestMapping("insert")
    public String insert(HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        baseService.add(map);
        return "";
    }

    /**
     * 删除
     */
    @RequestMapping("delete")
    public String delete(HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        baseService.delete(map);
        return "";
    }

    /**
     * 修改
     */
    @RequestMapping("update")
    public String update(HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        baseService.update(map);
        return "";
    }

    /**
     * 查询
     */
    @RequestMapping("select")
    public String select(HttpServletRequest request, HttpServletResponse response) {
        Map map = new HashMap();
        baseService.select(map);
        return "";
    }
}
