package com.spring2024project.Scheduler;

import com.spring2024project.Scheduler.repository.ZipCodeDataRepository;
import com.spring2024project.Scheduler.service.ZipCodeDataLoaderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SchedulerApplication.class)
public class ZipCodeDataTests {
    @Autowired
    private ZipCodeDataLoaderService zipLoader;
    @Autowired
    private ZipCodeDataRepository zipRepo;

    @Before
    public void setup() {
        zipLoader.loadZipCodeData();
    }

    @Test
    public void testEntries() {
        var l = zipLoader.getZipCodeDataRepository().findById("1230");
        System.out.println(l);
    }

}
