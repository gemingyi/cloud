//package com.example.dddapplication.service;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.example.ddddomain.model.CommonImageModel;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//public interface IImageService {
//
//    /**
//     * 上传文件 返回OSS path
//     */
//    String saveImage(MultipartFile file) throws IOException;
//
//    /**
//     * 删除文件
//     */
//    int deleteImage(Integer imageId);
//
//    /**
//     * 分页查询文件
//     */
//    IPage<CommonImageModel> findPageImage(Page<CommonImageModel> page);
//
//    /**
//     * 获取文件
//     */
//    CommonImageModel getImageById(Integer imageId);
//
//    /**
//     * 下载文件
//     */
//    InputStream downloadImage(Integer imageId);
//
//}
