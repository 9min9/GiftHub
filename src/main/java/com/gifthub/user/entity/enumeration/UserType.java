package com.gifthub.user.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum UserType {
    ADMIN("ADMIN"), USER("USER"), MANAGER("MANAGER");

    private String role;
 }
