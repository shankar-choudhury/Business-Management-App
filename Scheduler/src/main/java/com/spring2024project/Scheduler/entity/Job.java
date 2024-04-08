package com.spring2024project.Scheduler.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring2024project.Scheduler.entity.employees.Electrician;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.time.LocalDate;

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
    private LocalDate start;

    @Column
    private LocalDate end;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address jobAddress;

    @Column
    private boolean isComplete;
}
