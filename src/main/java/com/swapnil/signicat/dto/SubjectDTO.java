package com.swapnil.signicat.dto;

import com.swapnil.signicat.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {
    private Long id;

    private String username;

    public static SubjectDTO from(Subject subject) {
        SubjectDTO subjectDTO = new SubjectDTO();

        subjectDTO.setId(subject.getId());
        subjectDTO.setUsername(subject.getUsername());

        return subjectDTO;
    }
}
