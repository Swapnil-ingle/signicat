package com.swapnil.signicat.dto;

import com.swapnil.signicat.dto.request.GroupRequestDTO;
import com.swapnil.signicat.model.UserGroup;
import lombok.Data;

@Data
public class UserGroupDTO {
    private Long id;

    private String name;

    public static UserGroupDTO from(UserGroup userGroup) {
        UserGroupDTO userGroupDTO = new UserGroupDTO();

        userGroupDTO.setId(userGroup.getId());
        userGroupDTO.setName(userGroup.getName());

        return userGroupDTO;
    }

    public static void transform(UserGroup group, GroupRequestDTO groupRequestDTO) {
        group.setName(groupRequestDTO.getGroupName());
    }
}
