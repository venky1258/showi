package com.mypack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/")
    public String index(Model model) {
        List<Image> images = imageService.getAllImages();
        model.addAttribute("images", images);
        return "index";
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("image") MultipartFile file) throws Exception {
        imageService.saveImage(file);
        return "redirect:/";
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<ByteArrayResource> serveImage(@PathVariable Long id) throws Exception {
        Image image = imageService.getImage(id).orElseThrow();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                .body(new ByteArrayResource(image.getData()));
    }

    @PostMapping("/images/{id}/delete")
    public String deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return "redirect:/";
    }

    @PostMapping("/images/{id}/update")
    public String updateImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) throws Exception {
        imageService.updateImage(id, file);
        return "redirect:/";
    }
}

