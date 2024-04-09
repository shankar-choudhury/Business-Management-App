package com.spring2024project.Scheduler.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring2024project.Scheduler.entity.employees.Electrician;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.time.LocalDate;
import java.util.Objects;

import static com.spring2024project.Scheduler.validatingMethods.ListValidator.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JsonBackReference(value = "customer-jobs")
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "job_electrician",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "electrician_id")
    )
    private List<Electrician> electricians;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address jobAddress;

    @Column
    private boolean isComplete;

    private Job(Customer customer, Address jobAddress, List<Electrician> electricians, LocalDate startDate) {
        this.customer = customer;
        this.jobAddress = jobAddress;
        this.electricians = electricians;
        this.startDate = startDate;
    }

    public Job from(Job entity) {
        return new Job(
                Customer.from(entity.getCustomer()),
                entity.getJobAddress(),
                verifyNoElementsMatch(
                        verifyNonNullElements(
                            verifyNonEmpty(
                                    verifyNonNull(entity.getElectricians()))),
                        electrician -> {
                            if (Objects.nonNull(electrician.getJobs()))
                                return electrician.getJobs().stream().noneMatch(job -> job.getStartDate().equals(entity.getStartDate()));
                            else return true;}),
                entity.getStartDate());
    }
}
