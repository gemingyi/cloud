package com.example.commons.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * 生成二维码
 * 网上抄的 https://blog.csdn.net/wangmx1993328/article/details/83856391
 */
public class QRBarCodeUtil {

    /**
     * CODE_WIDTH：二维码宽度，单位像素
     * CODE_HEIGHT：二维码高度，单位像素
     * FRONT_COLOR：二维码前景色，0x000000 表示黑色
     * BACKGROUND_COLOR：二维码背景色，0xFFFFFF 表示白色
     * 演示用 16 进制表示，和前端页面 CSS 的取色是一样的，注意前后景颜色应该对比明显，如常见的黑白
     */
    private static final int CODE_WIDTH = 400;
    private static final int CODE_HEIGHT = 400;
    private static final int FRONT_COLOR = 0x000000;
    private static final int BACKGROUND_COLOR = 0xFFFFFF;


    /**
     * 生成二维码 并 保存为图片
     * @param codeContent        :二维码参数内容，如果是一个网页地址，如 https://www.baidu.com/ 则 微信扫一扫会直接进入此地址
     * 如果是一些参数，如 1541656080837，则微信扫一扫会直接回显这些参数值
     */
    public static String generateQrCode(String codeContent) {
        try {
            //参数检验
            if (codeContent == null || "".equals(codeContent)) {
                return null;
            }
            codeContent = codeContent.trim();

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.MARGIN, 1);

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(codeContent, BarcodeFormat.QR_CODE, CODE_WIDTH, CODE_HEIGHT, hints);

            BufferedImage bufferedImage = new BufferedImage(CODE_WIDTH, CODE_HEIGHT, BufferedImage.TYPE_INT_BGR);
            for (int x = 0; x < CODE_WIDTH; x++) {
                for (int y = 0; y < CODE_HEIGHT; y++) {
                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? FRONT_COLOR : BACKGROUND_COLOR);
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String codeContent1 = "https://www.baidu.com/";
        generateQrCode(codeContent1);

        String codeContent2 = "18774987061";
        generateQrCode(codeContent2);
    }

}
