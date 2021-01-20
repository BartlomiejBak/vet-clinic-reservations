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

    private String customerId;
    private String pin;

    @ManyToOne
    private Vet vet;

    private LocalDate date;
    private LocalTime time;

    private final int visitDurationMinutes = 20;

}
