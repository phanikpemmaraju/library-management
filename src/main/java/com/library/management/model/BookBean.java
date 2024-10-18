package com.library.management.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BOOKS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookBean {

    @Id
    @Column(nullable = false, updatable = false)
    private String isbn;
    @Column(nullable = false, updatable = false)
    private String title;
    @Column(nullable = false, updatable = false)
    private String author;
    @Column(nullable = false, updatable = false)
    private int publicationYear;
    @Column(nullable = false)
    private int availableCopies;
    @Column(nullable = false)
    private int initialCopies;

}
