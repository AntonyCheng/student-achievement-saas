package top.sharehome.issuemodule.service.impl;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.sharehome.issuemodule.service.FileCosService;
import top.sharehome.issuemodule.utils.cos.CosUtils;

import javax.annotation.Resource;

/**
 * 文件ServiceImpl
 *
 * @author AntonyCheng
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileCosServiceImpl implements FileCosService {

    @Resource
    private CosUtils cosUtils;

    @Override
    public String upload(MultipartFile file, String rootPath) {
        return cosUtils.uploadToCos(file, rootPath);
    }

    @Override
    public void delete(String url) {
        cosUtils.deleteInCos(url);
    }
}