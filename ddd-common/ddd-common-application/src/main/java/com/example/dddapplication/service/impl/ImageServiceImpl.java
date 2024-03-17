package com.example.dddapplication.service.impl;//package com.example.commonserver.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.example.commonserver.dao.CommonImageMapper;
//import com.example.commonserver.dao.model.CommonImage;
//import com.example.commonserver.oss.OSSUtil;
//import com.example.commonserver.service.IImageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.math.BigDecimal;
//
//@Service
//public class ImageServiceImpl implements IImageService {
//
//    @Autowired
//    private CommonImageMapper commonImageMapper;
//    @Autowired
//    private OSSUtil ossUtil;
//
//
//    @Override
//    public String saveImage(MultipartFile file) throws IOException {
//        //
//        String filePath = ossUtil.save(file.getInputStream(), file.getOriginalFilename());
//        //
//        CommonImage commonImage = new CommonImage();
//        commonImage.setFileSize(BigDecimal.valueOf(file.getSize()));
////        commonImage.setFileType(file.getOriginalFilename().substring());
//        commonImage.setFilePath(filePath);
//        commonImage.setIsDelete(0);
//        commonImageMapper.insert(commonImage);
//        return filePath;
//    }
//
//    @Override
//    public int deleteImage(Integer imageId) {
//        // 删除OOS
////        CommonImage commonImage = commonImageMapper.selectById(imageId);
////        ossUtil.delete(commonImage.getFilePath());
//        //
//        return commonImageMapper.updateIsDeleteById(imageId);
//    }
//
//    @Override
//    public IPage<CommonImage> findPageImage(Page<CommonImage> page) {
//        QueryWrapper<CommonImage> queryWrapper = new QueryWrapper<>();
//
//        return commonImageMapper.selectPage(page, queryWrapper);
//    }
//
//    @Override
//    public CommonImage getImageById(Integer imageId) {
//        return commonImageMapper.selectById(imageId);
//    }
//
//    @Override
//    public InputStream downloadImage(Integer imageId) {
//        CommonImage commonImage = commonImageMapper.selectById(imageId);
//        return ossUtil.getFile(commonImage.getFilePath());
//    }
//}
