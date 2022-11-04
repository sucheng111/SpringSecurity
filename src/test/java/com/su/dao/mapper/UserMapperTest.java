package com.su.dao.mapper;

import com.su.dao.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    UserMapper mapper;

    @Test
    public void testSelect(){
        final List<User> users = mapper.selectList(null);
        users.forEach(System.out::println);
    }


    @Test
    public void testEncoder(){
        final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        final String encode1 = bCryptPasswordEncoder.encode("123");
        System.out.println(encode1);
        Assert.assertTrue(bCryptPasswordEncoder.matches("123",encode1));

    }

}