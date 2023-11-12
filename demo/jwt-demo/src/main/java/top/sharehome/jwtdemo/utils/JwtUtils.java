package top.sharehome.jwtdemo.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import top.sharehome.jwtdemo.entity.vo.LoginVo;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Jwt工具类
 *
 * @author AntonyCheng
 * @since 2023/7/3 09:45:17
 */
@Component
public class JwtUtils {

    private final static String JWT_KEY = "#UX3bwWwnU^sPC4T";

    private final static String AES_KEY = "^yrknDV&%@*T56V&";

    private final static AES AES = SecureUtil.aes(AES_KEY.getBytes());

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 生成token
     *
     * @return token 生成的token
     */
    public String createLoginToken(LoginVo obj) {
        HashMap<String, Object> bodyMap = new HashMap<String, Object>() {
            private final long serialVersionUID = 1L;

            {
                put("loginBody", obj);
            }
        };
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, JWT_KEY)
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("sk", aesEnTimestamp(System.currentTimeMillis()))
                .setClaims(bodyMap);
        String token = jwtBuilder.compact();
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String redisLoginToken = valueOperations.get(String.valueOf("login_" + obj.getId()));
        if (!Objects.isNull(redisLoginToken)) {
            return "禁止多用户同时登录同一账号！";
        }
        valueOperations.set("login_" + obj.getId(), token, Duration.ofHours(1));
        System.out.println("TOKEN签发成功！");
        return token;
    }

    /**
     * 校验TOKEN
     *
     * @param token 需要被校验的TOKEN
     * @return token校验结果
     */
    public boolean checkLoginToken(String token) {
        try {
            Jws<Claims> claimsJws = getJwsInLoginToken(token);
            Claims body = claimsJws.getBody();
            String loginId = String.valueOf(((LinkedHashMap) (body.get("loginBody"))).get("id"));

            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String redisLoginToken = valueOperations.get("token_" + loginId);
            Long redisTokenTime = aesDecTimestamp((String) getJwsInLoginToken(redisLoginToken).getHeader().get("sk"));
            Long userTokenTime = aesDecTimestamp((String) claimsJws.getHeader().get("sk"));
            if (Objects.isNull(redisLoginToken) || redisTokenTime > userTokenTime) {
                System.out.println("登陆身份已过期，请重新登录！");
                return false;
            }
            valueOperations.set("token_" + loginId, token, Duration.ofHours(1));
            System.out.println("验证成功，刷新登陆状态！");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("登录状态错误，请重新登录！");
            return false;
        }
    }

    /**
     * 校验TOKEN然后获取TOKEN中的Header
     *
     * @param token 需要被处理的token
     * @return JwsHeader
     */
    public JwsHeader checkLoginTokenAndGetHeader(String token) {
        try {
            Jws<Claims> claimsJws = getJwsInLoginToken(token);
            Claims body = claimsJws.getBody();
            String loginId = String.valueOf(((LinkedHashMap) (body.get("loginBody"))).get("id"));

            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String redisLoginToken = valueOperations.get("token_" + loginId);
            Long redisTokenTime = aesDecTimestamp((String) getJwsInLoginToken(redisLoginToken).getHeader().get("sk"));
            Long userTokenTime = aesDecTimestamp((String) claimsJws.getHeader().get("sk"));
            if (Objects.isNull(redisLoginToken) || redisTokenTime > userTokenTime) {
                System.out.println("登陆身份已过期，请重新登录！");
                return null;
            }
            valueOperations.set("token_" + loginId, token, Duration.ofHours(1));
            System.out.println("验证成功，刷新登陆状态！");
            return claimsJws.getHeader();
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("登录状态错误，请重新登录！");
            return null;
        }
    }

    /**
     * 校验TOKEN然后获取TOKEN中的Claims
     *
     * @param token 需要被处理的token
     * @return Claims
     */
    public Claims checkLoginTokenAndGetClaims(String token) {
        try {
            Jws<Claims> claimsJws = getJwsInLoginToken(token);
            Claims body = claimsJws.getBody();
            String loginId = String.valueOf(((LinkedHashMap) (body.get("loginBody"))).get("id"));

            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String redisLoginToken = valueOperations.get("token_" + loginId);
            Long redisTokenTime = aesDecTimestamp((String) getJwsInLoginToken(redisLoginToken).getHeader().get("sk"));
            Long userTokenTime = aesDecTimestamp((String) claimsJws.getHeader().get("sk"));
            if (Objects.isNull(redisLoginToken) || redisTokenTime > userTokenTime) {
                System.out.println("登陆身份已过期，请重新登录！");
                return null;
            }
            valueOperations.set("token_" + loginId, token, Duration.ofHours(1));
            System.out.println("验证成功，刷新登陆状态！");
            return body;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("登录状态错误，请重新登录！");
            return null;
        }
    }

    /**
     * 校验TOKEN然后获取TOKEN中的Signature
     *
     * @param token 需要被处理的token
     * @return Signature
     */
    public String checkLoginTokenAndGetSignature(String token) {
        try {
            Jws<Claims> claimsJws = getJwsInLoginToken(token);
            Claims body = claimsJws.getBody();
            String loginId = String.valueOf(((LinkedHashMap) (body.get("loginBody"))).get("id"));

            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String redisLoginToken = valueOperations.get("token_" + loginId);
            Long redisTokenTime = aesDecTimestamp((String) getJwsInLoginToken(redisLoginToken).getHeader().get("sk"));
            Long userTokenTime = aesDecTimestamp((String) claimsJws.getHeader().get("sk"));
            if (Objects.isNull(redisLoginToken) || redisTokenTime > userTokenTime) {
                System.out.println("登陆身份已过期，请重新登录！");
                return null;
            }
            valueOperations.set("token_" + loginId, token, Duration.ofHours(1));
            System.out.println("验证成功，刷新登陆状态！");
            return claimsJws.getSignature();
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("登录状态错误，请重新登录！");
            return null;
        }
    }

    /**
     * 获取TOKEN中的Header
     *
     * @param token 需要被处理的token
     * @return JwsHeader
     */
    public JwsHeader getHeaderInLoginToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(JWT_KEY)
                    .parseClaimsJws(token)
                    .getHeader();
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("登录状态错误，请重新登录！");
            return null;
        }
    }

    /**
     * 获取TOKEN中的Claims
     *
     * @param token 需要被处理的token
     * @return Claims
     */
    public Claims getClaimsInLoginToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(JWT_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("登录状态错误，请重新登录！");
            return null;
        }
    }

    /**
     * 获取TOKEN中的Signature
     *
     * @param token 需要被处理的token
     * @return Signature
     */
    public String getSignatureInLoginToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(JWT_KEY)
                    .parseClaimsJws(token)
                    .getSignature();
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("登录状态错误，请重新登录！");
            return null;
        }
    }

    /**
     * 获取TOKEN中的Jws&lt;Claims&gt;
     *
     * @param token 需要被处理的token
     * @return Jws&lt;Claims&gt;
     */
    public Jws<Claims> getJwsInLoginToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(JWT_KEY)
                    .parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("登录状态错误，请重新登录！");
            return null;
        }
    }

    /**
     * 对称加密AES加密时间戳
     *
     * @param plaintext 明文
     * @return 密文
     */
    public String aesEnTimestamp(Long plaintext) {
        if (ObjectUtils.isEmpty(plaintext)) {
            System.out.println("时间异常!");
            return null;
        }
        return AES.encryptHex(String.valueOf(plaintext), CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 对称加密AES解密时间戳
     *
     * @param ciphertext 密文
     * @return 明文
     */
    public Long aesDecTimestamp(String ciphertext) {
        try {
            if (ObjectUtils.isEmpty(ciphertext)) {
                System.out.println("时间异常!");
                return null;
            }
            return Long.parseLong(AES.decryptStr(ciphertext, CharsetUtil.CHARSET_UTF_8));
        } catch (NumberFormatException e) {
            System.out.println("时间异常！");
            return null;
        }
    }


}
