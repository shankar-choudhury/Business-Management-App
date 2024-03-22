package com.spring2024project.Scheduler.entity.employees;

import com.spring2024project.Scheduler.constantValues.OfficeAccessPermissions;
import com.spring2024project.Scheduler.entity.Address;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"firstName", "lastName", "email", "phoneNumber"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class Electrician extends Employee {
    private OfficeAccessPermissions level;
    private String specialties;
}
