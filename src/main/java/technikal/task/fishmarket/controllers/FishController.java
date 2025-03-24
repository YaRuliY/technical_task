package technikal.task.fishmarket.controllers;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import technikal.task.fishmarket.models.dto.FishDto;
import technikal.task.fishmarket.models.entity.Fish;
import technikal.task.fishmarket.repo.FishRepository;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.*;

@Log4j2
@Controller
@RequestMapping("/fish")
@RequiredArgsConstructor
public class FishController {
    private final FishRepository repo;

    @GetMapping({"", "/"})
    public String showFishList(Model model, Principal principal) {
        List<Fish> fishlist = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("fishlist", fishlist);
        log.warn("Fish list loaded by {}", principal.getName());
        return "index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        FishDto fishDto = new FishDto();
        model.addAttribute("fishDto", fishDto);
        return "createFish";
    }

    @GetMapping("/delete")
    public String deleteFish(@RequestParam int id) {

        try {
            Optional<Fish> opt = repo.findById(id);
            if (opt.isPresent()) {
                Fish fish = opt.get();
                fish.getImages()
                    .stream()
                    .map(imageFileName -> "public/images/" + imageFileName)
                    .map(Paths::get)
                    .forEach(imagePath -> {
                        try {
                            Files.delete(imagePath);
                        } catch (IOException e) {
                            throw new RuntimeException("Can't delete file: " + imagePath);
                        }
                    })
                ;
                repo.delete(fish);
            } else {
                throw new EntityNotFoundException(String.format("Entity with id %s not found", id));
            }
        } catch (Exception ex) {
            log.error("deleteFish: {}", ex.getMessage());
        }

        return "redirect:/fish";
    }

    @PostMapping("/create")
    public String addFish(@Valid @ModelAttribute FishDto dto, BindingResult result) {
        if (dto.getImageFiles().isEmpty()) {
            result.addError(new FieldError("fishDto", "imageFile", "at least one photo is required!"));
        }

        if (result.hasErrors()) {
            return "createFish";
        }

        List<MultipartFile> multiFiles = dto.getImageFiles();
        Date creation = new Date();
        Set<String> names = new HashSet<>();

        try {
            String root = "public/images/";
            Path rootPath = Paths.get(root);
            Files.createDirectories(rootPath);

            for (MultipartFile image : multiFiles) {
                String name = creation.getTime() + "_" + image.getOriginalFilename();
                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(root + name), StandardCopyOption.REPLACE_EXISTING);
                }
                names.add(name);
            }
        } catch (Exception ex) {
            log.error("addFish: {}", ex.getMessage());
        }

        Fish fish = new Fish();
        fish.setCatchDate(creation);
        fish.setImages(names);
        fish.setName(dto.getName());
        fish.setPrice(dto.getPrice());
        repo.save(fish);

        return "redirect:/fish";
    }

}
