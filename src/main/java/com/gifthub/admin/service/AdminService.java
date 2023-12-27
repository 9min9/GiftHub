package com.gifthub.admin.service;

import com.gifthub.gifticon.enumeration.RegistrationFailureReason;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AdminService {

    public List<RegistrationFailureReason> getAllRegistrationFailureReason() {
        return Arrays.stream(RegistrationFailureReason.values()).toList();
    }

}
