package pl.bartekbak.vetclinicreservations.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bartekbak.vetclinicreservations.entity.Vet;
import pl.bartekbak.vetclinicreservations.entity.Visit;
import pl.bartekbak.vetclinicreservations.repository.VetRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VetManagerTest {

    @Mock
    private VetRepository repository;

    @InjectMocks
    private VetManager manager;

    private Vet firstVet = Vet.builder()
            .id(1L)
            .firstName("First")
            .visits(new ArrayList<>())
            .build();
    private Vet secondVet = Vet.builder()
            .id(2L)
            .firstName("Second")
            .visits(new ArrayList<>())
            .build();
    private Vet thirdVet = Vet.builder()
            .id(3L)
            .firstName("Third")
            .visits(new ArrayList<>())
            .build();

    private List<Vet> twoVets;

    private Visit firstVisit = Visit.builder()
            .id(1L)
            .vet(firstVet)
            .customerId(1234)
            .date(LocalDate.of(2020, 5, 5))
            .time(LocalTime.of(12, 00))
            .pin(4567)
            .build();
    private Visit secondVisit = Visit.builder()
            .id(2L)
            .vet(firstVet)
            .customerId(1235)
            .date(LocalDate.of(2020, 5, 5))
            .time(LocalTime.of(12, 20))
            .pin(4568)
            .build();
    private Visit thirdVisit = Visit.builder()
            .id(3L)
            .vet(firstVet)
            .customerId(1236)
            .date(LocalDate.of(2020, 5, 5))
            .time(LocalTime.of(12, 10))
            .pin(4569)
            .build();


    @BeforeEach
    void setUp() {
        twoVets = List.of(firstVet, secondVet);
    }

    @Test
    void findById_shouldReturnFirstVet() {
        //given
        when(repository.findById(anyLong())).thenReturn(Optional.of(firstVet));
        //when
        Vet result = manager.findById(1L);
        //then
        assertEquals(firstVet, result);
    }

    @Test
    void findAll_shouldReturnTwoVets() {
        //given
        when(repository.findAll()).thenReturn(twoVets);
        //when
        List<Vet> result = manager.findAll();
        //then
        assertEquals(twoVets, result);
    }

    @Test
    void addVet_shouldInvokeSaveOnce() {
        //given
        when(repository.save(thirdVet)).thenReturn(thirdVet);
        //when
        Vet result = manager.addVet(thirdVet);
        //then
        assertEquals(thirdVet, result);
        verify(repository, times(1)).save(thirdVet);
    }

    @Test
    void deleteVetById_shouldInvokeDeleteOnce() {
        //given
        doNothing().when(repository).deleteById(anyLong());
        //when
        manager.deleteVetById(1L);
        //then
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void checkAvailability_shouldReturnTrue() {
        //given
        firstVet.getVisits().add(firstVisit);
        //when
        boolean result = manager.checkAvailability(secondVisit);
        //then
        assertTrue(result);
    }

    @Test
    void checkAvailability_shouldReturnFalse() {
        //given
        firstVet.getVisits().add(firstVisit);
        firstVet.getVisits().add(secondVisit);
        //when
        boolean result = manager.checkAvailability(thirdVisit);
        //then
        assertFalse(result);
    }

    @Test
    void addVisit_shouldAddVisit() {
        //given
        firstVet.getVisits().add(firstVisit);
        //when
        manager.addVisit(secondVisit);
        //then
        assertEquals(2, firstVet.getVisits().size());
    }
}
