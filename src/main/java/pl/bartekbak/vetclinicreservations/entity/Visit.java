package pl.bartekbak.vetclinicreservations.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int customerId;
    private int pin;

    @ManyToOne
    private Vet vet;

    private LocalDate date;
    private LocalTime time;

    private int estimatedVisitDurationMinutes = 20;

}
