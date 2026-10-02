package com.examples.prototype.data.jdbc.prototypespringdatajdbc;

import com.examples.prototype.data.jdbc.prototypespringdatajdbc.model.Address;
import com.examples.prototype.data.jdbc.prototypespringdatajdbc.model.User;
import com.examples.prototype.data.jdbc.prototypespringdatajdbc.model.UserWithAddress;
import com.examples.prototype.data.jdbc.prototypespringdatajdbc.repository.UserRepository;
import com.examples.prototype.data.jdbc.prototypespringdatajdbc.repository.UserWithAddressRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QueryFromTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserWithAddressRepository withAddressRepository;
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private String sql = """
            select *
            from users
            where users.id = :userId;
            """;
    private String sqlAddresses = """
            select * from addresses where user_id = :userId
            """;
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

    @Test
    void testViaAggregateOperations() {
        var result = jdbcTemplate.queryForObject(sql,
                Map.of("userId", 2),
                new BeanPropertyRowMapper<>(User.class));
        var addresses = jdbcTemplate.query(sqlAddresses,
                Map.of("userId", 2),
                new BeanPropertyRowMapper<>(Address.class));
        var userWithAddress = UserWithAddress.builder()
                .addresses(addresses)
                .id(result.getId())
                .email(result.getEmail())
                .userName(result.getUserName())
                .build();

        assertThat(userWithAddress).isNotNull();
        assertThat(userWithAddress.getAddresses()).hasSize(1);
        var address = userWithAddress.getAddresses().get(0);
        assertThat(address).isNotNull();
        assertThat(address.getId()).isNotNull();
        assertThat(address.getCity()).isNotNull();
        assertThat(address.getCountry()).isNotNull();
        assertThat(address.getState()).isNotNull();
        assertThat(address.getZip()).isNotNull();
        assertThat(address.getUserId()).isEqualTo(2);
    }

    @Test
    void testInsert() {
        var user = User.builder()
                .email("testinsert@gmail.com")
                .userName("testInsert")
                .build();
        userRepository.save(user);
        var userInDb = userRepository.findAllByEmail("testinsert@gmail.com");
        assertThat(userInDb).isNotNull();
        assertThat(userInDb).isNotEmpty();
        assertThat(user.getEmail()).isEqualTo(userInDb.get(0).getEmail());
        assertThat(user.getUserName()).isEqualTo(userInDb.get(0).getUserName());
        var userForUpdate=userInDb.get(0);
        userForUpdate.setEmail("updated_testinsert@gmail.com");
        userRepository.save(userForUpdate);
        var userInDbAfterUpdate = userRepository.findAllByEmail("updated_testinsert@gmail.com");
        assertThat(userInDbAfterUpdate).isNotNull();
        assertThat(userInDbAfterUpdate).isNotEmpty();
        assertThat(userForUpdate.getEmail()).isEqualTo(userInDbAfterUpdate.get(0).getEmail());
        assertThat(userForUpdate.getUserName()).isEqualTo(userInDbAfterUpdate.get(0).getUserName());
        userRepository.deleteById(userInDbAfterUpdate.get(0).getId());
        var userAfterDelete = userRepository.findAllByEmail("updated_testinsert@gmail.com");
        assertThat(userAfterDelete).isEmpty();
    }

    @Test
    void testOptional(){
        var user = userRepository.findByEmail("user1@email.com");
        assertThat(user).isPresent();
        var notPresent = userRepository.findByEmail("notPresentuser1@email.com");
        assertThat(notPresent).isNotPresent();
    }
}
