package top.sharehome.gatewaydemo.common.response;

import lombok.Getter;

/**
 * 响应码枚举类
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
@Getter
public enum RCodeEnum {

    // 以下（Axxxx，Bxxxx，Cxxxx）完全遵守阿里巴巴开发规范中给出的响应码进行设计

    /**
     * 一切OK 00000
     */
    SUCCESS("00000", "请求正常响应"),

    /**
     * 账户名称校验失败 A0110
     */
    USERNAME_VALIDATION_FAILED("A0110", "账户名称校验失败"),

    /**
     * 账户名称已经存在 A0111
     */
    USERNAME_ALREADY_EXISTS("A0111", "账户名称已经存在"),

    /**
     * 账户名称包含特殊字符 A0113
     */
    USERNAME_CONTAINS_SPECIAL_CHARACTERS("A0113", "账户名称包含特殊字符"),

    /**
     * 密码校验失败 A0120
     */
    PASSWORD_VERIFICATION_FAILED("A0120", "密码校验失败"),

    /**
     * 验证码输入错误 A0130
     */
    INCORRECT_VERIFICATION_CODE("A0130", "验证码输入错误"),

    /**
     * 用户基本信息校验失败 A0150
     */
    USER_BASIC_INFORMATION_VERIFICATION_FAILED("A0150", "用户基本信息校验失败"),

    /**
     * 手机格式校验失败 A0151
     */
    PHONE_FORMAT_VERIFICATION_FAILED("A0151", "手机格式校验失败"),

    /**
     * 邮箱格式校验失败 A0153
     */
    EMAIL_FORMAT_VERIFICATION_FAILED("A0153", "邮箱格式校验失败"),

    /**
     * 用户账户不存在 A0201
     */
    USER_ACCOUNT_DOES_NOT_EXIST("A0201", "用户账户不存在"),

    /**
     * 用户账户被封禁 A0202
     */
    USER_ACCOUNT_BANNED("A0202", "用户账户被封禁"),

    /**
     * 用户密码错误 A0210
     */
    WRONG_USER_PASSWORD("A0210", "用户密码错误"),

    /**
     * 用户登录已过期 A0230
     */
    USER_LOGIN_HAS_EXPIRED("A0230", "用户登录已过期"),

    /**
     * 访问未授权 A0301
     */
    ACCESS_UNAUTHORIZED("A0301", "访问未授权"),

    /**
     * 请求必填参数为空 A0410
     */
    REQUEST_REQUIRED_PARAMETER_IS_EMPTY("A0410", "请求必填参数为空"),

    /**
     * 参数格式不匹配 A0421
     */
    PARAMETER_FORMAT_MISMATCH("A0421", "参数格式不匹配"),

    /**
     * 用户操作异常 A0440
     */
    ABNORMAL_USER_OPERATION("A0440", "用户操作异常"),

    /**
     * 用户上传文件异常 A0700
     */
    FILE_UPLOAD_EXCEPTION("A0700", "用户上传文件异常"),

    /**
     * 用户上传文件类型不匹配 A0701
     */
    USER_UPLOADED_FILE_TYPE_MISMATCH("A0701", "用户上传文件类型不匹配"),

    /**
     * 用户上传文件太大 A0702
     */
    USER_UPLOADED_FILE_IS_TOO_LARGE("A0702", "用户上传文件太大"),

    /**
     * 用户上传图片太大 A0703
     */
    USER_UPLOADED_IMAGE_IS_TOO_LARGE("A0703", "用户上传图片太大"),

    /**
     * 用户上传视频太大 A0704
     */
    USER_UPLOADED_VIDEO_IS_TOO_LARGE("A0704", "用户上传视频太大"),

    /**
     * 用户上传压缩文件太大 A0705
     */
    USER_UPLOADED_ZIP_IS_TOO_LARGE("A0705", "用户上传压缩文件太大"),

    /**
     * 用户设备异常 A1000
     */
    ABNORMAL_USER_EQUIPMENT("A1000", "用户设备异常"),

    /**
     * 中间件服务出错 C0100
     */
    MIDDLEWARE_SERVICE_ERROR("C0100", "中间件服务出错"),

    /**
     * 网关服务出错 C0154
     */
    GATEWAY_SERVICE_ERROR("C0154", "网关服务出错"),

    /**
     * 数据库服务出错 C0300
     */
    ERRORS_OCCURRED_IN_THE_DATABASE_SERVICE("C0300", "数据库服务出错"),


    // 如需添加其他，请先查阅“阿里巴巴开发手册响应码.md”，如果有，请按照上方格式在上方进行添加，如果没有，请按照上方格式在下方进行添加
    // 注释约定：注释名即message内容，后需空格，然后跟上响应码，响应码即code内容
    // 名称约定：按照百度翻译，谷歌翻译等进行直译，全大写，下划线分割单词

    /**
     * 网关流量来源不明 D1000
     */
    THE_SOURCE_OF_GATEWAY_TRAFFIC_IS_UNKNOWN("D1000", "网关流量来源不明"),

    /**
     * 登陆凭证丢失 D1001
     */
    USER_LOGIN_TOKEN_IS_LOST("D1001", "登陆凭证丢失"),

    /**
     * 账户处于审核中 D1002
     */
    USER_ACCOUNT_IS_CENSOR("D1002", "账户处于审核中"),

    /**
     * 禁止多用户同时登录同一账号 D1003
     */
    CANNOT_BE_MULTIPLAYER_ONLINE("D1003", "禁止多用户同时登录同一账号"),

    /**
     * 用户登陆凭证无效 D1004
     */
    USER_LOGIN_TOKEN_IS_INVALID("D1004", "用户登陆凭证无效"),

    /**
     * 加密内容异常 D1005
     */
    ENCRYPTED_CONTENT_IS_ABNORMAL("D1005", "加密内容异常"),

    /**
     * 确认密码与密码不相同 D1006
     */
    PASSWORD_AND_CONFIRM_PASSWORD_ARE_NOT_THE_SAME("D1006", "确认密码与密码不相同"),

    /**
     * 账号长度不匹配 D1007
     */
    ACCOUNT_LENGTH_DO_NOT_MATCH("D1007", "账号长度不匹配"),

    /**
     * 账号长度不匹配 D1008
     */
    PASSWORD_LENGTH_DO_NOT_MATCH("D1008", "密码长度不匹配"),

    /**
     * 姓名格式校验失败 D1009
     */
    NAME_FORMAT_VERIFICATION_FAILED("D1009", "姓名格式校验失败"),

    /**
     * 账号或密码不能包含空格 D1010
     */
    ACCOUNT_OR_PASSWORDS_CANNOT_CONTAIN_SPACES("D1010", "账号或密码不能包含空格"),

    /**
     * 不满足数据二元性 D1011
     */
    DATA_BINARY_IS_NOT_SATISFIED("D1011", "不满足数据二元性"),

    /**
     * 姓名格式校验失败 D1012
     */
    TENANT_LEVEL_FORMAT_VERIFICATION_FAILED("D1012", "租户等级格式校验失败"),

    /**
     * 数据库添加数据失败 D1013
     */
    DB_DATA_ADDITION_FAILED("D1013", "系统添加数据失败"),

    /**
     * 数据库删除数据失败 D1014
     */
    DB_DATA_DELETION_FAILED("D1014", "系统删除数据失败"),

    /**
     * 数据库修改数据失败 D1015
     */
    DB_DATA_MODIFICATION_FAILED("D1015", "系统修改数据失败"),

    /**
     * IP格式校验失败 D1016
     */
    IP_FORMAT_VERIFICATION_FAILED("D1016", "IP格式校验失败"),

    /**
     * PORT格式校验失败 D1017
     */
    PORT_FORMAT_VERIFICATION_FAILED("D1017", "PORT格式校验失败"),

    /**
     * 消息队列从提供者到交换机 D1018
     */
    PROVIDER_TO_EXCHANGE_ERROR("D1018", "消息队列：提供者到交换机出错"),

    /**
     * 消息队列从提供者到交换机 D1019
     */
    EXCHANGE_TO_QUEUE_ERROR("D1019", "消息队列：交换机到队列出错"),

    /**
     * 账户审核不通过 D1020
     */
    USER_ACCOUNT_IS_CENSOR_FAILURE("D1020", "账户审核不通过"),

    /**
     * 该账户已经被审核 D1021
     */
    ACCOUNT_HAS_BEEN_REVIEWED("D1021", "该账户已经被审核"),

    /**
     * Nginx流量来源不明 D1022
     */
    THE_SOURCE_OF_NGINX_TRAFFIC_IS_UNKNOWN("D1022", "Nginx流量来源不明"),

    /**
     * 系统未知异常 Z0000
     */
    SYSTEM_UNKNOWN_EXCEPTION("Z0000", "系统未知异常");

    /**
     * 枚举响应码
     */
    private String code;

    /**
     * 枚举响应消息
     */
    private String message;

    RCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
