package pl.bartekbak.vetclinicreservations.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bartekbak.vetclinicreservations.entity.Vet;
import pl.bartekbak.vetclinicreservations.entity.Visit;
import pl.bartekbak.vetclinicreservations.exceptions.DataCollisionException;
import pl.bartekbak.vetclinicreservations.exceptions.IncompleteDataException;
import pl.bartekbak.vetclinicreservations.exceptions.InvalidCredentialsException;
import pl.bartekbak.vetclinicreservations.exceptions.ResourceNotFoundException;
import pl.bartekbak.vetclinicreservations.repository.VisitRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitManagerTest {

    @Mock
    private VisitRepository repository;

    @Mock
    private VetManager vetManager;

    @InjectMocks
    VisitManager manager;

    private Vet firstVet;

    private Visit firstVisit;
    private Visit secondVisit;
    private Visit thirdVisit;

    List<Visit> visits;

    //region setup
    @BeforeEach
    void setUp() {
        firstVet = Vet.builder()
                .id(1L)
                .firstName("First")
                .build();

        firstVisit = Visit.builder()
                .id(1L)
                .vet(firstVet)
                .customerId("1234")
                .date(LocalDate.of(2021, 5, 5))
                .time(LocalTime.of(12, 00))
                .pin("4567")
                .build();
        secondVisit = Visit.builder()
                .id(2L)
                .vet(firstVet)
                .customerId("1235")
                .date(LocalDate.of(2021, 5, 5))
                .time(LocalTime.of(12, 25))
                .pin("4568")
                .build();
        thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId("1236")
                .date(LocalDate.of(2021, 5, 5))
                .time(LocalTime.of(12, 40))
                .pin("4569")
                .build();

        visits = List.of(firstVisit, secondVisit);
    }
    //endregion

    //region findById
    @Test
    void findById_shouldReturnFirstVisit() {
        //given
        when(repository.findById(anyLong())).thenReturn(Optional.of(firstVisit));
        //when
        Visit result = manager.findById(1L);
        //then
        assertEquals(firstVisit, result);
    }
    //endregion

    //region findAll
    @Test
    void findAll_shouldReturnVisits() {
        //given
        when(repository.findAll()).thenReturn(visits);
        //when
        List<Visit> result = manager.findAll();
        //then
        assertEquals(visits, result);
    }
    //endregion

    //region findVisitsOfVetInDate
    @Test
    void findVisitsOfVetInDate() {
        //given
        when(repository.findAll()).thenReturn(visits);
        LocalDate date = LocalDate.of(2021, 5, 5);
        //when
        List<Visit> result = manager.findVisitsOfVetInDate(1L, date);
        //then
        assertEquals(2, result.size());
    }
    //endregion

    //region addVisit
    @Test
    void addVisit_shouldReturnDateIsUnavailable() {
        //given
        when(repository.findAll()).thenReturn(visits);
        when(vetManager.findById(anyLong())).thenReturn(firstVet);
        //when
        //then
        assertThrows(DataCollisionException.class, () -> manager.addVisit(thirdVisit));
    }

    @Test
    void addVisit_shouldReturnYouNeedToChooseVet() {
        //given
        Visit fourthVisit = Visit.builder()
                .customerId("1236")
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 40))
                .build();
        //when
        //then
        assertThrows(IncompleteDataException.class, () -> manager.addVisit(fourthVisit));
    }

    @Test
    void addVisit_shouldReturnNoSuchVetInDatabase() {
        //given
        Visit fourthVisit = Visit.builder()
                .customerId("1236")
                .vet(Vet.builder().id(900L).build())
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 40))
                .pin("1345")
                .build();
        when(vetManager.findById(anyLong())).thenReturn(null);
        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> manager.addVisit(fourthVisit));
    }

    @Test
    void addVisit_shouldReturnYouNeedToSpecifyDate() {
        //given
        Visit fourthVisit = Visit.builder()
                .vet(firstVet)
                .customerId("1236")
                .time(LocalTime.of(12, 40))
                .build();
        //when
        //then
        assertThrows(IncompleteDataException.class, () -> manager.addVisit(fourthVisit));
    }

    @Test
    void addVisit_shouldReturnYouNeedToSpecifyTime() {
        //given
        Visit fourthVisit = Visit.builder()
                .vet(firstVet)
                .customerId("1236")
                .date(LocalDate.of(2020, 5, 5))
                .build();
        //when
        //then
        assertThrows(IncompleteDataException.class, () -> manager.addVisit(fourthVisit));
    }

    @Test
    void addVisit_shouldReturnInvalidUser() {
        //given
        Visit fourthVisit = Visit.builder()
                .vet(firstVet)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 40))
                .pin("1245")
                .build();
        //when
        //then
        assertThrows(InvalidCredentialsException.class, () -> manager.addVisit(fourthVisit));
    }

    @Test
    void addVisit_shouldReturnSuccessfullyCreated() {
        //given
        Visit fourthVisit = Visit.builder()
                .vet(firstVet)
                .customerId("1236")
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 40))
                .pin("4567")
                .build();
        when(vetManager.findById(anyLong())).thenReturn(firstVet);
        //when
        String result = manager.addVisit(fourthVisit);
        //then
        assertEquals("Successfully created", result);
    }
    //endregion

    //region updateVisit
    @Test
    void updateVisit_shouldReturnNoSuchVisitInDatabase() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(55L)
                .vet(firstVet)
                .customerId("1236")
                .date(LocalDate.of(2021, 5, 5))
                .time(LocalTime.of(12, 40))
                .pin("4569")
                .build();
        when(vetManager.findById(anyLong())).thenReturn(firstVet);
        when(repository.findById(anyLong())).thenThrow(new RuntimeException("Id not found"));
        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> manager.updateVisit(thirdVisit));
    }

    @Test
    void updateVisit_shouldReturnDateIsUnavailable() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId("1236")
                .date(LocalDate.of(2021, 5, 5))
                .time(LocalTime.of(12, 40))
                .pin("4569")
                .build();
        when(vetManager.findById(anyLong())).thenReturn(firstVet);
        when(repository.findById(anyLong())).thenReturn(Optional.of(firstVisit));
        when(repository.findAll()).thenReturn(visits);
        //when
        //then
        assertThrows(DataCollisionException.class, ()-> {manager.updateVisit(thirdVisit);});
    }

    @Test
    void updateVisit_shouldReturnYouNeedToChooseVet() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .customerId("1236")
                .date(LocalDate.of(2021, 5, 5))
                .time(LocalTime.of(12, 40))
                .pin("4569")
                .build();
        //when
        //then
        assertThrows(IncompleteDataException.class, () -> manager.updateVisit(thirdVisit));
    }

    @Test
    void updateVisit_shouldReturnYouNeedToSpecifyDate() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId("1236")
                .time(LocalTime.of(12, 40))
                .pin("4569")
                .build();
        //when
        //then
        assertThrows(IncompleteDataException.class, () -> manager.updateVisit(thirdVisit));
    }

    @Test
    void updateVisit_shouldReturnYouNeedToSpecifyTime() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId("1236")
                .date(LocalDate.of(2021, 5, 5))
                .pin("4569")
                .build();
        //when
        //then
        assertThrows(IncompleteDataException.class, () -> manager.updateVisit(thirdVisit));
    }

    @Test
    void updateVisit_shouldReturnInvalidPin() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId("1236")
                .date(LocalDate.of(2021, 5, 5))
                .time(LocalTime.of(12, 40))
                .pin("9999999")
                .build();
        //when
        //then
        assertThrows(InvalidCredentialsException.class, () -> manager.updateVisit(thirdVisit));
    }

    @Test
    void updateVisit_shouldReturnInvalidUser() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId("9991236")
                .date(LocalDate.of(2021, 5, 5))
                .time(LocalTime.of(12, 40))
                .pin("1324")
                .build();
        //when
        //then
        assertThrows(InvalidCredentialsException.class, () -> manager.updateVisit(thirdVisit));
    }

    @Test
    void updateVisit_shouldReturnSuccessfullyUpdated() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId("1236")
                .date(LocalDate.of(2021, 5, 5))
                .time(LocalTime.of(12, 40))
                .pin("1234")
                .build();
        when(vetManager.findById(anyLong())).thenReturn(firstVet);
        when(repository.findById(anyLong())).thenReturn(Optional.of(thirdVisit));
        //when
        String result = manager.updateVisit(thirdVisit);
        //then
        assertEquals("Successfully updated", result);
    }
    //endregion

    //region deleteVisit
    @Test
    void deleteVisitById_shouldReturnSuccessfullyDeleted() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId("1236")
                .date(LocalDate.of(2021, 5, 5))
                .time(LocalTime.of(12, 40))
                .pin("1234")
                .build();
        doNothing().when(repository).delete(any(Visit.class));
        when(vetManager.findById(anyLong())).thenReturn(firstVet);
        when(repository.findById(anyLong())).thenReturn(Optional.of(thirdVisit));
        //when
        String result = manager.deleteVisit(thirdVisit);
        //then
        assertEquals("Successfully deleted", result);
        verify(repository, times(1)).delete(thirdVisit);
    }

    @Test
    void deleteVisitById_shouldReturnNoSuchVisitInDatabase() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(50L)
                .vet(firstVet)
                .customerId("1236")
                .date(LocalDate.of(2021, 5, 5))
                .time(LocalTime.of(12, 40))
                .pin("1234")
                .build();
        when(vetManager.findById(anyLong())).thenReturn(firstVet);
        //when
        //then
        assertThrows(ResourceNotFoundException.class, () -> manager.deleteVisit(thirdVisit));
    }

    @Test
    void deleteVisitById_shouldReturnInvalidId() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId("12367")
                .date(LocalDate.of(2021, 5, 5))
                .time(LocalTime.of(12, 40))
                .pin("1234")
                .build();
        //when
        //then
        assertThrows(InvalidCredentialsException.class, () -> manager.deleteVisit(thirdVisit));
    }

    @Test
    void deleteVisitById_shouldReturnInvalidPin() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId("1236")
                .date(LocalDate.of(2021, 5, 5))
                .time(LocalTime.of(12, 40))
                .pin("12545")
                .build();
        //when
        //then
        assertThrows(InvalidCredentialsException.class, () -> manager.deleteVisit(thirdVisit));
    }
    //endregion
}
