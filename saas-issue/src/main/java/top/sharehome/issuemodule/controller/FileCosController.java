package top.sharehome.issuemodule.controller;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.sharehome.issuemodule.common.exception_handler.customize.CustomizeReturnException;
import top.sharehome.issuemodule.common.response.R;
import top.sharehome.issuemodule.common.response.RCodeEnum;
import top.sharehome.issuemodule.service.FileCosService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * COS功能相关接口
 *
 * @author AntonyCheng
 */
@RestController
@RequestMapping("/file")
@CrossOrigin
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileCosController {
    @Resource
    private FileCosService fileCosService;
    /**
     * 头像可以通过的文件格式
     */
    private static final List<String> PICTURE_FORMATS = new ArrayList<>(Arrays.asList("png", "jpg", "jpeg"));
    /**
     * 其他文件可以通过的文件格式
     */
    private static final List<String> FILE_FORMATS = new ArrayList<>(Arrays.asList(
            "png", "jpg", "jpeg", "gif", "pdf",
            "xlsx", "xls", "doc", "docx",
            "ppt", "pptx", "mp3", "mp4", "mpeg",
            "zip", "rar", "7z",
            "py", "java", "c", "cpp", "go", "html", "js", "ts", "sql", "css"));

    /**
     * 注册材料照片上传（无需权限）
     *
     * @param file 用户头像上传的文件
     * @return 返回文件链接
     */
    @PostMapping("/picture_upload")
    public R<String> pictureUpload(MultipartFile file) {

        if (Objects.isNull(file)) {
            throw new CustomizeReturnException(R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY));
        }
        String originalFilename = file.getOriginalFilename();
        if (Objects.isNull(originalFilename)) {
            throw new CustomizeReturnException(R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY));
        }
        String[] split = originalFilename.split("\\.");
        String suffix = split[split.length - 1];
        if (!PICTURE_FORMATS.contains(suffix.toLowerCase())) {
            throw new CustomizeReturnException(R.failure(RCodeEnum.USER_UPLOADED_FILE_TYPE_MISMATCH));
        }
        long size = file.getSize();
        if (size / 1024 /1024 >= 5) {
            throw new CustomizeReturnException(R.failure(RCodeEnum.USER_UPLOADED_IMAGE_IS_TOO_LARGE));
        }
        String url = fileCosService.upload(file, "tenant_register_picture");
        return R.success(url, "上传图片成功");
    }

    /**
     * 其他文件上传
     *
     * @param file 用户上传的文件
     * @return 返回上传信息
     */
    @PostMapping("/file_upload")
    public R<String> fileUpload(MultipartFile file) {
        if (file == null) {
            throw new CustomizeReturnException(R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY));
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new CustomizeReturnException(R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY));
        }
        String[] split = originalFilename.split("\\.");
        String suffix = split[split.length - 1].toLowerCase();
        if (!FILE_FORMATS.contains(suffix)) {
            throw new CustomizeReturnException(R.failure(RCodeEnum.USER_UPLOADED_FILE_TYPE_MISMATCH));
        }
        long size = file.getSize();
        if (Arrays.asList("py", "java", "c", "cpp", "go", "html", "js", "ts", "sql", "css").contains(suffix)) {
            if (size / 1024 /1024 >= 1) {
                throw new CustomizeReturnException(R.failure(RCodeEnum.USER_UPLOADED_CODE_IS_TOO_LARGE));
            }
        }
        if (Arrays.asList("png", "jpg", "jpeg", "gif").contains(suffix)) {
            if (size / 1024 / 1024 >= 5) {
                throw new CustomizeReturnException(R.failure(RCodeEnum.USER_UPLOADED_IMAGE_IS_TOO_LARGE));
            }
        }
        if (Arrays.asList("pdf", "xlsx", "xls", "doc", "docx", "ppt", "pptx").contains(suffix)) {
            if (size / 1024 / 1024 >= 10) {
                throw new CustomizeReturnException(R.failure(RCodeEnum.USER_UPLOADED_FILE_IS_TOO_LARGE));
            }
        }
        if (Arrays.asList("mp3", "mp4").contains(suffix)) {
            if (size / 1024 / 1024 >= 50) {
                throw new CustomizeReturnException(R.failure(RCodeEnum.USER_UPLOADED_VIDEO_IS_TOO_LARGE));
            }
        }
        if (Arrays.asList("rar", "7z", "zip").contains(suffix)) {
            if (size / 1024 / 1024 >= 50) {
                throw new CustomizeReturnException(R.failure(RCodeEnum.USER_UPLOADED_VIDEO_IS_TOO_LARGE));
            }
        }
        String url = fileCosService.upload(file, "file/" + suffix);
        return R.success(url, "上传文件成功");
    }

    /**
     * COS文件删除（无需权限）
     *
     * @param cosUrl oss链接
     * @return 返回删除结果
     */
    @DeleteMapping("/file_delete")
    public R<String> fileDelete(String cosUrl) {
        if (cosUrl == null) {
            throw new CustomizeReturnException(R.failure(RCodeEnum.REQUEST_REQUIRED_PARAMETER_IS_EMPTY));
        }
        if (!cosUrl.contains("myqcloud.com")) {
            throw new CustomizeReturnException(R.failure(RCodeEnum.PARAMETER_FORMAT_MISMATCH));
        }
        fileCosService.delete(cosUrl);
        return R.success("COS文件删除成功");
    }
}
