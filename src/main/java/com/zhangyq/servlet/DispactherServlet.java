package com.zhangyq.servlet;


import com.zhangyq.annotion.Controller;
import com.zhangyq.annotion.Qualifier;
import com.zhangyq.annotion.RequestMapping;
import com.zhangyq.annotion.Service;
import com.zhangyq.controller.BaseController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyq on 2017/8/25.
 */

@WebServlet("/DispactherServlet")
public class DispactherServlet extends HttpServlet {
    //将扫描到的所有文件
    private List<String> packageNames = new ArrayList<>();
    //注解属性对应各层对象实例的map
    private Map<String, Object> instanceMaps = new HashMap<String, Object>();
    //存储url和对应method的map
    private Map<String, Method> methodMaps = new HashMap<String, Method>();

    @Override
    public void init() throws ServletException {
        /**
         * 扫描我们基础包上的注解:com.zhangyq
         * 扫描基础包之后拿到我们的全限定名
         * src\com\zhangyq\annotion\Controller.java
         * 替换\  变成.
         * com.zhangyq.servlet.DispactherServlet
         * 拿到实例
         * 将实例注入各层bean的变量中
         *
         * */
        //扫描全包
        scanBase("com.zhangyq");
        //找到类实例
        try {
            filterAndInstance();
            //把所有类注入进来 springioc
            springIoc();
            //匹配requestMapping
            handlerMaps();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过url和找到相应的method对象进行处理
     */
    private void handlerMaps() throws Exception {
        if (instanceMaps.size() <= 0) {
            return;
        }
        //instanceMaps存储的是service和controller层  判断是否是controller
        for (Map.Entry<String, Object> entry : instanceMaps.entrySet()) {
            //拿到字段  判断是否有注入的注解
            if (entry.getValue().getClass().isAnnotationPresent(Controller.class)) {
                Controller controller = entry.getValue().getClass().getAnnotation(Controller.class);
                String baseUrl = controller.value();
                Method[] methods = entry.getValue().getClass().getMethods();
                for (Method method : methods) {
                    //判断是否有RequestMapping注解
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        String methodUrl = ((RequestMapping) method.getAnnotation(RequestMapping.class)).value();
                        methodMaps.put("/" + baseUrl + "/" + methodUrl, method);
                    } else {
                        continue;
                    }

                }
            }
        }
    }

    /**
     * 将实例注入到spring容器中
     */
    private void springIoc() throws IllegalAccessException {
        if (instanceMaps.size() <= 0) {
            return;
        }
        for (Map.Entry<String, Object> entry : instanceMaps.entrySet()) {
            //拿到字段  判断是否有注入的注解
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Qualifier.class)) {
                    //如果有  则注入
                    //取到注解的名字
                    String qualifierValueString = ((Qualifier) field.getAnnotation(Qualifier.class)).value();
                    field.setAccessible(true);
                    field.set(entry.getValue(), instanceMaps.get(qualifierValueString));

                } else {
                    continue;
                }
            }
        }

    }


    //拦截方法请求  根据请求找到对应的handler
    private void filterAndInstance() throws Exception {
        //判断集合中是否有实例
        if (packageNames.size() <= 0) {
            return;
        }
        //如果有
        for (String className : packageNames) {
            //拿到字节码对象
            Class ccName = Class.forName(className.replaceAll(".class", ""));
            //如果有controller注解
            if (ccName.isAnnotationPresent(Controller.class)) {
                Object instance = ccName.newInstance();
                //将实例装入map
                Controller controller = (Controller) ccName.getAnnotation(Controller.class);
                //拿到注解对象的属性值  相当于xml的beanID  value就是实例
                String key = controller.value();
                //放入map中   可以根据key找到类的实例
                instanceMaps.put(key, instance);
            } else if (ccName.isAnnotationPresent(Service.class)) {
                Object instance = ccName.newInstance();
                //将实例装入map
                Service service = (Service) ccName.getAnnotation(Service.class);
                //拿到注解对象的属性值  相当于xml的beanID  value就是实例
                String key = service.value();
                //放入map中   可以根据key找到类的实例
                instanceMaps.put(key, instance);
            } else {
                //如果没有注解
                continue;
            }
        }
    }

    /**
     * 扫描全包
     */
    private void scanBase(String basePackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + replacePath(basePackage));
        //拿到该路径下文件夹以及文件
        String pathFile = url.getFile();
        //将路径封装成File
        File file = new File(pathFile);
        String[] files = file.list();
        for (String path : files) {
            //再次构造成一个file类
            File itemsFile = new File(pathFile + path);
            if (itemsFile.isDirectory()) {
                //递归
                scanBase(basePackage + "." + itemsFile.getName());
            } else if (itemsFile.isFile()) {
                //如果是文件
                packageNames.add(basePackage + "." + itemsFile.getName());
            }
        }
    }

    /**
     * 将带.的路径替换成/
     */
    private String replacePath(String path) {
        return path.replaceAll("\\.", "/");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //methodMaps
        //拿到完整路径
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        String pathUrl = requestURI.replaceAll(contextPath, "");
        //拿到方法对象
        Method method = methodMaps.get(pathUrl);
        PrintWriter printWriter = resp.getWriter();
        if (method == null) {
            printWriter.write("找不到URL！");
            return;
        }
        //
        String[] split = requestURI.split("/");
        int index = 2;
        if("".equals(split[0])||null==split[0]){
            index = 1;
        }
        String className = split[index];
        BaseController baseController = (BaseController) instanceMaps.get(className);
        try {
            method.invoke(baseController, new Object[]{req, resp});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
