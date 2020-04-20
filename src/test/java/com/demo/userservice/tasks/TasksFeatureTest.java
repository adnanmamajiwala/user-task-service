package com.demo.userservice.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class TasksFeatureTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = String.format("http://localhost:%d/api/task", port);
    }

    @Test
    @Order(1)
    void get_tasks_shouldReturnListOfTasks() throws Exception {
        Pageable response = this.restTemplate.getForObject(baseUrl, Pageable.class);
        Task[] tasks = mapper.readValue(new ClassPathResource("data.json").getFile(), Task[].class);
        List<Task> content = response.getContent();
        assertThat(content).hasSize(7);
        assertThat(content)
                .usingElementComparatorIgnoringFields("id")
                .containsExactly(tasks);
    }

    @Test
    @Order(2)
    void post_tasks_shouldInsertNewTask() throws Exception {
        Task request = Task.builder()
                .title("New Sample Task").status("pending")
                .description("Some description").createdOn(LocalDate.now()).completeBy(LocalDate.now()).build();
        Task postResponse = this.restTemplate.postForObject(baseUrl, request, Task.class);
        assertThat(postResponse.getId()).isNotNull();

        Pageable response = this.restTemplate.getForObject(baseUrl, Pageable.class);
        List<Task> content = response.getContent();
        assertThat(content).hasSize(8);
        assertThat(content)
                .usingElementComparatorIgnoringFields("id")
                .contains(postResponse);
    }

    @Test
    @Order(3)
    void get_tasks_shouldReturnTaskById() throws Exception {
        String url = String.format("%s/1", baseUrl);
        Task response = this.restTemplate.getForObject(url, Task.class);
        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getStatus()).isEqualTo("pending");
        assertThat(response.getTitle()).isEqualTo("My Task 1");
        assertThat(response.getDescription()).isEqualTo("My task 1's full description");
        assertThat(response.getCreatedOn()).isEqualTo("2020-04-18");
        assertThat(response.getCompleteBy()).isEqualTo("2020-04-18");
    }

    @Test
    @Order(4)
    void put_tasks_shouldUpdateExistingTask() throws Exception {
        String url = String.format("%s/1", baseUrl);

        Task taskToUpdate = this.restTemplate.getForObject(url, Task.class);
        taskToUpdate.setStatus("completed");

        this.restTemplate.put(url, taskToUpdate, Task.class);

        Task taskAfterPut = this.restTemplate.getForObject(url, Task.class);

        assertThat(taskAfterPut).isEqualTo(taskToUpdate);
    }

    @Test
    @Order(5)
    void delete_tasks_shouldUpdateExistingTask() throws Exception {
        String url = String.format("%s/5", baseUrl);
        this.restTemplate.delete(url);
        Task taskAfterDelete = this.restTemplate.getForObject(url, Task.class);
        assertThat(taskAfterDelete).isEqualTo(null);
    }
}
