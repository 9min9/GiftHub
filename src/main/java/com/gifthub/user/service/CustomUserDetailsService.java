package com.gifthub.user.service;


import com.gifthub.user.entity.User;
import com.gifthub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User findUser = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디 입니다."));
		return findUser;
	}

	public UserDetails loadUserByKakaoAccountId(String kakaoAccountId) {
		return userRepository.findByKakaoAccountId(kakaoAccountId).orElse(null);
	}


}