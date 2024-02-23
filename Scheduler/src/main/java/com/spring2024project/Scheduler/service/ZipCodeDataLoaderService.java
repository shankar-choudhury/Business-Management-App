package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.ZipCodeData;
import com.spring2024project.Scheduler.exception.StringValidationException;
import com.spring2024project.Scheduler.repository.ZipCodeDataRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Objects;

/**
 * Service class for loading ZIP code data from a CSV file into the database.
 */
@Service
public class ZipCodeDataLoaderService {

    private final ZipCodeDataRepository zipCodeDataRepository;

    /**
     * Constructor for ZipCodeDataLoaderService.
     * @param zipCodeDataRepository The repository for ZIP code data.
     */
    @Autowired
    public ZipCodeDataLoaderService(ZipCodeDataRepository zipCodeDataRepository) {
        this.zipCodeDataRepository = zipCodeDataRepository;
    }

    /**
     * Method invoked after construction to load ZIP code data from a CSV file into the database.
     */
    @PostConstruct
    public void loadZipCodeData() {
        try(var reader = new BufferedReader(
                new InputStreamReader(
                        new ClassPathResource("zip_code_database.csv").getInputStream()))) {
            String line;
            while (Objects.nonNull(line = reader.readLine())) {
                String[] data = line.split(",");

                var zip = data[0];
                var primary_city = data[1];
                var acceptable_cities = data[2];
                var state = data[3];
                var timezone = data[4];

                zipCodeDataRepository.save(
                        ZipCodeData.of(zip,
                                primary_city,
                                acceptable_cities,
                                state,
                                timezone));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException i) {
            if (i.getCause() instanceof StringValidationException) {
                var s = (StringValidationException) i.getCause();
                System.out.println(s.cause());
                System.out.println(s.explanation());
            }
        }
    }

}
