package top.sharehome.jwtdemo.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.AntPathMatcher;
import top.sharehome.jwtdemo.entity.Response;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 登录、注册以及退出过滤器
 *
 * @author AntonyCheng
 * @since 2023/7/4 17:26:20
 */
@WebFilter(filterName = "AccountFilter", urlPatterns = "/*")
@Slf4j
public class AccountFilter implements Filter {
    public static final String GATEWAY_HEADER = "ag-request-header";

    public static final String GATEWAY_HEADER_VALUE = "admin_gateway_request_header";

    public static final String TOKEN_HEADER = "Authorization";

    public static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUri = request.getRequestURI();

        String logoutRequestUri = "/ag/api/account/logout";
//        String loginRequestUri = "/ag/api/account/login";
//        String registerRequestUri = "/ag/api/account/register";

        if (ObjectUtils.notEqual(request.getHeader(GATEWAY_HEADER), GATEWAY_HEADER_VALUE)) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(new Response<String>(400, "流量来源不明", null)));
            return;
        }

        if (ANT_PATH_MATCHER.match(logoutRequestUri,requestUri)
                && Objects.isNull(request.getHeader(TOKEN_HEADER))){
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(new Response<String>(401, "凭证丢失，请重新登录", null)));
            return;
        }

        filterChain.doFilter(request, response);
    }
}
