package com.demo.userservice.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.core.io.ClassPathResource;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Task[] tasks = mapper.readValue(new ClassPathResource("data.json").getFile(), Task[].class);
        for (Task task : tasks) {
            entityManager.persistAndFlush(task);
        }
    }

    @Test
    void findTasksByStatusEquals_whenPending_returnsOnlyPendingTasks() {
        List<Task> pending = repository.findTasksByStatusEquals("pending");
        assertThat(pending).hasSize(6);
    }

    @Test
    void findTasksByStatusEquals_whenCompleted_returnsOnlyCompletedTasks() {
        List<Task> completed = repository.findTasksByStatusEquals("completed");
        assertThat(completed).hasSize(1);
    }

    @Test
    void findTasksByStatusEquals_whenInvalid_returnsEmpty() {
        List<Task> empty = repository.findTasksByStatusEquals("someString");
        assertThat(empty).isEmpty();
    }

    @Test
    void findTasksByStatusEqualsAndCompleteByEquals_whenValid_returnList() {
        List<Task> pending = repository.findTasksByStatusEqualsAndCompleteByEquals("pending", LocalDate.of(2020, 4, 18));
        assertThat(pending).hasSize(4);
    }

    @Test
    void findTasksByStatusEqualsAndCompleteByEquals_whenInValid_returnEmptyList() {
        List<Task> invalid = repository.findTasksByStatusEqualsAndCompleteByEquals("someInvalid", LocalDate.of(2020, 4, 18));
        assertThat(invalid).isEmpty();
    }

    @Test
    void findTasksByTitleContains_whenValid_returnList() {
        List<Task> searchByTitle = repository.findTasksByTitleContains("My Task 1");
        assertThat(searchByTitle).hasSize(1);
    }

    @Test
    void findTasksByTitleContains_whenInValid_returnEmpty() {
        List<Task> invalid = repository.findTasksByTitleContains("My Title 1");
        assertThat(invalid).isEmpty();
    }

}