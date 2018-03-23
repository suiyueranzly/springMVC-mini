package com.zhangyq.service;

import java.util.Map;

/**
 * Created by zhangyq on 2017/8/25.
 */
public interface BaseService {
    /**
     * 增加
     * */
    int add(Map map);
    /**
     * 删除
     * */
    int delete(Map map);
    /**
     * 修改
     * */
    int update(Map map);
    /**
     * 查询
     * */
    int select(Map map);
}
