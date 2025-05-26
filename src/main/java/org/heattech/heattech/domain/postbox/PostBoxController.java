package org.heattech.heattech.domain.postbox;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/postboxes")
@RequiredArgsConstructor
public class PostBoxController {
    private final PostBoxRepository postBoxRepository;

    @GetMapping
    public List<PostBox> getAllPostBoxes() {
        return postBoxRepository.findAll();
    }
}
