package com.swapnil.signicat.util;

import com.swapnil.signicat.dto.request.GroupRequestDTO;
import com.swapnil.signicat.model.Subject;
import com.swapnil.signicat.model.UserGroup;

import java.util.Collections;

public class Constants {
    public static final String REGISTER_API_URL = "http://127.0.0.1:%d/api/auth/register";

    public static final String GET_GROUP_API_URL = "http://127.0.0.1:%d/api/group";

    public static final String LOGIN_API_URL = "http://127.0.0.1:%d/api/auth/login";

    public static final String DEMO_USER_NAME = "swap_demo_user";

    public static final String DEMO_USER_NAME_MAX = "max_demo_user";

    public static final String DEMO_PASSWORD = "Login@123";

    public static final String DEMO_GROUP_NAME = "demo_group_name";

    public static final String DEMO_GROUP_NAME_EDITED = "demo_group_name_edited";

    public static final String DEMO_ENCODED_PASSWORD = "$2a$10$a7YQr3C/gn0Sisrh0woYeu7WVnU5u05uxtF5LXg8xkjc9sQQRbYKy";

    public static final String DEMO_REFRESH_TOKEN = "b53867b1-10f0-4479-9129-77a7f9e2611d";

    public static final String DEMO_JWT_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOjEsImdyb3VwcyI6W10sInVzZXJuYW1lIjoic3dhcCIsImlhdCI6MTY1NTIzNDU5MSwiZXhwIjoxNjU1MjM2MzkxfQ.vsxJN6ovacyHluIFSs4ggEQXmLzXoRs_PIcBQg9N3FFKltVNIdz8uo0LunSlPznuO4gb8yq4oSQ2XnQEOfbu2A";

    public static Subject getDemoUser() {
        Subject user = new Subject();
        user.setId(1L);
        user.setUsername(DEMO_USER_NAME);
        user.setPassword(DEMO_PASSWORD);
        return user;
    }

    public static Subject getDemoUserMax() {
        Subject user = new Subject();
        user.setId(2L);
        user.setUsername(DEMO_USER_NAME_MAX);
        user.setPassword(DEMO_PASSWORD);
        return user;
    }

    public static Subject getDemoUserWithGroups() {
        Subject user = getDemoUser();
        user.setUserGroups(Collections.singleton(getDemoUserGroup()));
        return user;
    }

    public static UserGroup getDemoUserGroup() {
        UserGroup demoGroup = new UserGroup();
        demoGroup.setId(1L);
        demoGroup.setName(DEMO_GROUP_NAME);
        return demoGroup;
    }

    public static GroupRequestDTO getDemoGroupRequestDTO() {
        GroupRequestDTO groupRequestDTO = new GroupRequestDTO();
        groupRequestDTO.setGroupName(Constants.DEMO_GROUP_NAME);
        return groupRequestDTO;
    }
}
