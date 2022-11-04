package com.su.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.su.dao.LoginUser;
import com.su.dao.User;
import com.su.dao.mapper.UserMapper;
import com.sun.xml.internal.ws.server.ServerRtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, username);
        final User user = mapper.selectOne(wrapper);
        if(Objects.isNull(user) ){
            throw new RuntimeException("用户不存在");
        }
        List<String> permissions = new ArrayList<>(Arrays.asList("test"));
        return new LoginUser(user,permissions);
    }
}
