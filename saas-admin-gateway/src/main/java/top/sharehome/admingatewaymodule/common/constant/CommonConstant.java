package top.sharehome.admingatewaymodule.common.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 通用常量类
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */

public interface CommonConstant {
    /**
     * 整个项目的JWT密钥（可修改，但不建议，如果要强行修改，请先确保Redis中无登录数据且整个项目所有分布式节点上密钥一致）
     */
    String JWT_KEY = "#UX3bwWwnU^sPC4T";

    /**
     * 整个项目的AES加密密钥（可修改，但不建议，如果强行修改，请先确保Redis中无登录数据且整个项目所有分布式节点上密钥一致）
     */
    String AES_KEY = "^yrknDV&%@*T56V&";

    /**
     * 整个项目的MD5加密盐（可修改，但不建议，如果强行修改，请先确保Redis中无登录数据且整个项目所有分布式节点上盐一致，真实加密内容为：<b>String realContent = content+MD5_KEY</b>）
     */
    String MD5_SALT = "^yrknDV&%@*T56V&";

    /**
     * 网关访问管理端请求头名称
     */
    String GATEWAY_ADMIN_HEADER = "ag-request-header";

    /**
     * 网关访问管理端请求头内容
     */
    String GATEWAY_ADMIN_HEADER_VALUE = "admin_gateway_request_header";

    /**
     * 网关访问管用户端请求头名称
     */
    String GATEWAY_USER_HEADER = "ug-request-header";

    /**
     * 网关访问用户端请求头内容
     */
    String GATEWAY_USER_HEADER_VALUE = "user_gateway_request_header";

    /**
     * Nginx访问管理端网关请求头
     */
    String NGINX_ADMIN_HEADER = "request-header";

    /**
     * Nginx访问管理端网关请求头内容
     */
    String NGINX_ADMIN_HEADER_VALUE = "admin_nginx_request_header";

    /**
     * Nginx访问管理端网关请求头
     */
    String NGINX_USER_HEADER = "request-header";

    /**
     * Nginx访问管理端网关请求头内容
     */
    String NGINX_USER_HEADER_VALUE = "user_nginx_request_header";

    /**
     * 用于存放TOKEN的请求头名称
     */
    String TOKEN_HEADER = "Authorization";

    /**
     * 用户身份（0表示学生，1表示老师，2表示租户负责人，3表示平台超级管理员）
     */
    String LOGIN_IDENTITY_STUDENT = "0";
    String LOGIN_IDENTITY_TEACHER = "1";
    String LOGIN_IDENTITY_MANAGER = "2";
    String LOGIN_IDENTITY_ADMIN = "3";

    /**
     * 租户状态（0表示待审核，1表示启用，2表示禁用，3表示审核未通过）
     */
    int TENANT_STATUS_CENSOR = 0;
    int TENANT_STATUS_ENABLE = 1;
    int TENANT_STATUS_DISABLE = 2;
    int TENANT_STATUS_CENSOR_DISABLE = 3;
    List<Integer> TENANT_STATUS_LIST = Arrays.asList(TENANT_STATUS_CENSOR, TENANT_STATUS_ENABLE, TENANT_STATUS_DISABLE, TENANT_STATUS_CENSOR_DISABLE);

    /**
     * 登陆账号状态（0表示启用，1表示禁用）
     */
    int LOGIN_STATUS_ENABLE = 0;
    int LOGIN_STATUS_DISABLE = 1;

    /**
     * 用户性别
     */
    int GENDER_WOMAN = 0;
    int GENDER_MAN = 1;

    /**
     * 租户等级（0表示0-500，1表示501-1000，2表示1001-1500，3表示1501-2000，4表示2000人以上）
     */
    int LEVEL_1 = 0;
    int LEVEL_2 = 1;
    int LEVEL_3 = 2;
    int LEVEL_4 = 3;
    int LEVEL_5 = 4;
    List<Integer> LEVEL_LIST = Arrays.asList(LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5);

    /**
     * 账号的匹配表达式
     */
    String MATCHER_ACCOUNT_REGEX = "^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$";

    /**
     * 姓名的匹配表达式
     */
    String MATCHER_NAME_REGEX = "^[\u4e00-\u9fa5.·]{0,}$";

    /**
     * 邮箱的匹配表达式
     */
    String MATCHER_EMAIL_REGEX = "([a-z0-9A-Z_]+[-|\\.]?)+[a-z0-9A-Z_]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}";

    /**
     * 校验IPv4地址的匹配表达式
     */
    String MATCHER_IP_REGEX = "^(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])$";

    /**
     * 校验Port端口的匹配表达式
     */
    String MATCHER_PORT_REGEX = "^([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{4}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$";

    /**
     * 账号长度最小值
     */
    int ACCOUNT_GE_LENGTH = 6;

    /**
     * 账号长度最大值
     */
    int ACCOUNT_LE_LENGTH = 16;

    /**
     * 密码长度最小值
     */
    int PASSWORD_GE_LENGTH = 6;

    /**
     * 密码长度最大值
     */
    int PASSWORD_LE_LENGTH = 16;

    /**
     * 审核验证成功与否
     */
    int CENSOR_PASS = 0;
    int CENSOR_NOT_PASS = 1;

}
