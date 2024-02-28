package org.shopping.site.admin.user;

import org.shopping.entity.Role;
import org.shopping.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> listAllUsers() {
        return (List<User>) userRepository.findAll();
    }
    public List<Role> listAllRoles(){
        return (List<Role>) roleRepository.findAll();
    }
    public void saveUser(User user){
        encodePassword(user);
        userRepository.save(user);
    }

    private void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }
    public boolean isEmailUnique(String email){
        User userByEmail = userRepository.getUserByEmail(email);
        return userByEmail == null;
    }
}
