package com.spring2024project.Scheduler;

import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;
import com.spring2024project.Scheduler.repository.AddressRepository;
import com.spring2024project.Scheduler.service.AddressService;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ZipCodeValidatorTag zipCodeValidator;

    @InjectMocks
    private AddressService addressService;


}
