package com.project.fiapnotes.controller;

import com.project.fiapnotes.dtos.FiapNotesDto;
import com.project.fiapnotes.models.FiapNotesModel;
import com.project.fiapnotes.service.FiapNotesService;
import com.project.fiapnotes.service.FileService;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fiap-notes")
@CrossOrigin({"http://localhost:3000", "https://viniroxo.github.io/fiap-note/", "https://viniroxo.github.io"})
public class FiapNotesController {

    final FiapNotesService service;

    public FiapNotesController(FiapNotesService service, FileService fileService) {
        this.service = service;
        this.fileService = fileService;
    }

    private final FileService fileService;


//    @PostMapping()
//    public ResponseEntity<Object> saveNote(@RequestBody FiapNotesDto fiapNotesDto) {
//        var fiapNotesModel = new FiapNotesModel();
//        BeanUtils.copyProperties(fiapNotesDto, fiapNotesModel);
//        fiapNotesModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
//        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveNote(fiapNotesModel));
//    }


    @PostMapping( produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<Object> saveNote(@RequestBody MultipartFile file,@RequestBody FiapNotesDto fiapNotesDto) {

        var fiapNotesModel = new FiapNotesModel();
        BeanUtils.copyProperties(fiapNotesDto, fiapNotesModel);
        fiapNotesModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        var entity = service.saveNote(fiapNotesModel);
        try {
            if (fileService.uploadAndDownloadFile(file, "imagem", "img" + entity.getId().toString())) {
                final ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(fileService
                        .getFileStorageLocation() + "/" + file.getOriginalFilename())));
                return ResponseEntity.status(HttpStatus.OK).contentLength(resource.contentLength()).body(resource);
            }
            //return ResponseEntity.ok("Error while processing file");
        } catch (Exception e) {
            // return ResponseEntity.ok("Error while processing file");
        }
        return ResponseEntity.ok("Error while processing file");
    }

    @GetMapping
    public ResponseEntity<List<FiapNotesModel>> getNotes() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getNotes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getNoteById(@PathVariable Long id) {
        Optional<FiapNotesModel> notesModelOptional = service.findById(id);
        if (!notesModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(notesModelOptional.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateNote(@PathVariable Long id,
                                     @RequestBody FiapNotesDto fiapNotesDto) {
        Optional<FiapNotesModel> notesModelOptional = service.findById(id);
        if (!notesModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found");
        }

        var fiapNotesModel = notesModelOptional.get();
        fiapNotesModel.setUrgent(fiapNotesDto.getUrgent());
        fiapNotesModel.setText(fiapNotesDto.getText());

        return ResponseEntity.status(HttpStatus.OK).body(service.saveNote(fiapNotesModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteNote(@PathVariable Long id) {
        Optional<FiapNotesModel> notesModelOptional = service.findById(id);
        if (!notesModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found");
        }

        service.delete(notesModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Note deleted successfully");
    }
}
