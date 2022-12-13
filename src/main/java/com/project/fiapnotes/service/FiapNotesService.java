package com.project.fiapnotes.service;

import com.project.fiapnotes.models.FiapNotesModel;
import com.project.fiapnotes.repository.FiapNotesRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class FiapNotesService {

    final FiapNotesRepository fiapNotesRepository;

    public FiapNotesService(FiapNotesRepository fiapNotesRepository) {
        this.fiapNotesRepository = fiapNotesRepository;
    }

    @Transactional
    public FiapNotesModel saveNote(FiapNotesModel fiapNotesModel) {
        return fiapNotesRepository.save(fiapNotesModel);
    }

    public List<FiapNotesModel> getNotesByText(String text) {
        return null;
    }

    public Optional<FiapNotesModel> findById(Long id) {
        return fiapNotesRepository.findById(id);
    }

    @Transactional
    public void delete(FiapNotesModel fiapNotesModel) {
        fiapNotesRepository.delete(fiapNotesModel);
    }

    public List<FiapNotesModel> getNotes() {
        return fiapNotesRepository.findAll();
    }
}
