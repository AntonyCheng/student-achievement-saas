package top.sharehome.issuemodule.common.response;

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
     * REST请求方式有误 D1023
     */
    REST_REQUEST_IS_DONE_IN_THE_WRONG_WAY("D1023", "REST请求方式有误"),

    /**
     * 姓名格式校验失败 D1024
     */
    TENANT_STATUS_FORMAT_VERIFICATION_FAILED("D1024", "租户状态格式校验失败"),

    /**
     * 发布Nacos配置文件失败 D1025
     */
    FAILED_TO_PUBLISH_NACOS_CONFIGURATION_FILE("D1025", "发布Nacos配置文件失败"),

    /**
     * 被删除目标不存在 D1026
     */
    THE_DELETED_TARGET_DOES_NOT_EXIST("D1026", "被删除目标不存在"),

    /**
     * 被更改目标不存在 D1027
     */
    THE_MODIFIED_TARGET_DOES_NOT_EXIST("D1027", "被更改目标不存在"),

    /**
     * 用户上传源代码太大 D1028
     */
    USER_UPLOADED_CODE_IS_TOO_LARGE("D1028", "用户上传源代码太大"),

    /**
     * COS删除对象异常 D1029
     */
    COS_DELETES_OBJECTS_EXCEPTIONALLY("D1029", "COS删除对象异常"),

    /**
     * 数据未发生变化 D1030
     */
    THE_DATA_HAS_NOT_CHANGED("D1030", "数据未发生变化"),

    /**
     * 用户状态不可被更改 D1031
     */
    THE_USER_STATUS_CANNOT_BE_CHANGED("D1031", "用户状态不可被更改"),

    /**
     * 用户信息还处于审核中 D1032
     */
    USER_INFORMATION_IS_STILL_UNDER_REVIEW("D1032", "用户信息还处于审核中"),

    /**
     * 数据不存在 D1033
     */
    DATA_DOES_NOT_EXIST("D1033", "数据不存在"),

    /**
     * 该数据已经被审核 D1034
     */
    DATA_HAS_BEEN_REVIEWED("D1034", "该数据已经被审核"),

    /**
     * 非管理员账号请联系管理员进行重置密码 D1035
     */
    CONTACT_YOUR_ADMINISTRATOR_TO_RESET_YOUR_PASSWORD("D1035", "非管理员账号请联系管理员进行重置密码"),

    /**
     * 邮箱与该用户非绑定 D1036
     */
    THE_MAILBOX_IS_NOT_BOUND_TO_THE_ACCOUNT("D1036", "邮箱与该用户非绑定"),

    /**
     * 请勿频繁获取验证码 D1037
     */
    DO_NOT_GET_VERIFICATION_CODES_FREQUENTLY("D1037", "请勿频繁获取验证码"),

    /**
     * 验证码无效 D1038
     */
    The_verification_code_is_invalid("D1038", "验证码无效"),

    /**
     * 租户更新信息状态格式校验失败 D1039
     */
    TENANT_UPDATE_INFORMATION_STATUS_FORMAT_VALIDATION_FAILED("D1039", "租户更新信息状态格式校验失败"),

    /**
     * 更新密码不能与原密码相同 D1040
     */
    THE_UPDATE_PASSWORD_CANNOT_BE_THE_SAME_AS_THE_ORIGINAL_PASSWORD("D1040", "更新密码不能与原密码相同"),

    /**
     * 平台管理端异常，请联系平台管理员 D1041
     */
    EXCEPTION_ON_THE_PLATFORM_MANAGEMENT_SIDE("D1041", "平台管理端异常，请联系平台管理员"),

    /**
     * 登陆者身份异常，请联系平台管理员 D1042
     */
    THE_IDENTITY_OF_THE_LOGON_IS_ABNORMAL("D1042", "登陆者身份异常，请联系平台管理员"),

    /**
     * 用户账号信息异常，请联系平台管理员 D1043
     */
    THE_USER_ACCOUNT_INFORMATION_IS_ABNORMAL("D1043", "用户账号信息异常，请联系平台管理员"),

    /**
     * 提交修改管理员信息申请异常，请联系平台管理员 D1044
     */
    FAILED_TO_SUBMIT_THE_ADMINISTRATOR_INFORMATION_MODIFICATION_REQUEST("D1044", "修改管理员信息异常，请联系平台管理员"),

    /**
     * 年级格式不正确 D1045
     */
    THE_GRADE_FORMAT_IS_INCORRECT("D1045", "年级格式不正确"),

    /**
     * 班级格式不正确 D1046
     */
    THE_CLASS_FORMAT_IS_INCORRECT("D1046", "班级格式不正确"),

    /**
     * 该专业已经存在于该年级 D1047
     */
    THE_MAJOR_ALREADY_EXISTS_IN_THAT_GRADE("D1047", "该专业已经存在于该年级"),

    /**
     * 该年级不包含任何专业 D1048
     */
    THIS_GRADE_DOES_NOT_CONTAIN_ANY_MAJORS("D1048", "该年级不包含任何专业"),

    /**
     * 该专业不包含任何班级 D1049
     */
    THIS_MAJOR_DOES_NOT_CONTAIN_ANY_CLASSES("D1049", "该专业不包含任何班级"),

    /**
     * 批量操作存在重复数据 D1050
     */
    THERE_IS_DUPLICATE_DATA_FOR_BULK_OPERATIONS("D1050", "批量操作存在重复数据"),

    /**
     * 无法删除已经存在学生的专业 D1051
     */
    CAN_NOT_DELETE_MAJOR_THAT_ALREADY_HAS_STUDENTS("D1051", "无法删除已经存在学生的专业，可以修改此专业的信息"),

    /**
     * 该专业不存在 D1052
     */
    THE_MAJOR_DOES_NOT_EXIST("D1052", "该专业不存在"),

    /**
     * 该班级不存在 D1053
     */
    THE_CLASS_DOES_NOT_EXIST("D1053", "该班级不存在"),

    /**
     * 无法批量添加多个专业的学生信息 D1054
     */
    CAN_NOT_BULK_ADD_STUDENT_INFORMATION_FOR_MULTIPLE_MAJORS("D1054", "无法批量添加多个专业的学生信息"),

    /**
     * 学生状态格式校验失败 D1055
     */
    STUDENT_STATUS_FORMAT_VERIFICATION_FAILED("D1055", "学生状态格式校验失败"),

    /**
     * 该教师不存在 D1054
     */
    THE_TEACHER_DOES_NOT_EXIST("D1054", "该教师不存在"),

    /**
     * 该学生不存在 D1055
     */
    THE_STUDENT_DOES_NOT_EXIST("D1055", "该学生不存在"),

    /**
     * 该教师角色不存在 D1056
     */
    THE_TEACHER_ROLE_DOES_NOT_EXIST("D1056", "该教师角色不存在"),

    /**
     * 该教师职称不存在 D1057
     */
    THE_TEACHER_JOB_TITLE_DOES_NOT_EXIST("D1057", "该教师职称不存在"),

    /**
     * 该教师角色已经存在 D1058
     */
    THE_TEACHER_ROLE_ALREADY_EXISTS("D1058", "该教师角色已经存在"),

    /**
     * 该教师职称已经存在 D1059
     */
    THE_TEACHER_JOB_TITLE_ALREADY_EXISTS("D1059", "该教师职称已经存在"),

    /**
     * 无法删除已经存在数据的荣誉类型 D1060
     */
    CAN_NOT_DELETE_HONOR_TYPE_THAT_ALREADY_HAS_DATA("D1060", "无法删除已经存在数据的荣誉类型，可以修改此类型的信息"),

    /**
     * 无法删除已经存在数据的竞赛类型 D1061
     */
    CAN_NOT_DELETE_COMPETITION_TYPE_THAT_ALREADY_HAS_DATA("D1061", "无法删除已经存在数据的竞赛类型，可以修改此类型的信息"),

    /**
     * 无法删除已经存在数据的成果类型 D1062
     */
    CAN_NOT_DELETE_ACHIEVEMENT_TYPE_THAT_ALREADY_HAS_DATA("D1062", "无法删除已经存在数据的成果类型，可以修改此类型的信息"),

    /**
     * 该荣誉类型已经存在 D1063
     */
    THE_HONOR_TYPE_ALREADY_EXISTS("D1063", "该荣誉类型已经存在"),

    /**
     * 该竞赛类型已经存在 D1064
     */
    THE_COMPETITION_TYPE_ALREADY_EXISTS("D1064", "该竞赛类型已经存在"),

    /**
     * 该成果类型已经存在 D1065
     */
    THE_ACHIEVEMENT_TYPE_ALREADY_EXISTS("D1065", "该成果类型已经存在"),

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
