package com.examples.prototype.data.jdbc.prototypespringdatajdbc;

import com.examples.prototype.data.jdbc.prototypespringdatajdbc.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FileQueryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void getAll() {
        var users = userRepository.getAll();
        Assertions.assertThat(users).isNotEmpty();
        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    void getAllProjection() {
        var users = userRepository.projectionTest();
        Assertions.assertThat(users).isNotEmpty();
        Assertions.assertThat(users.size()).isEqualTo(2);
        Assertions.assertThat(users.getFirst().getEmail()).isNotEmpty();
        Assertions.assertThat(users.getFirst().getId()).isNotNull();
    }

    @Test
    void getByParam() {
        var users = userRepository.findByTest("user1");
        Assertions.assertThat(users).isNotEmpty();
        Assertions.assertThat(users.size()).isEqualTo(1);
        Assertions.assertThat(users.getFirst().getEmail()).isNotEmpty().isEqualTo("user1@email.com");
        Assertions.assertThat(users.getFirst().getId()).isNotNull();
    }

    @Test
    void findByParamOriginal() {
        var users = userRepository.findAllByEmail("user1@email.com");
        Assertions.assertThat(users).isNotEmpty();
        Assertions.assertThat(users.size()).isEqualTo(1);
        Assertions.assertThat(users.getFirst().getEmail()).isNotEmpty().isEqualTo("user1@email.com");
        Assertions.assertThat(users.getFirst().getId()).isNotNull();
    }

    @Test
    void findByParamOriginalQuery() {
        var users = userRepository.findAllByEmailQuery("user1@email.com");
        Assertions.assertThat(users).isNotEmpty();
        Assertions.assertThat(users.size()).isEqualTo(1);
        Assertions.assertThat(users.getFirst().getEmail()).isNotEmpty().isEqualTo("user1@email.com");
        Assertions.assertThat(users.getFirst().getId()).isNotNull();
    }
}
