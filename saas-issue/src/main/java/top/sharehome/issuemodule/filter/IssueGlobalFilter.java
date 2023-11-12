package top.sharehome.issuemodule.filter;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.annotation.Order;
import top.sharehome.issuemodule.common.constant.CommonConstant;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 签发过滤器
 *
 * @author AntonyCheng
 * @since 2023/7/4 17:26:20
 */
@WebFilter(filterName = "IssueGlobalFilter", urlPatterns = "/*")
@Slf4j
@Order(0)
public class IssueGlobalFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (ObjectUtils.notEqual(request.getHeader(CommonConstant.NGINX_ADMIN_HEADER), CommonConstant.NGINX_ADMIN_HEADER_VALUE)) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(R.failure(RCodeEnum.THE_SOURCE_OF_NGINX_TRAFFIC_IS_UNKNOWN)));
            return;
        }

        if (ObjectUtils.notEqual(request.getHeader(CommonConstant.GATEWAY_ADMIN_HEADER), CommonConstant.GATEWAY_ADMIN_HEADER_VALUE)) {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JSON.toJSONString(R.failure(RCodeEnum.THE_SOURCE_OF_GATEWAY_TRAFFIC_IS_UNKNOWN)));
            return;
        }

        filterChain.doFilter(request, response);
    }
}
