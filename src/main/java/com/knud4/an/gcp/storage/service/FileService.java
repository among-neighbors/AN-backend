package com.knud4.an.gcp.storage.service;


import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;


@Service
@RequiredArgsConstructor
public class FileService {
    private final Storage storage;

    @Value("${gcp-bucket-name}")
    private String BUCKET_NAME;
    @Value("${gcp-directory-name}")
    private String FILE_DIRECTORY_NAME;
    @Value("${gcp-storage-url}")
    private String GCP_STORAGE_URL;

    /**
     *
     * @param name 파일 이름
     * @param data 파일 데이터
     */
    public String uploadPNG(String name, byte[] data)  {
        BlobInfo info = BlobInfo.newBuilder(BUCKET_NAME, FILE_DIRECTORY_NAME+"/"+name)
                .setContentType(MimeTypeUtils.IMAGE_PNG_VALUE)
                .build();

        BlobInfo blobInfo = storage.create(info, data);

        return generateGcpUrl(blobInfo.getName());
    }

    private String generateGcpUrl(String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(GCP_STORAGE_URL)
                .append(BUCKET_NAME)
                .append("/")
                .append(fileName);

        return sb.toString();
    }
}
