package hu.bme.compsec.sudoku.authserver.data;

import hu.bme.compsec.sudoku.common.security.UserRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    //TODO: make this work
    private PasswordEncoder passwordEncoder;

    Long adminId = 0L;

    @BeforeAll
    @PostConstruct
    public void initRepository() {
        adminId = userRepository.saveAndFlush(
                User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .email("admin@sudokucaff.com")
                        .roles(List.of(UserRole.USER, UserRole.ADMIN))
                        .build()
        ).getId();

        userRepository.saveAndFlush(
                User.builder()
                        .username("userke")
                        .password(passwordEncoder.encode("password"))
                        .email("userke@sudokucaff.com")
                        .roles(List.of(UserRole.USER))
                        .build()
        );
    }

    @Test
    void testFindByUsername() {
        User found = null;
        if(userRepository.findByUsername("admin").isPresent()) {
            found = userRepository.findByUsername("admin").get();
        }
        assert found != null;
        assertThat(found.getId()).isEqualTo(adminId);
    }

    @Test
    void testFindAll() {
        List<User> caffFiles = userRepository.findAll();
        assertThat(caffFiles.size()).isEqualTo(2);
    }

    @Test
    void testRemove() {
        User test = null;
        if(userRepository.findById(adminId).isPresent()){
            test = userRepository.findById(adminId).get();
        }
        assert test != null;
        userRepository.delete(test);
        assertThat(userRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void testInsert() {
        userRepository.saveAndFlush(User.builder()
                .username("inas")
                .password(passwordEncoder.encode("asd123"))
                .email("inas@sudokucaff.com")
                .roles(List.of(UserRole.USER))
                .build());
        assertThat(userRepository.findAll().size()).isEqualTo(3);
    }
}
