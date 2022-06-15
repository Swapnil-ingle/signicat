package com.swapnil.signicat.controller;

import com.swapnil.signicat.dto.request.AddUserToGroupReqDTO;
import com.swapnil.signicat.dto.UserGroupDTO;
import com.swapnil.signicat.dto.request.GroupRequestDTO;
import com.swapnil.signicat.dto.response.UserGroupResponseDTO;
import com.swapnil.signicat.service.GroupService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group")
@AllArgsConstructor
@Slf4j
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public List<UserGroupDTO> getGroups() {
        return groupService.getGroups();
    }

    @PostMapping("/addUser")
    public UserGroupResponseDTO addUserToGroup(@RequestBody AddUserToGroupReqDTO addUserToGroupReqDTO) {
        return groupService.addUserToGroup(addUserToGroupReqDTO);
    }

    @PostMapping
    public UserGroupResponseDTO createGroup(@RequestBody GroupRequestDTO groupRequestDTO) {
        return groupService.createGroup(groupRequestDTO);
    }

    @PutMapping("/{groupId}")
    public UserGroupResponseDTO updateGroupById(@PathVariable Long groupId, @RequestBody GroupRequestDTO groupRequestDTO) {
        return groupService.updateGroupById(groupId, groupRequestDTO);
    }

    @DeleteMapping("/{groupId}")
    public void deleteByGroupId(@PathVariable Long groupId) {
        groupService.deleteByGroupId(groupId);
    }
}
