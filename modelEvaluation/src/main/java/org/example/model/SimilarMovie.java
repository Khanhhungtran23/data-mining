package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimilarMovie {
    private String id;
    private String title;
    private Double rating;
    private Double similarity;
}