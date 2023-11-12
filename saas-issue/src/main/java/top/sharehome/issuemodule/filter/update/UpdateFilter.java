package top.sharehome.issuemodule.filter.update;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import top.sharehome.issuemodule.common.constant.CommonConstant;
import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeReturnException;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.common.thread_handler.ThreadBaseHandler;
import top.sharehome.issuemodule.model.vo.token.TokenLoginVo;
import top.sharehome.issuemodule.utils.jwt.JwtUtils;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 信息更新相关接口过滤器
 *
 * @author AntonyCheng
 * @since 2023/7/22 21:02:20
 */
@WebFilter(filterName = "UpdateFilter", urlPatterns = "/*")
@Slf4j
public class UpdateFilter implements Filter {
    @Resource
    private JwtUtils jwtUtils;

    public static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUri = request.getRequestURI();

        String needHandleRequest = "/ag/issue/update/**";

        if (!ANT_PATH_MATCHER.match(needHandleRequest, requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        List<String> excludeUris = Arrays.asList();

        for (String s : excludeUris) {
            if (ANT_PATH_MATCHER.match(s, requestUri)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String token = request.getHeader(CommonConstant.TOKEN_HEADER);
        try {
            if (StringUtils.isEmpty(token)) {
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(JSON.toJSONString(R.failure(RCodeEnum.USER_LOGIN_TOKEN_IS_LOST)));
                return;
            }
            Claims claims = jwtUtils.checkLoginTokenAndGetClaims(token);
            String jsonBody = (String) claims.get("tokenBody");
            JSONObject jsonObject = jwtUtils.aesDecObject(jsonBody);
            TokenLoginVo loginToken = jsonObject.toBean(TokenLoginVo.class);
            if (!Objects.equals(loginToken.getIdentity(), CommonConstant.LOGIN_IDENTITY_ADMIN)) {
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(JSON.toJSONString(R.failure(RCodeEnum.ACCESS_UNAUTHORIZED)));
                return;
            }
            ThreadBaseHandler.setCurrentId(jwtUtils.aesDecLong(String.valueOf(jwtUtils.getHeaderInLoginToken(token).get("uid"))));
        } catch (CustomizeReturnException e) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(R.failure(RCodeEnum.USER_LOGIN_TOKEN_IS_INVALID)));
            return;
        }
        filterChain.doFilter(request, response);
    }
}
