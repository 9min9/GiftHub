package com.gifthub.user.repository;

import com.gifthub.user.entity.NaverUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaverRepository  extends JpaRepository <NaverUser , Long> {
}
