package com.project.fiapnotes.repository;

import com.project.fiapnotes.models.FiapNotesModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FiapNotesRepository extends JpaRepository<FiapNotesModel, Long> {
}
