package com.ocbc.msdochandler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.ocbc.msdochandler.Word2PdfUtil.getOutPath;
import static com.ocbc.msdochandler.Word2PdfUtil.wordConvertPdfFile;

@RestController
@RequestMapping("/api/files")
public class FileController {


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("文件不能为空");
        }
        String outPath = getOutPath();

        try {
            Path filePath = Paths.get(outPath, file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            wordConvertPdfFile(outPath + File.separator + file.getOriginalFilename(), outPath + File.separator + "success.pdf");
            return ResponseEntity.ok("文件上传成功: " + filePath.getFileName());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败");
        }
    }

    @GetMapping("word2PdfByte")
    public byte[] word2PdfByte() {
        return Word2PdfUtil.wordConvertPdfByte("D:\\code\\pdf\\word.docx");
    }


}
