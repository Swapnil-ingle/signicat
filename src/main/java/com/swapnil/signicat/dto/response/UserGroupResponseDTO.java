package com.swapnil.signicat.dto.response;

import com.swapnil.signicat.dto.SubjectDTO;
import com.swapnil.signicat.model.Subject;
import com.swapnil.signicat.model.UserGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGroupResponseDTO {
    private Long id;

    private String name;

    private List<SubjectDTO> users;

    public static UserGroupResponseDTO from(UserGroup group) {
        return UserGroupResponseDTO.builder().id(group.getId())
                .name(group.getName())
                .users(getUserDTOs(group.getUsers()))
                .build();
    }

    private static List<SubjectDTO> getUserDTOs(Set<Subject> users) {
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }

        return users.stream().map(SubjectDTO::from).collect(Collectors.toList());
    }
}
