package top.sharehome.issuemodule.filter.token;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import top.sharehome.issuemodule.common.constant.CommonConstant;
import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeReturnException;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.common.thread_handler.ThreadBaseHandler;
import top.sharehome.issuemodule.utils.jwt.JwtUtils;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 登录信息相关接口过滤器
 *
 * @author AntonyCheng
 * @since 2023/7/9 13:15:52
 */
@WebFilter(filterName = "TokenFilter", urlPatterns = "/*")
@Slf4j
public class TokenFilter implements Filter {

    @Resource
    private JwtUtils jwtUtils;
    public static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUri = request.getRequestURI();

        String needHandleRequest = "/ag/issue/token/**";

        if (!ANT_PATH_MATCHER.match(needHandleRequest, requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        List<String> excludeUris = Arrays.asList("/ag/issue/token/login");

        for (String s : excludeUris) {
            if (ANT_PATH_MATCHER.match(s, requestUri)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String token = request.getHeader(CommonConstant.TOKEN_HEADER);
        if (StringUtils.isEmpty(token)) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(R.failure(RCodeEnum.USER_LOGIN_TOKEN_IS_LOST)));
            return;
        }

        try {
            ThreadBaseHandler.setCurrentId(jwtUtils.aesDecLong(String.valueOf(jwtUtils.getHeaderInLoginToken(token).get("uid"))));
        }catch (CustomizeReturnException e){
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(R.failure(RCodeEnum.USER_LOGIN_TOKEN_IS_INVALID)));
            return;
        }
        filterChain.doFilter(request, response);
    }
}
