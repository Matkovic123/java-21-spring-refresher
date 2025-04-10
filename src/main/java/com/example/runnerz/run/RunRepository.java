package com.example.runnerz.run;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Repository
public class RunRepository {

    private static final Logger log = Logger.getLogger(RunRepository.class.getName());
    private final JdbcClient jdbcClient;

    public RunRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Run> findAll() {
        return jdbcClient.sql("select * from Run").query((Run.class)).list();
    }

    public Optional<Run> findById(Long id) {
        return jdbcClient.sql("SELECT * FROM Run WHERE id = :id")
                .param("id", id)
                .query(Run.class)
                .optional();
    }

    public void create(Run run) {
        var updated = jdbcClient.sql("INSERT INTO Run(id, title, started_on, completed_on, miles, location) values(?,?,?,?,?,?)")
                .params(List.of(run.id(), run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString()))
                .update();
        Assert.state(updated == 1, "Failed to create run: " + run.title());
    }

    public void update(Run run, Long id) {
        var updated = jdbcClient.sql("UPDATE Run SET title = ?, started_on = ?, completed_on = ?, miles = ?, location = ? WHERE id = ?")
                .params(List.of(run.title(), run.startedOn(), run.completedOn(), run.miles(), run.location().toString(), id))
                .update();
        Assert.state(updated == 1, "Failed to update run: " + run.title());
    }

    public void delete(Long id) {
        var updated = jdbcClient.sql("DELETE FROM Run WHERE id = :id").param("id", id).update();
        Assert.state(updated == 1, "Failed to delete run: " + id);
    }

    public int count() {
        return jdbcClient.sql("SELECT COUNT(id) FROM Run").query(Integer.class).single();

    }

    public void saveAll(List<Run> runs) {
        // Build SQL for a multi-row insert
        StringBuilder sql = new StringBuilder(
                "INSERT INTO Run(id, title, started_on, completed_on, miles, location) VALUES ");

        List<Object> allParams = new ArrayList<>();
        for (int i = 0; i < runs.size(); i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append("(?, ?, ?, ?, ?, ?)");

            Run run = runs.get(i);
            allParams.add(run.id());
            allParams.add(run.title());
            allParams.add(run.startedOn());
            allParams.add(run.completedOn());
            allParams.add(run.miles());
            allParams.add(run.location().toString());
        }

        // Execute the multi-row insert
        int updated = jdbcClient.sql(sql.toString())
                .params(allParams)
                .update();

        // Verify all rows were inserted
        if (updated != runs.size()) {
            throw new IllegalStateException("Expected to insert " + runs.size() +
                    " runs but inserted " + updated);
        }
    }
}