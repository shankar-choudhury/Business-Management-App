package com.spring2024project.Scheduler.controllerTests;
import com.spring2024project.Scheduler.entityFunctionalities.address.AddressController;
import com.spring2024project.Scheduler.entityFunctionalities.address.Address;
import com.spring2024project.Scheduler.entityFunctionalities.BaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AddressControllerTest {

    @Mock
    private BaseService<Address> addressService;

    @InjectMocks
    private AddressController addressController;

    private Address address;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        address = Address.from(
                Address.builder()
                .buildingNumber("04")
                .street("Hooyah ave")
                .city("Hanover")
                .state("NH")
                .zipcode("03755")
                .build());
    }

    @Test
    void testGetAll() {
        when(addressService.getAll()).thenReturn(Collections.singletonList(address));

        ResponseEntity<List<Address>> responseEntity = addressController.getAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());
    }

    @Test
    void testGetById() {
        when(addressService.getById(anyInt())).thenReturn(address);

        ResponseEntity<Address> responseEntity = addressController.getById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(address, responseEntity.getBody());
    }

    @Test
    void testCreate() {
        when(addressService.create(any())).thenReturn(address);

        ResponseEntity<Address> responseEntity = addressController.create(address);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(address, responseEntity.getBody());
    }

    @Test
    void testUpdate() {

        when(addressService.update(anyInt(), any())).thenReturn(address);

        ResponseEntity<Address> responseEntity = addressController.update(1, address);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(address, responseEntity.getBody());
    }

    @Test
    void testDelete() {

        when(addressService.delete(anyInt())).thenReturn(address);

        ResponseEntity<Address> responseEntity = addressController.delete(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(address, responseEntity.getBody());
    }
}
