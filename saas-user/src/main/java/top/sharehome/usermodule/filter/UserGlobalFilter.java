package top.sharehome.usermodule.filter;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.annotation.Order;
import top.sharehome.issuemodule.model.vo.token.TokenLoginVo;
import top.sharehome.issuemodule.common.constant.CommonConstant;
import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeReturnException;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.utils.jwt.JwtUtils;
import top.sharehome.issuemodule.utils.meta.BeanMetaDataUtils;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 签发过滤器
 *
 * @author AntonyCheng
 * @since 2023/7/4 17:26:20
 */
@WebFilter(filterName = "UserGlobalFilter", urlPatterns = "/*")
@Slf4j
@Order(0)
public class UserGlobalFilter implements Filter {

    @Resource
    private JwtUtils jwtUtils;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (ObjectUtils.notEqual(request.getHeader(CommonConstant.NGINX_USER_HEADER), CommonConstant.NGINX_USER_HEADER_VALUE)) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(R.failure(RCodeEnum.THE_SOURCE_OF_NGINX_TRAFFIC_IS_UNKNOWN)));
            return;
        }

        if (ObjectUtils.notEqual(request.getHeader(CommonConstant.GATEWAY_USER_HEADER), CommonConstant.GATEWAY_USER_HEADER_VALUE)) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(R.failure(RCodeEnum.THE_SOURCE_OF_GATEWAY_TRAFFIC_IS_UNKNOWN)));
            return;
        }

        try {
            String token = request.getHeader("Authorization");
            if (Objects.isNull(token)) {
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(JSON.toJSONString(R.failure(RCodeEnum.USER_LOGIN_TOKEN_IS_LOST)));
                return;
            }
            Claims claims = null;
            try {
                claims = jwtUtils.checkLoginTokenAndGetClaims(token);
            } catch (CustomizeReturnException e) {
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(JSON.toJSONString(R.failure(RCodeEnum.USER_LOGIN_HAS_EXPIRED)));
                return;
            } catch (JwtException | IllegalArgumentException e) {
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(JSON.toJSONString(R.failure(RCodeEnum.USER_LOGIN_TOKEN_IS_INVALID)));
                return;
            }
            TokenLoginVo tokenBody = jwtUtils.aesDecObject(String.valueOf(claims.get("tokenBody"))).toBean(TokenLoginVo.class);
            if (BeanMetaDataUtils.isAnyMetadataEmpty(tokenBody)) {
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(JSON.toJSONString(R.failure(RCodeEnum.USER_LOGIN_TOKEN_IS_INVALID)));
                return;
            }
            request.setAttribute("loginUser", tokenBody);
        } catch (CustomizeReturnException e) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(R.failure(RCodeEnum.USER_LOGIN_TOKEN_IS_INVALID)));
            return;
        }

        filterChain.doFilter(request, response);
    }
}
