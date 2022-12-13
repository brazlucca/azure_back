package com.project.fiapnotes.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FiapNotesDto {

    private String text;

    private Boolean urgent;

}
