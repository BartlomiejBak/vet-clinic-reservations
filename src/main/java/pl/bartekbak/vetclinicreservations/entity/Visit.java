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
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerId;
    private String pin;
    @ManyToOne
    @JoinColumn(name = "vet_id")
    private Vet vet;
    private LocalDate date;
    private LocalTime time;
    @Builder.Default
    private int visitDurationMinutes = 20;

}
