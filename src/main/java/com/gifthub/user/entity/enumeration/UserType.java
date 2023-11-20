package com.gifthub.user.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum UserType {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER"), MANAGER("ROLE_MANAGER");

    private String role;
 }
