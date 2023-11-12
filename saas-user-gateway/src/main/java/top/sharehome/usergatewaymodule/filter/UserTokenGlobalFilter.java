package top.sharehome.usergatewaymodule.filter;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.sharehome.usergatewaymodule.common.constant.CommonConstant;
import top.sharehome.usergatewaymodule.common.exception_handler.customize.CustomizeReturnException;
import top.sharehome.usergatewaymodule.common.response.R;
import top.sharehome.usergatewaymodule.common.response.RCodeEnum;
import top.sharehome.usergatewaymodule.model.vo.token.TokenLoginVo;
import top.sharehome.usergatewaymodule.utils.JwtUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 全局Token认证过滤器
 *
 * @author AntonyCheng
 * @since 2023/7/10 15:37:47
 */
@Component
public class UserTokenGlobalFilter implements GlobalFilter, Ordered {

    @Resource
    private JwtUtils jwtUtils;

    public static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String requestUri = request.getURI().getPath();

        try {
            String nginxHeader = request.getHeaders().getFirst(CommonConstant.NGINX_USER_HEADER);
            if (Objects.isNull(nginxHeader) || ObjectUtils.notEqual(nginxHeader, CommonConstant.NGINX_USER_HEADER_VALUE)) {
                ServerHttpResponse response = exchange.getResponse();
                byte[] returnBits = JSON.toJSONString(R.failure(RCodeEnum.THE_SOURCE_OF_NGINX_TRAFFIC_IS_UNKNOWN)).getBytes(StandardCharsets.UTF_8);
                DataBuffer wrap = response.bufferFactory().wrap(returnBits);
                return response.writeWith(Mono.just(wrap));
            }
            List<String> excludeUrisNotNeedTokenAndNotNeedAuthenticate = Arrays.asList();
            for (String s : excludeUrisNotNeedTokenAndNotNeedAuthenticate) {
                if (ANT_PATH_MATCHER.match(s, requestUri)) {
                    return chain.filter(exchange);
                }
            }

            String token = request.getHeaders().getFirst(CommonConstant.TOKEN_HEADER);
            if (StringUtils.isEmpty(token)) {
                ServerHttpResponse response = exchange.getResponse();
                byte[] returnBits = JSON.toJSONString(R.failure(RCodeEnum.USER_LOGIN_TOKEN_IS_LOST)).getBytes(StandardCharsets.UTF_8);
                DataBuffer wrap = response.bufferFactory().wrap(returnBits);
                return response.writeWith(Mono.just(wrap));
            }

            List<String> excludeUrisNeedTokenAndNotNeedAuthenticate = Arrays.asList();
            for (String s : excludeUrisNeedTokenAndNotNeedAuthenticate) {
                if (ANT_PATH_MATCHER.match(s, requestUri)) {
                    return chain.filter(exchange);
                }
            }

            Claims claims = jwtUtils.getClaimsInLoginToken(token);
            String jsonBody = (String) claims.get("tokenBody");
            JSONObject jsonObject = jwtUtils.aesDecObject(jsonBody);
            TokenLoginVo loginToken = jsonObject.toBean(TokenLoginVo.class);
            if (!Objects.equals(loginToken.getIdentity(), CommonConstant.LOGIN_IDENTITY_MANAGER)
                    && !Objects.equals(loginToken.getIdentity(), CommonConstant.LOGIN_IDENTITY_TEACHER)
                    && !Objects.equals(loginToken.getIdentity(), CommonConstant.LOGIN_IDENTITY_STUDENT)) {
                ServerHttpResponse response = exchange.getResponse();
                byte[] returnBits = JSON.toJSONString(R.failure(RCodeEnum.ACCESS_UNAUTHORIZED)).getBytes(StandardCharsets.UTF_8);
                DataBuffer wrap = response.bufferFactory().wrap(returnBits);
                return response.writeWith(Mono.just(wrap));
            }
        } catch (CustomizeReturnException e) {
            ServerHttpResponse response = exchange.getResponse();
            byte[] returnBits = JSON.toJSONString(R.failure(RCodeEnum.ACCESS_UNAUTHORIZED)).getBytes(StandardCharsets.UTF_8);
            DataBuffer wrap = response.bufferFactory().wrap(returnBits);
            return response.writeWith(Mono.just(wrap));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
