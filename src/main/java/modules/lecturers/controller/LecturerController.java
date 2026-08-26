package modules.lecturers.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import modules.lecturers.dto.*;
import modules.lecturers.service.LecturerService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lecturers")
@RequiredArgsConstructor
public class LecturerController {
    private final LecturerService lecturerService;

    @PostMapping("/register")
    public ResponseEntity<LecturerResponse> register(@Valid @RequestBody RegisterLecturerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lecturerService.register(request));
    }
}
