package pl.bartekbak.vetclinicreservations.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {
    @Id
    private int id;
    private String firstName;
    private String lastName;
    private int pin;
    @OneToMany
    private List<Visit> visits = new ArrayList<>();
}
