package top.sharehome.issuemodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sharehome.issuemodule.model.entity.Tenant;

/**
 * 租户注册相关接口Mapper（租户提交注册申请、平台管理员审核租户申请等）
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {
}