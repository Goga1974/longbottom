package com.goga74.platform.DB.entity.elprice;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "price_data")
public class PriceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate date; // Колонка для хранения даты

    @Column(columnDefinition = "JSON", nullable = false)
    private String jsonData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
