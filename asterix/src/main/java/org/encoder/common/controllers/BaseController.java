package org.encoder.common.controllers;

import lombok.SneakyThrows;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;

@CrossOrigin(origins = "http://localhost:80")
@RestController
public class BaseController {

    @SneakyThrows
    protected ResponseEntity<Resource> serveZipFile(String zipFilePath) {

        File zipFile = new File(zipFilePath);

        if (!zipFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));

        ResponseEntity<Resource> response = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFile.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(zipFile.length())
                .body(resource);

        new Thread(() -> {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            zipFile.delete();
        }).start();

        return response;
    }
}
