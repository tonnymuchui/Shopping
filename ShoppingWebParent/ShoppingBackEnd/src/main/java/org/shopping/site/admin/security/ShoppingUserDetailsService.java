package org.shopping.site.admin.security;

import org.shopping.entity.User;
import org.shopping.site.admin.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ShoppingUserDetailsService implements UserDetailsService {


    private UserRepository userRepo;
    @Autowired
    public ShoppingUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    public ShoppingUserDetailsService(){

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.getUserByEmail(email);
        if (user != null) {
            return new ShoppingUserDetails(user);
        }

        throw new UsernameNotFoundException("Could not find user with email: " + email);
    }

}