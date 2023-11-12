package top.sharehome.usergatewaymodule.predicates;

import io.jsonwebtoken.JwsHeader;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;
import top.sharehome.usergatewaymodule.common.constant.CommonConstant;
import top.sharehome.usergatewaymodule.common.exception_handler.customize.CustomizeReturnException;
import top.sharehome.usergatewaymodule.utils.JwtUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 租户ID自定义断言
 *
 * @author AntonyCheng
 * @since 2023/7/12 13:55:42
 */
@Component
public class TenantIdRoutePredicateFactory extends AbstractRoutePredicateFactory<TenantIdRoutePredicateFactory.Config> {

    @Resource
    private JwtUtils jwtUtils;

    public TenantIdRoutePredicateFactory() {
        super(TenantIdRoutePredicateFactory.Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("tenantId");
    }


    @Override
    public Predicate<ServerWebExchange> apply(TenantIdRoutePredicateFactory.Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange exchange) {
                try {
                    String token = exchange.getRequest().getHeaders().getFirst(CommonConstant.TOKEN_HEADER);
                    JwsHeader header = jwtUtils.getHeaderInLoginToken(token);
                    Long tenantId = jwtUtils.aesDecLong(String.valueOf(header.get("tid")));
                    return Objects.equals(config.getTenantId(), tenantId);
                } catch (CustomizeReturnException e) {
                    return false;
                }
            }
        };
    }

    @Validated
    public static class Config {
        private Long tenantId;

        public Long getTenantId() {
            return tenantId;
        }

        public void setTenantId(Long tenantId) {
            this.tenantId = tenantId;
        }
    }
}