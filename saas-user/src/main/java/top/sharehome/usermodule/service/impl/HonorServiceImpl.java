package top.sharehome.usermodule.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.sharehome.usermodule.model.entity.Honor;
import top.sharehome.usermodule.mapper.HonorMapper;
import top.sharehome.usermodule.service.HonorService;
@Service
public class HonorServiceImpl extends ServiceImpl<HonorMapper, Honor> implements HonorService{

}
