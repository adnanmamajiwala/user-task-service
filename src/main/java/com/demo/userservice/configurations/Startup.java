package com.demo.userservice.configurations;

import com.demo.userservice.tasks.Task;
import com.demo.userservice.tasks.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class Startup implements CommandLineRunner {

    private final TaskRepository repository;
    private final ObjectMapper mapper;

    @Override
    public void run(String... args) throws Exception {
        Task[] data = mapper.readValue(new ClassPathResource("data.json").getInputStream(), Task[].class);
        List<Task> tasks = new ArrayList<>(Arrays.asList(data));
        repository.saveAll(tasks);
    }
}
