package com.su.filter;

import ch.qos.logback.classic.Logger;
import com.su.dao.LoginUser;
import com.su.util.JwtUtil;
import com.su.util.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    final Logger LOGGER = (Logger) LoggerFactory.getLogger("com.su.JwtAuthenticationTokenFilter");

    @Autowired
    RedisCache cache;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取Token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 解析Token
        Claims claims = JwtUtil.parseJWT(token);
        String id = claims.getSubject();

        //获取redist key
        final String redisKey = "login:" + id;
        LOGGER.info("redis key from token:{}", redisKey);
        LoginUser loginUser = cache.getCacheObject(redisKey);
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("用户未登录");
        }

        LOGGER.info("concurrent user:{}",loginUser);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);

    }
}
