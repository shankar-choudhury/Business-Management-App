package com.spring2024project.Scheduler;

import com.spring2024project.Scheduler.entity.ZipCodeData;
import com.spring2024project.Scheduler.repository.ZipCodeDataRepository;
import com.spring2024project.Scheduler.service.ZipCodeDataLoaderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
        realZipLoader.loadZipCodeData("zip_code_database.csv");
    }

    @Test
    public void testSuccessfulZipCodeImport() throws IOException {
        var repoData = (List<ZipCodeData>) realZipRepo.findAll();
        assertTrue(repoData.stream().allMatch(Objects::nonNull));
    }


}
