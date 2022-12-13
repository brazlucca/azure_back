package com.project.fiapnotes.controller;

import com.project.fiapnotes.dtos.FiapNotesDto;
import com.project.fiapnotes.models.FiapNotesModel;
import com.project.fiapnotes.service.FiapNotesService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fiap-notes")
@CrossOrigin({"http://localhost:3000", "https://viniroxo.github.io/fiap-note/", "https://viniroxo.github.io"})
public class FiapNotesController {

    final FiapNotesService service;

    public FiapNotesController(FiapNotesService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Object> saveNote(@RequestBody FiapNotesDto fiapNotesDto) {
        var fiapNotesModel = new FiapNotesModel();
        BeanUtils.copyProperties(fiapNotesDto, fiapNotesModel);
        fiapNotesModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveNote(fiapNotesModel));
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
