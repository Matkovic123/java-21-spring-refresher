package com.example.runnerz.run;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface RunRepository extends ListCrudRepository<Run, Long> {

    List<Run> findByLocation(Location location);

    List<Run> findAll();

    Optional<Run> findById(Long id);

    void create(Run run);

    void update(Run run, Long id);

    void delete(Long id);

    long count();

    void saveAll(List<Run> runs);

    List<Run> findByLocation(String location);
}
