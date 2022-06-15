package com.swapnil.signicat.service.impl;

import com.swapnil.signicat.dto.request.AddUserToGroupReqDTO;
import com.swapnil.signicat.dto.UserGroupDTO;
import com.swapnil.signicat.dto.request.GroupRequestDTO;
import com.swapnil.signicat.dto.response.UserGroupResponseDTO;
import com.swapnil.signicat.exception.UserGroupAlreadyExistException;
import com.swapnil.signicat.exception.UserGroupNotFoundException;
import com.swapnil.signicat.exception.UserNotAuthorizedForGroupException;
import com.swapnil.signicat.exception.UserNotFoundException;
import com.swapnil.signicat.model.Subject;
import com.swapnil.signicat.model.UserGroup;
import com.swapnil.signicat.repository.GroupRepository;
import com.swapnil.signicat.repository.UserRepository;
import com.swapnil.signicat.service.GroupService;
import com.swapnil.signicat.utils.SecurityContextUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    @Override
    public List<UserGroupDTO> getGroups() {
        Subject user = getUserByUsername(SecurityContextUtils.getLoggedInUsername());
        return user.getUserGroups().stream().map(UserGroupDTO::from).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserGroupResponseDTO createGroup(GroupRequestDTO groupRequestDTO) {
        Assert.hasText(groupRequestDTO.getGroupName(), "Cannot create group with an empty name");

        throwExceptionIfGroupAlreadyExists(groupRequestDTO.getGroupName());
        Subject user = getUserByUsername(SecurityContextUtils.getLoggedInUsername());

        UserGroup group = new UserGroup();
        group.setName(groupRequestDTO.getGroupName());
        group.getUsers().add(user);
        return UserGroupResponseDTO.from(groupRepository.save(group));
    }

    @Override
    @Transactional
    public UserGroupResponseDTO updateGroupById(Long groupId, GroupRequestDTO groupRequestDTO) {
        UserGroup group = getUserGroupById(groupId);
        Subject loggedInUser = getUserByUsername(SecurityContextUtils.getLoggedInUsername());

        validateUserRightsOnGroup(groupId, loggedInUser);

        UserGroupDTO.transform(group, groupRequestDTO);
        groupRepository.save(group);

        return UserGroupResponseDTO.from(group);
    }

    @Override
    @Transactional
    public UserGroupResponseDTO addUserToGroup(AddUserToGroupReqDTO addUserToGroupReqDTO) {
        Subject user = getUserByUsername(addUserToGroupReqDTO.getUsername());
        UserGroup group = getUserGroupById(addUserToGroupReqDTO.getGroupId());
        Subject loggedInUser = getUserByUsername(SecurityContextUtils.getLoggedInUsername());

        validateUserRightsOnGroup(group.getId(), loggedInUser);

        if (group.getUsers().contains(user)) {
            log.info("User already added to the group");
            return UserGroupResponseDTO.from(group);
        }


        group.getUsers().add(user);
        groupRepository.save(group);
        return UserGroupResponseDTO.from(group);
    }

    @Override
    @Transactional
    public void deleteByGroupId(Long groupId) {
        UserGroup group = getUserGroupById(groupId);
        Subject loggedInUser = getUserByUsername(SecurityContextUtils.getLoggedInUsername());
        validateUserRightsOnGroup(groupId, loggedInUser);

        groupRepository.delete(group);
    }

    private void validateUserRightsOnGroup(Long groupId, Subject user) {
        user
            .getUserGroups()
            .stream().map(UserGroup::getId)
            .filter(id -> groupId.equals(id))
            .findAny().orElseThrow(() -> new UserNotAuthorizedForGroupException(user.getUsername(), groupId));
    }

    private void throwExceptionIfGroupAlreadyExists(String groupName) {
        Optional<UserGroup> userGroup = groupRepository.findByName(groupName);

        if (userGroup.isEmpty()) {
            return;
        }

        throw new UserGroupAlreadyExistException(userGroup.get().getName());
    }

    public Subject getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    public UserGroup getUserGroupById(Long id) {
        return groupRepository.findById(id).orElseThrow(() -> new UserGroupNotFoundException(id));
    }
}
