package br.com.vtferrari.challenge.usecase.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Term {
    SIX("6", 6, 3.9, 4.7, 5.5, 6.4),
    NINE("9", 9, 4.2, 5.0, 5.8, 6.6),
    TWELVE("12", 12, 4.5, 5.3, 6.1, 6.9);

    private String name;
    private int terms;
    private double high;
    private double mid;
    private double low;
    private double lowest;

    public double getByScore(int score) {
        if (score >= 900) {
            return this.high;
        }
        if (score >= 800) {
            return this.mid;
        }
        if (score >= 700) {
            return this.low;
        }
        return this.lowest;
    }

    public static Term fromValue(String name) {
        return Stream.of(Term.values())
                .filter(term -> term.name.equals(name))
                .findAny()
                .orElse(null);
    }
}
