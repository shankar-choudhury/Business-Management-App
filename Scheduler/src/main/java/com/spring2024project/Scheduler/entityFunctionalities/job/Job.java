package com.spring2024project.Scheduler.entityFunctionalities.job;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.spring2024project.Scheduler.entityFunctionalities.address.Address;
import com.spring2024project.Scheduler.entityFunctionalities.people.customer.Customer;
import com.spring2024project.Scheduler.entityFunctionalities.people.electrician.Electrician;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.time.LocalDate;

import static com.spring2024project.Scheduler.validatingMethods.ListValidator.*;

/**
 * TODO: when fetching job, display job details including regular fields, job address, regular customer details, and customer's generators
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(exclude = {"id", "electricians"})
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "customer-jobs")
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
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

    public static Job from(Job entity) {
        return new Job(
                Customer.from(entity.getCustomer()),
                entity.getJobAddress(),
                validateElectricians(entity),
                entity.getStartDate());
    }

    public static Job emptyJob() {
        return new Job(
                Customer.defaultCustomer(),
                Address.emptyAddress(),
                List.of(),
                LocalDate.of(0,1,1));
    }

    private static List<Electrician> validateElectricians(Job entity) {
        return verifyAllElementsMatch(
                verifyNonNullElements(
                        verifyNonEmpty(
                                verifyNonNull(entity.getElectricians()))),
                electrician -> electrician.getJobs().stream()
                                .noneMatch(job -> job.getStartDate().equals(entity.getStartDate())));

    }
}
