package com.Surnia.commons.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @ClassName JWTUtils
 * @Description TODO
 * @Author Surnia
 * @Date 2021/7/26
 * @Version 1.0
 */
public class JWTUtils {
    // 设置token过期时间
    public static final long EXPIRE = 1000 * 60 * 60 * 24;
    // 字符编码密钥
    public static final String APP_SECRET = "ukc8BDfWus2jZWLPHO";


    /**
     * @description: 生成token字符串的方法
     * @param id：用户id
     * @param nickname：用户昵称
     * @param avatar：	用户头像
     * @return String： 包含用户id、昵称和头像的token字符串
     * @author: Surnia
     * @date: 2021/7/26
     */
    public static String getJwtToken(String id, String nickname, String avatar){

        String JwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")

                .setSubject("user")
                .setIssuedAt(new Date()) // 开始时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE)) //过期时间

                .claim("id", id)
                .claim("avatar", avatar)
                .claim("nickname", nickname)

                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();

        return JwtToken;
    }

    /**
     * 判断token是否存在与有效
     * @param jwtToken
     * @return
     */
    public static boolean checkToken(String jwtToken) {
        if(StringUtils.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断token是否存在与有效
     * @param request
     * @return
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("token");
            if(StringUtils.isEmpty(jwtToken)) return false;
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据token获取会员id
     * @param request
     * @return
     */
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        if(StringUtils.isEmpty(jwtToken)) return "";
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("id");
    }
}
