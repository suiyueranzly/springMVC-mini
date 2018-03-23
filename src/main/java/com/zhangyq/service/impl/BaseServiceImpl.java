package com.zhangyq.service.impl;

import com.zhangyq.annotion.Service;
import com.zhangyq.service.BaseService;

import java.util.Map;

/**
 * Created by zhangyq on 2017/8/25.
 */
@Service("baseService")
public class BaseServiceImpl implements BaseService {
    /**
     * 增加
     *
     * @param map
     */
    @Override
    public int add(Map map) {
        System.out.println("this is add method");
        return 0;
    }

    /**
     * 删除
     *
     * @param map
     */
    @Override
    public int delete(Map map) {
        System.out.println("this is delete method");
        return 0;
    }

    /**
     * 修改
     *
     * @param map
     */
    @Override
    public int update(Map map) {
        System.out.println("this is update method");
        return 0;
    }

    /**
     * 查询
     *
     * @param map
     */
    @Override
    public int select(Map map) {
        System.out.println("this is select method");
        return 0;
    }
}
