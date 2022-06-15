package com.swapnil.signicat;

import com.swapnil.signicat.dto.UserGroupDTO;
import com.swapnil.signicat.dto.request.AddUserToGroupReqDTO;
import com.swapnil.signicat.dto.request.GroupRequestDTO;
import com.swapnil.signicat.dto.response.UserGroupResponseDTO;
import com.swapnil.signicat.exception.UserNotAuthorizedForGroupException;
import com.swapnil.signicat.model.UserGroup;
import com.swapnil.signicat.repository.GroupRepository;
import com.swapnil.signicat.repository.UserRepository;
import com.swapnil.signicat.service.impl.GroupServiceImpl;
import com.swapnil.signicat.utils.SecurityContextUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.swapnil.signicat.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserGroupTests {
    @InjectMocks
    private GroupServiceImpl groupService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @BeforeAll
    public static void registerStaticMocking() {
        Mockito.mockStatic(SecurityContextUtils.class).when(SecurityContextUtils::getLoggedInUsername).thenReturn(DEMO_USER_NAME);
    }

    @BeforeEach
    public void init() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(getDemoUserWithGroups()));
        when(groupRepository.save(any(UserGroup.class))).thenReturn(getDemoUserGroup());
    }

    @Test
    public void getGroupsListTest() {
        List<UserGroupDTO> groupDTOs = groupService.getGroups();
        assertTrue(!groupDTOs.isEmpty());
    }

    @Test
    public void createGroupTest() {
        when(groupRepository.findByName(anyString())).thenReturn(Optional.empty());

        UserGroupResponseDTO userGroupResponseDTO = groupService.createGroup(getDemoGroupRequestDTO());
        assertEquals(userGroupResponseDTO.getName(), DEMO_GROUP_NAME);
    }

    @Test
    public void updateGroupTest() {
        when(groupRepository.findById(anyLong())).thenReturn(Optional.of(getDemoUserGroup()));

        Long groupId = 1L;
        GroupRequestDTO groupRequestDTO = new GroupRequestDTO();
        groupRequestDTO.setGroupName(DEMO_GROUP_NAME_EDITED);

        UserGroupResponseDTO userGroupResponseDTO = groupService.updateGroupById(groupId, groupRequestDTO);
        assertEquals(userGroupResponseDTO.getName(), DEMO_GROUP_NAME_EDITED);
    }

    @Test
    public void addUserToGroupTest() {
        when(groupRepository.findById(anyLong())).thenReturn(Optional.of(getDemoUserGroup()));
        when(userRepository.findByUsername(DEMO_USER_NAME)).thenReturn(Optional.of(getDemoUserWithGroups()));
        when(userRepository.findByUsername(DEMO_USER_NAME_MAX)).thenReturn(Optional.of(getDemoUserMax()));

        AddUserToGroupReqDTO addUserToGroupReqDTO = new AddUserToGroupReqDTO();
        addUserToGroupReqDTO.setGroupId(1L);
        addUserToGroupReqDTO.setUsername(DEMO_USER_NAME_MAX);

        UserGroupResponseDTO userGroupResponseDTO = groupService.addUserToGroup(addUserToGroupReqDTO);

        assertTrue(userGroupResponseDTO.getUsers()
                .stream().filter(u -> u.getUsername().equalsIgnoreCase(DEMO_USER_NAME_MAX))
                .findAny().isPresent());
    }

    @Test
    public void deleteGroupTest() {
        when(groupRepository.findById(anyLong())).thenReturn(Optional.of(getDemoUserGroup()));
        when(userRepository.findByUsername(DEMO_USER_NAME)).thenReturn(Optional.of(getDemoUserWithGroups()));

        assertDoesNotThrow(() -> groupService.deleteByGroupId(1L));
    }

    @Test
    public void deleteGroup_WhenUserIsNotAPart_ShouldThrowException() {
        when(groupRepository.findById(anyLong())).thenReturn(Optional.of(getDemoUserGroup()));
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(getDemoUserMax()));

        assertThrowsExactly(UserNotAuthorizedForGroupException.class, () -> groupService.deleteByGroupId(1L));
    }
}
