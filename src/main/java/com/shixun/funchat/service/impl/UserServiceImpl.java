package com.shixun.funchat.service.impl;

import com.shixun.funchat.dao.UserMapper;
import com.shixun.funchat.entity.User;
import com.shixun.funchat.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    /**
     * 登录验证
     *
     * @param userName 用户名
     * @param password 密码
     * @return null 验证失败 其他 验证成功的用户信息
     */
    @Override
    public User login(String userName, String password) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password))
            return null;
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        List<User> users = userMapper.selectBySelective(user);
        if (users.isEmpty())
            return null;
        return users.get(0);
    }


    /**
     * 注册用户
     *
     * @param user 要注册的用户信息
     * @return null 注册失败 其他 注册成功的用户信息
     */
    @Override
    @Transactional
    public User register(User user) {
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()))
            return null;
        //查找同名用户
        User user_find = new User();
        user_find.setUsername(user.getUsername());
        List<User> users = userMapper.selectBySelective(user_find);
        if (!users.isEmpty()) //用户名已存在
            return null;
        //插入用户
        user.setId(null);
        userMapper.insertSelective(user);
        return user;
    }


    /**
     * 更新用户信息
     *
     * @param user 要更新的用户信息
     * @return null 更新失败 其他 更新后的用户信息
     */
    @Override
    @Transactional
    public User updateUserInfo(User user) {
        if (user.getId() == null) //ID 为空
            return null;
        if (StringUtils.isNotBlank(user.getUsername())) { //要修改用户名
            //查找同名用户
            User user_find = new User();
            user_find.setUsername(user.getUsername());
            List<User> users = userMapper.selectBySelective(user_find);
            if (!users.isEmpty()) //用户名已存在
                return null;
        }
        if (user.getPassword() != null && StringUtils.isBlank(user.getPassword())) //要修改密码
            return null;
        //更新
        userMapper.updateByPrimaryKeySelective(user);
        return user;
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return null 没有找到用户 其他 用户信息
     */
    @Override
    public User getUserInfo(Integer userId) {
        if (userId == null)
            return null;
        return userMapper.selectByPrimaryKey(userId);
    }

}
