package com.jordan.skyshoplink.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class ImageStorageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif");
    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024; // 5 MB

    private final Path storageLocation;

    public ImageStorageService(@Value("${app.upload.dir}") String uploadDir) {
        this.storageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.storageLocation);
        } catch (IOException e) {
            throw new IllegalStateException("Could not initialize storage directory.", e);
        }
    }

    public String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File exceeds the 5 MB limit.");
        }

        String extension = resolveExtension(file.getOriginalFilename());

        // Content-Type is supplied by the client: verify the actual bytes instead.
        byte[] bytes = file.getBytes();
        if (!isRealImage(bytes)) {
            throw new IllegalArgumentException("Only image files (JPEG/PNG/GIF) are allowed.");
        }

        String uniqueFilename = UUID.randomUUID() + "." + extension;
        Path targetPath = resolveSafely(uniqueFilename);

        Files.write(targetPath, bytes,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);

        return uniqueFilename;
    }

    public Resource loadImage(String filename) {
        try {
            Path filePath = resolveSafely(filename);
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new IllegalArgumentException("Image not found: " + filename);
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid image path: " + filename, e);
        }
    }

    public void deleteImage(String filename) throws IOException {
        Files.deleteIfExists(resolveSafely(filename));
    }

    /** Guards against path traversal (../../etc/passwd) in user-supplied names. */
    private Path resolveSafely(String filename) {
        String cleaned = StringUtils.cleanPath(filename == null ? "" : filename);
        Path target = this.storageLocation.resolve(cleaned).normalize();
        if (!target.startsWith(this.storageLocation)) {
            throw new IllegalArgumentException("Invalid filename: " + filename);
        }
        return target;
    }

    private String resolveExtension(String originalFilename) {
        String cleaned = StringUtils.cleanPath(
                originalFilename == null ? "" : originalFilename);
        String ext = StringUtils.getFilenameExtension(cleaned);
        if (ext == null) {
            throw new IllegalArgumentException("Missing file extension.");
        }
        ext = ext.toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new IllegalArgumentException("Unsupported extension: " + ext);
        }
        return ext;
    }

    private boolean isRealImage(byte[] bytes) throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
            BufferedImage image = ImageIO.read(in);
            return image != null;
        }
    }
}

