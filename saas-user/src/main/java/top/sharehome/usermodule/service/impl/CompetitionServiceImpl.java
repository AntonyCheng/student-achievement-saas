package top.sharehome.usermodule.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.sharehome.usermodule.model.entity.Competition;
import top.sharehome.usermodule.mapper.CompetitionMapper;
import top.sharehome.usermodule.service.CompetitionService;
@Service
public class CompetitionServiceImpl extends ServiceImpl<CompetitionMapper, Competition> implements CompetitionService{

}
