package top.sharehome.issuemodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sharehome.issuemodule.model.entity.Token;

/**
 * 用户登陆相关接口Mapper（用户登陆、用户登出等）
 *
 * @author AntonyCheng
 * @since 2023/7/7 15:41:24
 */
@Mapper
public interface TokenMapper extends BaseMapper<Token> {
}