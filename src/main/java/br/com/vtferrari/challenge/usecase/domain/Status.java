package br.com.vtferrari.challenge.usecase.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Status {
    PROCESSING("processing"), COMPLETED("completed");
    private String name;

    public static Status fromValue(String name) {
        return Stream.of(Status.values())
                .filter(status -> status.name.equals(name))
                .findAny()
                .orElse(null);
    }
}
