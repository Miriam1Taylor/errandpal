package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);

        try {
            // 原始文件名
            String originalFilename = file.getOriginalFilename();
            // 后缀名
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 生成新文件名
            String newFileName = UUID.randomUUID().toString() + extension;

            // 修改为 Flask 的目标路径
            String targetFolder = "E:\\Desktop\\WebImg";
            File targetDir = new File(targetFolder);
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            // 保存文件到指定位置
            File localFile = new File(targetDir, newFileName);
            file.transferTo(localFile);

            // 返回 Flask 静态服务地址
            String url = "http://localhost:5000/images/" + newFileName;
            return Result.success(url);
        } catch (IOException e) {
            log.error("文件保存失败：{}", e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

//    @PostMapping("/upload")
//    @ApiOperation("文件上传")
//    public Result<String> upload(MultipartFile file) {
//        log.info("文件上传：{}", file);
//
//        try {
////        原始文件名
//            String originalFilename = file.getOriginalFilename();
//
////        截取原始文件名的后缀
//            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//
////        构造新文件名称
//            String objectName = UUID.randomUUID().toString() + extension;
//
////        文件的请求路径
//            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
//            return Result.success(filePath);
//        } catch (IOException e) {
//            log.error("文件删除失败：{}", e);
//        }
//
//        return Result.error(MessageConstant.UPLOAD_FAILED);
//    }

}