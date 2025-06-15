package am.itspace.backend.utils;

import am.itspace.backend.entity.Image;
import am.itspace.backend.entity.Product;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class ImageUtil {

  public static List<Image> generateImages(List<MultipartFile> multipartFiles, String uploadPath, Product product) throws IOException {
    List<Image> imageUrls = new ArrayList<>();

    for (MultipartFile image : multipartFiles) {

      String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
      Path filePath = Paths.get(uploadPath + filename);
      Files.createDirectories(filePath.getParent());

      try (InputStream inputStream = image.getInputStream();
           BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
           OutputStream outputStream = Files.newOutputStream(filePath);
           BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {

        byte[] buffer = new byte[8192];
        int readByte;

        while ((readByte = bufferedInputStream.read(buffer)) != -1) {
          bufferedOutputStream.write(buffer, 0, readByte);
        }

        Image img = Image.builder()
            .fileName(filename)
            .product(product)
            .build();

        imageUrls.add(img);

      } catch (IOException e) {
        throw new FileNotFoundException("File not found");
      }
    }
    return imageUrls;
  }

}
