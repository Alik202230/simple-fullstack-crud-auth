package am.itspace.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {

  @Value("${upload.image.path}") // Configure this in application.properties
  private String imageUploadDirectory;

  @GetMapping("/{fileName:.+}") // Regex to allow file extensions
  public ResponseEntity<Resource> serveImage(@PathVariable String fileName) {
    try {
      Path filePath = Paths.get(imageUploadDirectory).resolve(fileName).normalize();
       Resource resource = new UrlResource(filePath.toUri());

      if (resource.exists() && resource.isReadable()) {
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG) // Adjust based on your image types
            .body(resource);
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

}
