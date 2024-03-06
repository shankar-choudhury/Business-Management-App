package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.ZipCodeData;
import com.spring2024project.Scheduler.exception.StringValidationException;
import com.spring2024project.Scheduler.repository.ZipCodeDataRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service class for loading ZIP code data from a CSV file into the database.
 */
@Getter
@Service
public class ZipCodeDataLoaderService {

    private final ZipCodeDataRepository zipCodeDataRepository;

    /**
     * Constructor for ZipCodeDataLoaderService.
     *
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
        try (var reader = new BufferedReader(
                new InputStreamReader(
                        new ClassPathResource("zip_code_database.csv").getInputStream()))) {
            String line;
            while (Objects.nonNull(line = reader.readLine())) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                var zip = data[0].trim();
                var primary_city = data[1].trim();
                var acceptable_cities = data[2];
                var cities = parseAcceptableCities(acceptable_cities);
                cities.add(primary_city);
                var state = data[3].trim();
                var timezone = data[4].trim();

                zipCodeDataRepository.save(
                        ZipCodeData.of(zip,
                                cities,
                                state,
                                timezone));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException i) {
            if (i.getCause() instanceof StringValidationException s) {
                System.out.println(s.cause());
                System.out.println(s.getBadString());
                System.out.println(s.explanation());
            }
        }
    }

    private Set<String> parseAcceptableCities(String cities) {
        Pattern pattern = Pattern.compile("\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(cities);

        return matcher.results()
                .map(result -> result.group(1))
                .flatMap(match -> Arrays.stream(match.split(",\\s*")))
                .map(String::toUpperCase)
                .collect(Collectors.toSet());
    }

}