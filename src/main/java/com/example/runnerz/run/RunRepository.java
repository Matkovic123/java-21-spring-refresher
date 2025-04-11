package com.example.runnerz.run;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface RunRepository extends ListCrudRepository<Run, Long> {

    List<Run> findByLocation(Location location);

}
