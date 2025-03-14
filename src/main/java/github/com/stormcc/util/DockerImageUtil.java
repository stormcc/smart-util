package github.com.stormcc.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * Create By: Jimmy Song
 * Create At: 2025-03-14 09:15
 */
@Slf4j
public final class DockerImageUtil implements Serializable {

    private static final String KEY_NAME = "Config";
    private static final String SEPARATOR = "/";
    private static final String IMAGE_ID_PREFIX = "sha256:";

    private DockerImageUtil(){}
    
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 从镜像文件中读取镜像Id，镜像文件必须为.tgz格式
     * @param tgzFileFullPath 镜像文件的完整路径
     * @return 返回镜像文件中的镜像id值，格式为sha256:abcdfasdfsdfsdf...afsd
     * @throws IOException
     */
    public static String getImageId(String tgzFileFullPath) throws IOException {
        String imageId;
        try (FileInputStream fis = new FileInputStream(tgzFileFullPath);
             BufferedInputStream bis = new BufferedInputStream(fis);
             GzipCompressorInputStream gzis = new GzipCompressorInputStream(bis);
             TarArchiveInputStream tarInput = new TarArchiveInputStream(gzis)) {

            TarArchiveEntry entry;
            // 2. 遍历 tar 包中的文件
            while ((entry = tarInput.getNextTarEntry()) != null) {
                String entryName = entry.getName();
                log.info(entryName);
                // 3. 查找 manifest.json
                if (entryName.equals("manifest.json")) {
                    String manifestJson = readTarEntryContent(tarInput);
                    // 解析 JSON 获取配置文件名
                    JsonNode root = mapper.readTree(manifestJson);
                    imageId = root.get(0).get(KEY_NAME).asText();
                    if (!Strings.isNullOrEmpty(imageId)) {
                        log.info("Config value is {}", imageId);
                        return IMAGE_ID_PREFIX+imageId.split(SEPARATOR)[2];
                    }
                }
            }
        }
        log.error("return image id is null, tgzFileFullPath:{}", tgzFileFullPath);
        return null;
    }

    /**
     * 读取 tar 条目内容为字符串（用于 manifest.json）
     */
    private static String readTarEntryContent(TarArchiveInputStream tarInput) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int bytesRead;
        while ((bytesRead = tarInput.read(data)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        return buffer.toString(StandardCharsets.UTF_8.name());
    }
}
