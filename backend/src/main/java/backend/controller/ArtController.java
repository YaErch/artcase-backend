package backend.controller;

import backend.model.Art;
import backend.repository.ArtRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/arts")
public class ArtController {

    private final ArtRepository artRepository;

    public ArtController(ArtRepository artRepository) {
        this.artRepository = artRepository;
    }

    // ✅ получить все арты
    @GetMapping
    public List<Art> getAllArts() {
        if (Math.random() < 0.3) {
            throw new RuntimeException("Random error");
        }
        return artRepository.findAll();
    }

    // ✅ создать арт (БЕЗ картинки)
    @PostMapping
    public Art createArt(@RequestBody Art art) {
        art.setImageUrl(null); // важно
        return artRepository.save(art);
    }

    // ✅ получить по id
    @GetMapping("/{id}")
    public Art getArtById(@PathVariable Long id) {
        return artRepository.findById(id).orElse(null);
    }

    // ✅ лайк
    @PostMapping("/{id}/like")
    public Art likeArt(@PathVariable Long id) {
        Art art = artRepository.findById(id).orElse(null);

        if (art == null) return null;

        art.setLikes(art.getLikes() + 1);

        return artRepository.save(art);
    }

    // ✅ загрузка картинки к арту
    @PostMapping("/{id}/image")
    public Art uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        Art art = artRepository.findById(id).orElse(null);

        if (art == null) {
            throw new RuntimeException("Art not found");
        }

        String uploadDir = "C:/artcase-images/";

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        File dest = new File(uploadDir + fileName);

        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(file.getBytes());
        }

        // ✅ сохраняем путь
        art.setImageUrl("/images/" + fileName);

        return artRepository.save(art);
    }

    // ✅ поиск
    @GetMapping("/search")
    public List<Art> searchByTag(@RequestParam String tag) {
        return artRepository.findByTagsContaining(tag);
    }

    @DeleteMapping("/{id}")
    public void deleteArt(@PathVariable Long id) {
        if (!artRepository.existsById(id)) {
            throw new RuntimeException("Art not found");
        }
        artRepository.deleteById(id);
    }
}