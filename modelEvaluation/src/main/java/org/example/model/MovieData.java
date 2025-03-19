package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieData {
    private String title;
    private Double budget;
    private Double revenue;
    private Integer runtime;
    private List<String> genres;
    private String releaseDate;
    private String originalLanguage;
    private Boolean adult;
    private Double popularity;

    // add more fields
    // Lombok sẽ tự động tạo getters, setters, toString, equals, và hashCode
}