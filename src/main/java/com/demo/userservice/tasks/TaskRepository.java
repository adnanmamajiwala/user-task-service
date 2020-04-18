package com.demo.userservice.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "task", path = "task")
public interface TaskRepository extends JpaRepository<Task, Long> {
}
