package io.wakelesstuna.accountservice.containers;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
// @Testcontainers
public class MyPostgresTestContainer {

    //@Container
    private static PostgreSQLContainer container = (PostgreSQLContainer) new PostgreSQLContainer("postgres:alpine")
            .withReuse(true);
    // to get withReuse to work you need to add a line to your .testcontainers.properties
    // with is located in your home directory exp C:/user/youruser
    // add "testcontainers.reuse.enable=true" to enable this
    // but if you has the @Container and @Testcontainers to disable the junit test container support
    // cause the support starts and shutdown in every test
    // then you need to start the container manually with the container.start() method

    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        // overriding the application.props file with the credentials from
        // the container we spin up
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
}
