package com.examples.prototype.data.jdbc.prototypespringdatajdbc;

import com.examples.prototype.data.jdbc.prototypespringdatajdbc.repository.UserRepository;
import com.examples.prototype.data.jdbc.prototypespringdatajdbc.repository.UserWithAddressRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QueryFromTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserWithAddressRepository withAddressRepository;

    @Test
    void getAll() {
        var users = userRepository.getAll();
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    void getAllProjection() {
        var users = userRepository.projectionTest();
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(2);
        assertThat(users.getFirst().getEmail()).isNotEmpty();
        assertThat(users.getFirst().getId()).isNotNull();
    }

    @Test
    void getByParam() {
        var users = userRepository.findByTest("user1");
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.getFirst().getEmail()).isNotEmpty().isEqualTo("user1@email.com");
        assertThat(users.getFirst().getId()).isNotNull();
    }

    @Test
    void findByParamOriginal() {
        var users = userRepository.findAllByEmail("user1@email.com");
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.getFirst().getEmail()).isNotEmpty().isEqualTo("user1@email.com");
        assertThat(users.getFirst().getId()).isNotNull();
    }

    @Test
    void findByParamOriginalQuery() {
        var users = userRepository.findAllByEmailQuery("user1@email.com");
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.getFirst().getEmail()).isNotEmpty().isEqualTo("user1@email.com");
        assertThat(users.getFirst().getId()).isNotNull();
    }

    @Test
    void testJoin() {
        var result = withAddressRepository.getById(2l);
        assertThat(result).isNotEmpty();
        assertThat(result.get().getAddresses()).hasSize(1);
        var address = result.get().getAddresses().get(0);
        assertThat(address).isNotNull();
        assertThat(address.getId()).isNotNull();
        assertThat(address.getCity()).isNotNull();
        assertThat(address.getCountry()).isNotNull();
        assertThat(address.getState()).isNotNull();
        assertThat(address.getZip()).isNotNull();
        assertThat(address.getUserId()).isEqualTo(2);
    }
}
