package org.encoder.common.services;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipFileBuilderService {

    public void zipFolder(String folderPath, String zipFilePath) {

        Path sourceFolderPath = Paths.get(folderPath);
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath))) {

            Files.walk(sourceFolderPath)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceFolderPath.relativize(path).toString());

                        try (InputStream inputStream = Files.newInputStream(path)) {

                            zipOut.putNextEntry(zipEntry);
                            byte[] buffer = new byte[1024];
                            int length;

                            while ((length = inputStream.read(buffer)) > 0) {

                                zipOut.write(buffer, 0, length);
                            }

                            zipOut.closeEntry();

                        } catch (IOException e) {

                            throw new RuntimeException(e);
                        }
                    });

            Files.walk(sourceFolderPath)
                    .filter(path -> !path.equals(sourceFolderPath))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
