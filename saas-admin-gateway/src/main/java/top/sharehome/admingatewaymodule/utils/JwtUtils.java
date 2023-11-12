package top.sharehome.admingatewaymodule.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import top.sharehome.admingatewaymodule.common.constant.CommonConstant;
import top.sharehome.admingatewaymodule.common.response.RCodeEnum;
import top.sharehome.admingatewaymodule.model.vo.token.TokenLoginVo;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;

/**
 * Jwt工具类
 *
 * @author AntonyCheng
 * @since 2023/7/3 09:45:17
 */
@Component
public class JwtUtils {
    public final static AES AES = SecureUtil.aes(CommonConstant.AES_KEY.getBytes());

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 生成token
     *
     * @return token 生成的token
     */
    public String createLoginToken(TokenLoginVo obj) {
        if (BeanMetaDataUtils.isAnyMetadataEmpty(obj)) {
            return "签发TOKEN必要参数为空";
        }
        String logAccount = obj.getAccount();
        String jsonBody = aesEnObject(obj);
        HashMap<String, Object> bodyMap = new HashMap<String, Object>() {
            private final long serialVersionUID = 1L;

            {
                put("tokenBody", jsonBody);
            }
        };
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, CommonConstant.JWT_KEY)
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("sk", aesEnLong(System.currentTimeMillis()))
                .setHeaderParam("uid", aesEnLong(obj.getId()))
                .setHeaderParam("tid", aesEnLong(obj.getTenant()))
                .setClaims(bodyMap);
        String token = jwtBuilder.compact();
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String redisLoginToken = valueOperations.get("token_" + obj.getId());
        if (!Objects.isNull(redisLoginToken)) {
            ThrowUtils.error(RCodeEnum.CANNOT_BE_MULTIPLAYER_ONLINE,
                    String.format("“%s”账号已经在线，不能二次登陆", logAccount));
        }
        valueOperations.set("token_" + obj.getId(), token, Duration.ofHours(1));
        return token;
    }

    /**
     * 校验TOKEN
     *
     * @param token 需要被校验的TOKEN
     */
    public void checkLoginToken(String token) {
        try {
            Jws<Claims> claimsJws = getJwsInLoginToken(token);
            JwsHeader header = claimsJws.getHeader();
            Long tokenId = aesDecLong(String.valueOf(header.get("uid")));

            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String redisLoginToken = valueOperations.get("token_" + tokenId);
            if (Objects.isNull(redisLoginToken)) {
                ThrowUtils.error(RCodeEnum.USER_LOGIN_HAS_EXPIRED);
            }
            Long redisTokenTime = aesDecLong((String) getJwsInLoginToken(redisLoginToken).getHeader().get("sk"));
            Long userTokenTime = aesDecLong((String) header.get("sk"));
            if (redisTokenTime > userTokenTime) {
                ThrowUtils.error(RCodeEnum.USER_LOGIN_HAS_EXPIRED);
            }
            valueOperations.set("token_" + tokenId, token, Duration.ofHours(1));
        } catch (JwtException | IllegalArgumentException e) {
            ThrowUtils.error(RCodeEnum.USER_LOGIN_TOKEN_IS_INVALID);
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
            JwsHeader header = claimsJws.getHeader();
            Long tokenId = aesDecLong(String.valueOf(header.get("uid")));

            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String redisLoginToken = valueOperations.get("token_" + tokenId);
            if (Objects.isNull(redisLoginToken)) {
                ThrowUtils.error(RCodeEnum.USER_LOGIN_HAS_EXPIRED);
            }
            Long redisTokenTime = aesDecLong((String) getJwsInLoginToken(redisLoginToken).getHeader().get("sk"));
            Long userTokenTime = aesDecLong((String) header.get("sk"));
            if (redisTokenTime > userTokenTime) {
                ThrowUtils.error(RCodeEnum.USER_LOGIN_HAS_EXPIRED);
            }
            valueOperations.set("token_" + tokenId, token, Duration.ofHours(1));
            return header;
        } catch (JwtException | IllegalArgumentException e) {
            ThrowUtils.error(RCodeEnum.USER_LOGIN_TOKEN_IS_INVALID);
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
            JwsHeader header = claimsJws.getHeader();
            Long tokenId = aesDecLong(String.valueOf(header.get("uid")));

            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String redisLoginToken = valueOperations.get("token_" + tokenId);
            if (Objects.isNull(redisLoginToken)) {
                ThrowUtils.error(RCodeEnum.USER_LOGIN_HAS_EXPIRED);
            }
            Long redisTokenTime = aesDecLong((String) getJwsInLoginToken(redisLoginToken).getHeader().get("sk"));
            Long userTokenTime = aesDecLong((String) header.get("sk"));
            if (redisTokenTime > userTokenTime) {
                ThrowUtils.error(RCodeEnum.USER_LOGIN_HAS_EXPIRED);
            }
            valueOperations.set("token_" + tokenId, token, Duration.ofHours(1));
            return claimsJws.getBody();
        } catch (JwtException | IllegalArgumentException e) {
            ThrowUtils.error(RCodeEnum.USER_LOGIN_TOKEN_IS_INVALID);
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
            JwsHeader header = claimsJws.getHeader();
            Long tokenId = aesDecLong(String.valueOf(header.get("uid")));
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String redisLoginToken = valueOperations.get("token_" + tokenId);
            if (Objects.isNull(redisLoginToken)) {
                ThrowUtils.error(RCodeEnum.USER_LOGIN_HAS_EXPIRED);
            }
            Long redisTokenTime = aesDecLong((String) getJwsInLoginToken(redisLoginToken).getHeader().get("sk"));
            Long userTokenTime = aesDecLong((String) header.get("sk"));
            if (redisTokenTime > userTokenTime) {
                ThrowUtils.error(RCodeEnum.USER_LOGIN_HAS_EXPIRED);
            }
            valueOperations.set("token_" + tokenId, token, Duration.ofHours(1));
            return claimsJws.getSignature();
        } catch (JwtException | IllegalArgumentException e) {
            ThrowUtils.error(RCodeEnum.USER_LOGIN_TOKEN_IS_INVALID);
            return null;
        }
    }

    /**
     * 校验TOKEN然后获取TOKEN中的Jws&lt;Claims&gt;
     *
     * @param token 需要被处理的token
     * @return Jws&lt;Claims&gt;
     */
    public Jws<Claims> checkLoginTokenAndGetJws(String token) {
        try {
            Jws<Claims> claimsJws = getJwsInLoginToken(token);
            JwsHeader header = claimsJws.getHeader();
            Long tokenId = aesDecLong(String.valueOf(header.get("uid")));
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String redisLoginToken = valueOperations.get("token_" + tokenId);
            if (Objects.isNull(redisLoginToken)) {
                ThrowUtils.error(RCodeEnum.USER_LOGIN_HAS_EXPIRED);
            }
            Long redisTokenTime = aesDecLong((String) getJwsInLoginToken(redisLoginToken).getHeader().get("sk"));
            Long userTokenTime = aesDecLong((String) header.get("sk"));
            if (redisTokenTime > userTokenTime) {
                ThrowUtils.error(RCodeEnum.USER_LOGIN_HAS_EXPIRED);
            }
            valueOperations.set("token_" + tokenId, token, Duration.ofHours(1));
            return claimsJws;
        } catch (JwtException | IllegalArgumentException e) {
            ThrowUtils.error(RCodeEnum.USER_LOGIN_TOKEN_IS_INVALID);
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
                    .setSigningKey(CommonConstant.JWT_KEY)
                    .parseClaimsJws(token)
                    .getHeader();
        } catch (JwtException | IllegalArgumentException e) {
            ThrowUtils.error(RCodeEnum.USER_LOGIN_TOKEN_IS_INVALID);
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
                    .setSigningKey(CommonConstant.JWT_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            ThrowUtils.error(RCodeEnum.USER_LOGIN_TOKEN_IS_INVALID);
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
                    .setSigningKey(CommonConstant.JWT_KEY)
                    .parseClaimsJws(token)
                    .getSignature();
        } catch (JwtException | IllegalArgumentException e) {
            ThrowUtils.error(RCodeEnum.USER_LOGIN_TOKEN_IS_INVALID);
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
                    .setSigningKey(CommonConstant.JWT_KEY)
                    .parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            ThrowUtils.error(RCodeEnum.USER_LOGIN_TOKEN_IS_INVALID);
            return null;
        }
    }

    /**
     * 对称加密AES加密长整
     *
     * @param plaintext 明文
     * @return 密文
     */
    public String aesEnLong(Long plaintext) {
        if (ObjectUtils.isEmpty(plaintext)) {
            ThrowUtils.error(RCodeEnum.ENCRYPTED_CONTENT_IS_ABNORMAL);
        }
        return AES.encryptHex(String.valueOf(plaintext), CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 对称加密AES解密长整
     *
     * @param ciphertext 密文
     * @return 明文
     */
    public Long aesDecLong(String ciphertext) {
        try {
            if (ObjectUtils.isEmpty(ciphertext)) {
                ThrowUtils.error(RCodeEnum.ENCRYPTED_CONTENT_IS_ABNORMAL);
            }
            return Long.parseLong(AES.decryptStr(ciphertext, CharsetUtil.CHARSET_UTF_8));
        } catch (Exception e) {
            ThrowUtils.error(RCodeEnum.ENCRYPTED_CONTENT_IS_ABNORMAL);
            return null;
        }
    }

    /**
     * 对称加密AES加密字符串
     *
     * @param plaintext 明文
     * @return 密文
     */
    public String aesEnString(String plaintext) {
        if (ObjectUtils.isEmpty(plaintext)) {
            ThrowUtils.error(RCodeEnum.ENCRYPTED_CONTENT_IS_ABNORMAL);
        }
        return AES.encryptHex(plaintext, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 对称加密AES解密字符串
     *
     * @param ciphertext 密文
     * @return 明文
     */
    public String aesDecString(String ciphertext) {
        try {
            if (ObjectUtils.isEmpty(ciphertext)) {
                ThrowUtils.error(RCodeEnum.ENCRYPTED_CONTENT_IS_ABNORMAL);
            }
            return AES.decryptStr(ciphertext, CharsetUtil.CHARSET_UTF_8);
        } catch (Exception e) {
            ThrowUtils.error(RCodeEnum.ENCRYPTED_CONTENT_IS_ABNORMAL);
            return null;
        }
    }

    /**
     * 对称加密AES加密整型
     *
     * @param plaintext 明文
     * @return 密文
     */
    public String aesEnInteger(Integer plaintext) {
        if (ObjectUtils.isEmpty(plaintext)) {
            ThrowUtils.error(RCodeEnum.ENCRYPTED_CONTENT_IS_ABNORMAL);
        }
        return AES.encryptHex(String.valueOf(plaintext), CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 对称加密AES解密整型
     *
     * @param ciphertext 密文
     * @return 明文
     */
    public Integer aesDecInteger(String ciphertext) {
        try {
            if (ObjectUtils.isEmpty(ciphertext)) {
                ThrowUtils.error(RCodeEnum.ENCRYPTED_CONTENT_IS_ABNORMAL);
            }
            return Integer.parseInt(AES.decryptStr(ciphertext, CharsetUtil.CHARSET_UTF_8));
        } catch (Exception e) {
            ThrowUtils.error(RCodeEnum.ENCRYPTED_CONTENT_IS_ABNORMAL);
            return null;
        }
    }

    /**
     * 先将对象转为JSON字符串，然后进行加密
     *
     * @param object 需要加密的对象
     * @return 密文
     */
    public String aesEnObject(Object object) {
        if (ObjectUtils.isEmpty(object)) {
            ThrowUtils.error(RCodeEnum.ENCRYPTED_CONTENT_IS_ABNORMAL);
        }
        return AES.encryptHex(JSONUtil.toJsonStr(object), CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 解密对象JSON
     *
     * @param ciphertext 密文
     * @return 明文
     */
    public JSONObject aesDecObject(String ciphertext) {
        try {
            if (ObjectUtils.isEmpty(ciphertext)) {
                ThrowUtils.error(RCodeEnum.ENCRYPTED_CONTENT_IS_ABNORMAL);
                return null;
            }
            return JSONUtil.parseObj(AES.decryptStr(ciphertext, CharsetUtil.CHARSET_UTF_8));
        } catch (Exception e) {
            ThrowUtils.error(RCodeEnum.ENCRYPTED_CONTENT_IS_ABNORMAL);
            return null;
        }
    }
}