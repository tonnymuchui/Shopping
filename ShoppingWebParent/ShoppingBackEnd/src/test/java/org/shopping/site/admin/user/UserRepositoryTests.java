package org.shopping.site.admin.user;

import org.junit.jupiter.api.Test;
import org.shopping.entity.Role;
import org.shopping.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void CreateUserTest(){
        Role role = testEntityManager.find(Role.class, 1);
        User user = new User("test@gmail", "test", "testFirst", "testLast");
        user.addRole(role);
        User saveUser = userRepository.save(user);
        assertThat(saveUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRoles() {
        User test = new User("test@gmail.com", "test123", "test", "tests");
        test.setEnabled(true);
        Role editor = new Role(3);
        Role assistant = new Role(5);

        test.addRole(editor);
        test.addRole(assistant);

        User savedUser = userRepository.save(test);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }
    @Test
    public void listAllUsersTest() {
        Iterable<User> listUsers = userRepository.findAll();
        listUsers.forEach(System.out::println);
    }
    @Test
    public void GetUserByIdTest() {
        User user = userRepository.findById(1).get();
        System.out.println(user);
        assertThat(user).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User user = userRepository.findById(1).get();
        user.setEnabled(true);
        user.setEmail("test22@gmail.com");

        userRepository.save(user);
    }
    @Test
    public void testUpdateUserRoles() {
        User user = userRepository.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSalesperson = new Role(2);

        user.getRoles().remove(roleEditor);
        user.addRole(roleSalesperson);

        userRepository.save(user);
    }

    @Test
    public void testDeleteUser() {
        Integer userId = 2;
        userRepository.deleteById(userId);
    }

    @Test
    public void getUserByEmailTest(){
        String email = "test22@gmail.com";
        User user = userRepository.getUserByEmail(email);
        assertThat(user).isNotNull();
    }
    @Test
    public void firstPageTest(){
        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(pageable);

        List<User> listUsers = page.getContent();
        listUsers.forEach(System.out::println);
        assertThat(listUsers.size()).isEqualTo(pageSize);
    }
    @Test
    public void testSearchUsers() {
        String keyword = "test";

        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(keyword, pageable);

        List<User> listUsers = page.getContent();

        listUsers.forEach(System.out::println);

        assertThat(listUsers.size()).isGreaterThan(0);
    }
}
