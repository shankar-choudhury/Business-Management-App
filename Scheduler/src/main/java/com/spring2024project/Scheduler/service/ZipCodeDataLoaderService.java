package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.repository.ZipCodeDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ZipCodeDataLoaderService {
    private final ZipCodeDataRepository zipCodeDataRepository;

    @Autowired
    public ZipCodeDataLoaderService(ZipCodeDataRepository zipCodeDataRepository) {
        this.zipCodeDataRepository = zipCodeDataRepository;
    }

    public void loadZipCodeData() {
        try(var reader = new BufferedReader(
                new InputStreamReader(
                        new ClassPathResource("zip_code_database.csv").getInputStream()))) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
