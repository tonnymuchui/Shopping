package org.shopping.site.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class PasswordEncoderTest {
    @Test
    public void encoderPasswordTest(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "test123";
        String encodedPassword = passwordEncoder.encode(password);
        boolean passwordMatches = passwordEncoder.matches(password, encodedPassword);
        assertThat(passwordMatches).isTrue();
    }
}
