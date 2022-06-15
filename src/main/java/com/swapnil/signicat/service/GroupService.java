package com.swapnil.signicat.service;

import com.swapnil.signicat.dto.request.AddUserToGroupReqDTO;
import com.swapnil.signicat.dto.UserGroupDTO;
import com.swapnil.signicat.dto.request.GroupRequestDTO;
import com.swapnil.signicat.dto.response.UserGroupResponseDTO;

import java.util.List;

public interface GroupService {
    List<UserGroupDTO> getGroups();

    UserGroupResponseDTO createGroup(GroupRequestDTO groupRequestDTO);

    UserGroupResponseDTO updateGroupById(Long groupId, GroupRequestDTO groupRequestDTO);

    void deleteByGroupId(Long groupId);

    UserGroupResponseDTO addUserToGroup(AddUserToGroupReqDTO groupRequestDTO);
}
