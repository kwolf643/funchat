package com.shixun.funchat.controller;

import com.shixun.funchat.dao.UserMapper;
import com.shixun.funchat.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
public class FileController {
    private static Logger log= LoggerFactory.getLogger(FileController.class);

    @Autowired
    private UserMapper userMapper;

    //文件上传
    @PostMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("fileName") MultipartFile file, HttpServletRequest request
            , HttpServletResponse response){
        HttpSession session=request.getSession();
        User user = (User) session.getAttribute("USER_SESSION");//从session中直接获得
        String realpath = "D:\\IDEA-workspace\\funchat\\fileupload\\images";
        //判断
        if(file.isEmpty()){
            return "false";
        }
        String originalFilename = file.getOriginalFilename();//得到原名
        int size = (int) file.getSize()/1024;//文件大小
        String newFilename = user.getUsername()+ "_" +originalFilename;
        log.debug(newFilename + " 大小:" + size+"KB");

        File file1 = new File("D:\\IDEA-workspace\\funchat\\fileupload\\"+user.getIcon());
        File dest = new File(realpath +"/"+ newFilename);//存储路径
        log.debug(realpath +"/"+ newFilename);
        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);//上传文件
            if (file1.exists()) {  //删除以前的头像
                if (file1.delete()) {
                    log.debug("删除成功");
                } else log.debug("删除失败！");
            } else log.debug("文件不存在！");

            user.setIcon("/images/"+newFilename);//把头像路径传递到数据库
            userMapper.updateByPrimaryKeySelective(user);
            response.sendRedirect("/userinfo");
            return "true";
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        }
    }
}
