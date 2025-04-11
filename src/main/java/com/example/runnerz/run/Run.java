package com.example.runnerz.run;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.time.Duration;
import java.time.LocalDateTime;

public record Run(@Id
                  Long id,
                  @NotEmpty String title,
                  LocalDateTime startedOn,
                  LocalDateTime completedOn,
                  @Positive Integer miles,
                  Location location,
                  @Version
                  Integer version
) {

    public Run {
        if (!completedOn.isAfter(startedOn)) {
            throw new IllegalArgumentException("Completed On must be after Started On");
        }
    }

    public Run(Long id, @NotEmpty String title, LocalDateTime startedOn, LocalDateTime completedOn, @Positive Integer miles, Location location) {
        this(id, title, startedOn, completedOn, miles, location, null);
    }

    public Duration getDuration() {
        return Duration.between(startedOn, completedOn);
    }

    public Integer getAvgPace() {
        return Math.toIntExact(getDuration().toMinutes() / miles);
    }
}
