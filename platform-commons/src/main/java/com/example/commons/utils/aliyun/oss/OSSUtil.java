package com.example.commons.utils.aliyun.oss;//package com.demo.common.utils.aliyun.oss;
//
//import com.aliyun.oss.OSSClient;
//import com.aliyun.oss.model.DeleteObjectsRequest;
//import com.aliyun.oss.model.OSSObjectSummary;
//import com.aliyun.oss.model.ObjectListing;
//import com.demo.common.utils.date.DateUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.InputStream;
//import java.util.Date;
//import java.util.List;
//
//@Component
//public class OSSUtil {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(OSSUtil.class);
//
//    @Autowired
//    private OSSProperties ossProperties;
//
//    private OSSClient ossClient;
//    private static final String SEPARATOR = File.separator;
//
//
//    /**
//     * 初始化OOS 客户端
//     *
//     * @author: mingyi ge
//     * @date: 2019/8/29 13:31
//     */
//    @PostConstruct
//    public void initOSSClient() {
//        ossClient = new OSSClient(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
//    }
//
//
//    /**
//     * 上传文件(byte数组)
//     *
//     * @author: mingyi ge
//     * @date: 2019/8/29 13:40
//     * @params:
//     * @return:
//     */
//    public String save(byte[] content, String fileName) {
//        String key = generateOSSKey(ossProperties.getAttachmentPath(), fileName);
//        ossClient.putObject(ossProperties.getBucketName(), key, new ByteArrayInputStream(content));
//        return key;
//    }
//
//    /**
//     * 上传文件（IO流）
//     * @author: mingyi ge
//     * @date: 2019/8/30 9:51
//     * @params:
//     * @return:
//     */
//    public String save(InputStream inputStream, String fileName) {
//        String key = generateOSSKey(ossProperties.getAttachmentPath(), fileName);
//        ossClient.putObject(ossProperties.getBucketName(), key, inputStream);
//        return key;
//    }
//
//    /**
//     * 上传文件(byte数组)
//     *
//     * @author: mingyi ge
//     * @date: 2019/8/29 13:41
//     * @params:
//     * @return:
//     */
//    public String save(File file) {
//        String key = generateOSSKey(ossProperties.getAttachmentPath(), file.getName());
//        ossClient.putObject(ossProperties.getBucketName(), key, file);
//        return key;
//    }
//
//    /**
//     * 上传文件到指定前缀路径下
//     *
//     * @author: mingyi ge
//     * @date: 2019/8/29 13:41
//     * @params:
//     * @return:
//     */
//    public String save(byte[] content, String fileName, String prefix) {
//        String key = generateOSSKey(prefix, fileName);
//        ossClient.putObject(ossProperties.getBucketName(), key, new ByteArrayInputStream(content));
//        return key;
//    }
//
//    /**
//     * 拷贝文件
//     * @author: mingyi ge
//     * @date: 2019/8/30 9:33
//     * @params: sourceKey:原来的OSSKey, fileName:文件名称
//     * @return:
//     */
//    public String copy(String sourceKey, String fileName) {
//        boolean flag = this.isExist(sourceKey);
//        if(flag) {
//            String destinationKey = generateOSSKey(ossProperties.getAttachmentPath(), fileName);
//            ossClient.copyObject(ossProperties.getBucketName(), sourceKey, ossProperties.getBucketName(), destinationKey);
//            return destinationKey;
//        }
//        return null;
//    }
//
//    /**
//     * 删除文件
//     *
//     * @author: mingyi ge
//     * @date: 2019/8/29 13:37
//     * @params:
//     * @return:
//     */
//    public void delete(String key) {
//        ossClient.deleteObject(ossProperties.getBucketName(), key);
//    }
//
//    /**
//     * 批量删除文件
//     *
//     * @author: mingyi ge
//     * @date: 2019/8/29 13:37
//     * @params:
//     * @return:
//     */
//    public void batchDelete(List<String> keys) {
//        ossClient.deleteObject(new DeleteObjectsRequest(ossProperties.getBucketName()).withKeys(keys));
//    }
//
//    /**
//     * 判断文件是否存在
//     *
//     * @author: mingyi ge
//     * @date: 2019/8/29 13:38
//     * @params:
//     * @return:
//     */
//    public boolean isExist(String key) {
//        return ossClient.doesObjectExist(ossProperties.getBucketName(), key);
//    }
//
//    /**
//     * 获取文件列表
//     *
//     * @author: mingyi ge
//     * @date: 2019/8/29 13:59
//     * @params:
//     * @return:
//     */
//    public List<OSSObjectSummary> listAttachment() {
//        ObjectListing objectListing = ossClient.listObjects(ossProperties.getBucketName());
//        List<OSSObjectSummary> ossObjectSummaryList = objectListing.getObjectSummaries();
//        return ossObjectSummaryList;
//    }
//
//    /**
//     * 下载文件(IO流)
//     *
//     * @author: mingyi ge
//     * @date: 2019/8/29 13:40
//     * @params:
//     * @return:
//     */
//    public InputStream getFile(String key) {
//        InputStream input = null;
//        try {
//            input = ossClient.getObject(ossProperties.getBucketName(), key).getObjectContent();
//        } catch (com.aliyun.oss.OSSException e) {
//            LOGGER.info("{} 不存在", key);
//        }
//        return input;
//    }
//
//    /**
//     * 生成文件 key
//     * 格式   /yyyy/MM/yyyyMMddHHmmss-fileName
//     *
//     * @author: mingyi ge
//     * @date: 2019/8/29 13:41
//     * @params:
//     * @return:
//     */
//    private String generateOSSKey(String prefix, String fileName) {
//        Date date = new Date();
//        String year = DateUtil.getYearStr(date);
//        String month = DateUtil.getMonthStr(date);
//        return prefix + SEPARATOR + year + SEPARATOR + month + SEPARATOR + DateUtil.getYMDHMS(date) + "-" + fileName;
//    }
//
//}
