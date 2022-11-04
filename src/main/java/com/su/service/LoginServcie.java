package com.su.service;

import com.su.common.ResponseResult;
import com.su.dao.User;

public interface LoginServcie {
    public ResponseResult login(User user);

    public ResponseResult logout();
}
