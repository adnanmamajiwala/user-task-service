package com.demo.userservice.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "task", path = "task")
public interface TaskRepository extends JpaRepository<Task, Long> {

    @RestResource(rel = "task", path = "statusEquals")
    List<Task> findTasksByStatusEquals(String status);

    @RestResource(rel = "task", path = "statusAndCompleteByEquals")
    List<Task> findTasksByStatusEqualsAndCompleteByEquals(@Param("status") String status,
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                          @Param("completeBy") LocalDate completeBy);

    @RestResource(rel = "task", path = "titleContains")
    List<Task> findTasksByTitleContains(String title);
}
