package com.library.management.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @NotBlank
    private String isbn;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    // Would prefer to have the publication year as LocalDate
    @Max(2024)
    private int publicationYear;

    @Min(1)
    private int availableCopies;
}
