package com.swapnil.signicat.dto.request;

import lombok.Data;

@Data
public class AddUserToGroupReqDTO {
    private String username;

    private Long groupId;
}
