package com.spring2024project.Scheduler;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.ZipCodeData;
import com.spring2024project.Scheduler.repository.ZipCodeDataRepository;
import com.spring2024project.Scheduler.service.ZipCodeDataLoaderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SchedulerApplication.class)
public class ZipCodeDataLoaderTests {
    @Autowired
    private ZipCodeDataRepository realZipRepo;
    @Autowired
    private ZipCodeDataLoaderService realZipLoader;


    @Before
    public void setUp() {
        realZipLoader.loadZipCodeData();
    }

    @Test
    public void testSuccessfulZipCodeImport() {
        var repoData = (List<ZipCodeData>) realZipRepo.findAll();
        assertTrue(repoData.stream().allMatch(Objects::nonNull));
    }

    @Test
    public void multiCityZipCode() {
        Set<String> expected = Set.of("RIVERVIEW", "BROWNSTWN TWP", "BROWNSTOWN", "WYANDOTTE", "BROWNSTOWN TOWNSHIP", "BROWNSTOWN TWP");
        var actual = realZipRepo.findById("48193")
                .map(ZipCodeData::getAcceptableCities)
                .orElseGet(HashSet::new);
        assertTrue(expected.containsAll(actual));
    }

    @Test
    public void allCitiesAndStatesCapitalized() {
        var repoData = (List<ZipCodeData>) realZipRepo.findAll();
        String onlyTwoCapitalLetters = "^[A-Z]{2}$";
        String capitalLettersAndOther = "^[A-Z\\s\\S]+$";
        assertTrue(repoData.stream()
                .allMatch(
                        zip -> zip.getState().matches(onlyTwoCapitalLetters) &&
                        zip.getAcceptableCities().stream().allMatch(city -> city.matches(capitalLettersAndOther))));
    }

    @Test
    public void as() {
        var multiCityAddress = new Address();
        multiCityAddress.setBuildingNumber("02");
        multiCityAddress.setStreet("roadhouse rd");
        multiCityAddress.setCity("riverview");
        multiCityAddress.setState("michigan");
        multiCityAddress.setZipcode("48193");

        var a = Address.from(multiCityAddress);

        var z = realZipRepo.findById(a.getZipcode());
        System.out.println(z);
    }
}
