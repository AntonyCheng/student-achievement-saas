package top.sharehome.usermodule.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.sharehome.usermodule.model.entity.Achievement;
import top.sharehome.usermodule.mapper.AchievementMapper;
import top.sharehome.usermodule.service.AchievementService;
@Service
public class AchievementServiceImpl extends ServiceImpl<AchievementMapper, Achievement> implements AchievementService{

}
