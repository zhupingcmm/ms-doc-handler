package com.ocbc.msdochandler;

import com.aspose.words.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * word转pdf工具类
 *
 * @author shmily
 */
@Slf4j
public class Word2PdfUtil {

    /**
     * 许可证字符串(可以放到resource下的xml文件中也可)
     */
    private static final String LICENSE = "<License>" +
            "<Data>" +
            "<Products><Product>Aspose.Total for Java</Product><Product>Aspose.Words for Java</Product></Products>" +
            "<EditionType>Enterprise</EditionType>" +
            "<SubscriptionExpiry>20991231</SubscriptionExpiry>" +
            "<LicenseExpiry>20991231</LicenseExpiry>" +
            "<SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>" +
            "</Data>" +
            "<Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>" +
            "</License>";


    /**
     * 设置 license 去除水印
     */
    private static void setLicense() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(LICENSE.getBytes());
        License license = new License();
        try {
            license.setLicense(byteArrayInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * word 转 pdf 生成至指定路径，pdf为空则上传至word同级目录
     *
     * @param wordPath word文件路径
     * @param pdfPath  pdf文件路径
     */
    public static void wordConvertPdfFile(String wordPath, String pdfPath) {
        FileOutputStream fileOutputStream = null;
        try {
            pdfPath = pdfPath == null ? getPdfFilePath(wordPath) : pdfPath;
            setLicense();
            File file = new File(pdfPath);
            fileOutputStream = new FileOutputStream(file);
            Document doc = new Document(wordPath);
            log.info("Try to save pdf file to: [{}]", pdfPath);

            String defaultFontName = FontSettings.getDefaultFontName();
            log.info("Before set F, [{}]", defaultFontName);
            FontSettings.setFontsFolder("/root/pzhu/fonts", false);
            defaultFontName = FontSettings.getDefaultFontName();
            log.info("After set F, [{}]", defaultFontName);
            PdfSaveOptions saveOptions = new PdfSaveOptions();
            saveOptions.setSaveFormat(SaveFormat.PDF);
            saveOptions.setEmbedFullFonts(true);
            doc.save(fileOutputStream, saveOptions);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fileOutputStream != null;
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * word 转 pdf 生成byte字节流
     *
     * @param wordPath word所在的目录地址
     * @return
     */
    public static byte[] wordConvertPdfByte(String wordPath) {
        ByteArrayOutputStream fileOutputStream = null;
        try {
            setLicense();
            fileOutputStream = new ByteArrayOutputStream();
            Document doc = new Document(wordPath);
            doc.save(fileOutputStream, SaveFormat.PDF);
            return fileOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fileOutputStream != null;
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }



    /**
     * 获取 生成的 pdf 文件路径，默认与源文件同一目录
     *
     * @param wordPath word文件
     * @return 生成的 pdf 文件
     */
    private static String getPdfFilePath(String wordPath) {
        int lastIndexOfPoint = wordPath.lastIndexOf(".");
        String pdfFilePath = "";
        if (lastIndexOfPoint > -1) {
            pdfFilePath = wordPath.substring(0, lastIndexOfPoint);
        }
        return pdfFilePath + ".pdf";
    }

    public static String getOutPath() {
        try {
            String userDir = System.getProperty("user.dir");
            String outDir = userDir + File.separator + "out";
            Path path = Paths.get(outDir);
            if (!Files.isDirectory(path)) {
                Files.createDirectories(path);
            }
            return outDir;
        } catch (IOException e) {
            throw new RuntimeException("无法创建上传目录", e);
        }

    }

}

