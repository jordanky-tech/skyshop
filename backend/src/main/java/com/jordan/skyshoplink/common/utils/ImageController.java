package com.jordan.skyshoplink.common.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uploads")
@RequiredArgsConstructor
public class ImageController {
    private final ImageStorageService imageStorageService;
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getImage(
            @PathVariable String filename
    ) {
        Resource resource =
                imageStorageService.loadImage(filename);
        MediaType mediaType = MediaTypeFactory
                .getMediaType(resource.getFilename())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }
}
