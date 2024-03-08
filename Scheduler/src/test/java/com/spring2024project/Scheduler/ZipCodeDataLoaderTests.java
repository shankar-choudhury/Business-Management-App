package com.spring2024project.Scheduler;

import com.spring2024project.Scheduler.entity.ZipCodeData;
import com.spring2024project.Scheduler.repository.ZipCodeDataRepository;
import com.spring2024project.Scheduler.service.ZipCodeDataLoaderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SchedulerApplication.class)
public class ZipCodeDataLoaderTests {
    @Autowired
    private ZipCodeDataRepository zipRepo;
    @Autowired
    private ZipCodeDataLoaderService zipLoader;


    @Before
    public void setUp() {
        zipLoader.loadZipCodeData();
    }

    @Test
    public void testSuccessfulZipCodeImport() {
        var repoData = (List<ZipCodeData>) zipRepo.findAll();
        assertTrue(repoData.stream().allMatch(Objects::nonNull));
    }

    @Test
    public void multiCityZipCode() {
        Set<String> expected = Set.of("RIVERVIEW", "BROWNSTWN TWP", "BROWNSTOWN", "WYANDOTTE", "BROWNSTOWN TOWNSHIP", "BROWNSTOWN TWP");
        var actual = zipRepo.findById("48193")
                .map(ZipCodeData::getAcceptableCities)
                .orElseGet(HashSet::new);
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void allCitiesAndStatesCapitalized() {
        var repoData = (List<ZipCodeData>) zipRepo.findAll();
        String onlyTwoCapitalLetters = "^[A-Z]{2}$";
        String capitalLettersAndOther = "^[A-Z\\s\\S]+$";
        assertTrue(repoData.stream()
                .allMatch(
                        zip -> zip.getState().matches(onlyTwoCapitalLetters) &&
                        zip.getAcceptableCities().stream().allMatch(city -> city.matches(capitalLettersAndOther))));
    }

    @Test
    public void testDataCompleteness() {
        List<ZipCodeData> databaseRecords = (List<ZipCodeData>) zipRepo.findAll();
        List<String> databaseZipCodes = databaseRecords.stream()
                .map(ZipCodeData::getZip)
                .toList();
        List<String> csvZipCodes = extractFirstColumnValues("zip_code_database.csv");
        assertEquals(csvZipCodes.size(), databaseRecords.size());
        assertTrue(csvZipCodes.containsAll(databaseZipCodes));
    }

    private static List<String> extractFirstColumnValues(String filePath) {
        List<String> firstColumnValues = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new ClassPathResource(filePath).getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Split by comma
                if (values.length > 0) {
                    firstColumnValues.add(values[0]); // Add the first value to the list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return firstColumnValues;
    }
}
