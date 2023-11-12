package top.sharehome.issuemodule.service.micro;

import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeReturnException;
import top.sharehome.issuemodule.model.entity.Tenant;
import top.sharehome.issuemodule.model.entity.Token;
import top.sharehome.issuemodule.model.entity.Update;

import java.util.List;
import java.util.Map;

/**
 * 用户信息录入Service（微服务）
 *
 * @author AntonyCheng
 * @since 2023/7/17 20:28
 */
public interface ManagerMicroService {
    /**
     * 管理员首次登陆激活账户接口
     *
     * @param managerTenantId 管理员所在租户ID
     * @return 租户完整信息
     * @throws CustomizeReturnException 自定义异常，以至于微服务程序不报错
     */
    public Tenant initManager(Long managerTenantId) throws CustomizeReturnException;

    /**
     * 添加管理员申请更新信息
     *
     * @param managerUpdate 申请更改信息的内容
     * @throws CustomizeReturnException 自定义异常，以至于微服务程序不报错
     */
    public void addUpdateManagerInfo(Update managerUpdate) throws CustomizeReturnException;

    /**
     * 管理员修改密码接口
     *
     * @param tenantUpdatePassword 更改密码的租户对象
     * @throws CustomizeReturnException 自定义异常，以至于微服务程序不报错
     */
    void updatePassword(Tenant tenantUpdatePassword) throws CustomizeReturnException;

    /**
     * 单个用户的登陆信息录入接口
     *
     * @param singleUser 单个用户的登录信息
     * @throws CustomizeReturnException 自定义异常，以至于微服务程序不报错
     */
    public void addSinglePerson(Token singleUser) throws CustomizeReturnException;

    /**
     * 多个用户的登陆信息录入接口
     *
     * @param multipleUser 多个用户的登陆信息
     * @throws CustomizeReturnException 自定义异常，以至于微服务程序不报错
     */
    public void addMultiplePerson(List<Token> multipleUser) throws CustomizeReturnException;

    /**
     * 单个用户的登陆信息删除接口
     *
     * @param singleUser 单个用户的登陆ID
     * @throws CustomizeReturnException 自定义异常，以至于微服务程序不报错
     */
    public void deleteSinglePerson(Token singleUser) throws CustomizeReturnException;

    /**
     * 多个用户的登陆信息删除接口
     *
     * @param multipleUsers 单个用户的登陆ID
     * @throws CustomizeReturnException 自定义异常，以至于微服务程序不报错
     */
    public void deleteMultiplePerson(List<Token> multipleUsers) throws CustomizeReturnException;

    /**
     * 单个用户的账号修改接口
     *
     * @param oldAndNewMap 用户的新旧账号
     * @throws CustomizeReturnException 自定义异常，以至于微服务程序不报错
     */
    public void updateSinglePersonAccount(Map<String, Object> oldAndNewMap) throws CustomizeReturnException;

    /**
     * 单个用户的密码重置接口
     *
     * @param singleUser 单个用户的重置信息
     * @throws CustomizeReturnException 自定义异常，以至于微服务程序不报错
     */
    public void resetSinglePersonPassword(Token singleUser) throws CustomizeReturnException;

    /**
     * 多个用户的密码重置接口
     *
     * @param multipleUsers 多个用户的重置信息
     * @throws CustomizeReturnException 自定义异常，以至于微服务程序不报错
     */
    public void resetMultiplePersonPassword(List<Token> multipleUsers) throws CustomizeReturnException;
}
