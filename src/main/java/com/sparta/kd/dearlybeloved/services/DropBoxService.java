package com.sparta.kd.dearlybeloved.services;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class DropBoxService {

    @Value("${token}")
    private String token;

    private static DbxRequestConfig config;
    private static DbxClientV2 client;

    private static ArrayList<String> images;

    public void config() {
        config = DbxRequestConfig.newBuilder("DearlyBelovedTest").build();
        client = new DbxClientV2(config, token);
        images = new ArrayList<>();
    }

    public boolean isImageFile(String filename) {
        return filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".png") || filename.toLowerCase().endsWith(".gif") || filename.toLowerCase().endsWith(".jpeg");
    }

    public String downloadAndConvertToBase64(DbxClientV2 client, FileMetadata fileMetadata) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             InputStream in = client.files().download(fileMetadata.getPathLower()).getInputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getImages(String folderPath) {

        config();

        try {
            ListFolderResult results = client.files().listFolder(folderPath);

            while (true) {
                for (Metadata metadata : results.getEntries()) {
                    if (metadata instanceof FileMetadata) {
                        FileMetadata fileMetadata = (FileMetadata) metadata;
                        String filename = fileMetadata.getName();
                        if (isImageFile(filename)) {
                            String base64Image = downloadAndConvertToBase64(client, fileMetadata);
                            images.add(base64Image);
                        }
                    }
                }

                if (!results.getHasMore()) {
                    break;
                }

                results = client.files().listFolderContinue(results.getCursor());
            }
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }

        return images;
    }

}
