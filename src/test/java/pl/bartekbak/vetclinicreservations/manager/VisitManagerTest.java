package pl.bartekbak.vetclinicreservations.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bartekbak.vetclinicreservations.entity.Vet;
import pl.bartekbak.vetclinicreservations.entity.Visit;
import pl.bartekbak.vetclinicreservations.repository.VisitRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
    List<Visit> enhancedVisits;

    //region setup
    @BeforeEach
    void setUp() {
        firstVet = Vet.builder()
                .id(1L)
                .firstName("Firs")
                .visits(new ArrayList<>())
                .build();

        firstVisit = Visit.builder()
                .id(1L)
                .vet(firstVet)
                .customerId(1234)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 00))
                .pin(4567)
                .build();
        secondVisit = Visit.builder()
                .id(2L)
                .vet(firstVet)
                .customerId(1235)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 20))
                .pin(4568)
                .build();
        thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId(1236)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 40))
                .pin(4569)
                .build();

        firstVet.getVisits().add(firstVisit);
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
    public void findVisitsOfVetInDate() {
        
    }
    //endregion

    //region addVisit
    @Test
    void addVisit_shouldReturnDateIsUnavailable() {
        //given
        when(vetManager.checkAvailability(any(Visit.class))).thenReturn(false);
        //when
        String result = manager.addVisit(thirdVisit);
        //then
        assertEquals("Date is unavailable", result);
    }

    @Test
    void addVisit_shouldReturnYouNeedToChooseVet() {
        //given
        Visit fourthVisit = Visit.builder()
                .customerId(1236)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 10))
                .build();
        //when
        String result = manager.addVisit(fourthVisit);
        //then
        assertEquals("You need to choose Vet", result);
    }

    @Test
    void addVisit_shouldReturnYouNeedToSpecifyDate() {
        //given
        Visit fourthVisit = Visit.builder()
                .vet(firstVet)
                .customerId(1236)
                .time(LocalTime.of(12, 10))
                .build();
        //when
        String result = manager.addVisit(fourthVisit);
        //then
        assertEquals("You need to specify date", result);
    }

    @Test
    void addVisit_shouldReturnYouNeedToSpecifyTime() {
        //given
        Visit fourthVisit = Visit.builder()
                .vet(firstVet)
                .customerId(1236)
                .date(LocalDate.of(2020, 5, 5))
                .build();
        //when
        String result = manager.addVisit(fourthVisit);
        //then
        assertEquals("You need to specify Time", result);
    }

    @Test
    void addVisit_shouldReturnInvalidUser() {
        //given
        Visit fourthVisit = Visit.builder()
                .vet(firstVet)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 10))
                .build();
        //when
        String result = manager.addVisit(thirdVisit);
        //then
        assertEquals("Invalid user", result);
    }

    @Test
    void addVisit_shouldReturnPin() {
        //given
        Visit fourthVisit = Visit.builder()
                .vet(firstVet)
                .customerId(1236)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 10))
                .build();
        when(vetManager.checkAvailability(any(Visit.class))).thenReturn(true);
        //when
        String result = manager.addVisit(thirdVisit);
        //then
        assertEquals("Pin is:", result.substring(0, 6));
        assertEquals(12, result.length());
    }
    //endregion

    //region updateVisit
    @Test
    void updateVisit_shouldReturnNoSuchVisitInDatabase() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(55L)
                .vet(firstVet)
                .customerId(1236)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 10))
                .pin(4569)
                .build();
        //when
        String result = manager.updateVisit(thirdVisit);
        //then
        assertEquals("No such visit in database", result);
    }

    @Test
    void updateVisit_shouldReturnDateIsUnavailable() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId(1236)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 10))
                .pin(4569)
                .build();
        when(vetManager.checkAvailability(thirdVisit)).thenReturn(false);
        //when
        String result = manager.updateVisit(thirdVisit);
        //then
        assertEquals("Date is unavailable", result);
    }

    @Test
    void updateVisit_shouldReturnYouNeedToChooseVet() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .customerId(1236)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 10))
                .pin(4569)
                .build();
        //when
        String result = manager.updateVisit(thirdVisit);
        //then
        assertEquals("You need to choose Vet", result);
    }

    @Test
    void updateVisit_shouldReturnYouNeedToSpecifyDate() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId(1236)
                .time(LocalTime.of(12, 10))
                .pin(4569)
                .build();
        //when
        String result = manager.updateVisit(thirdVisit);
        //then
        assertEquals("You need to specify date", result);
    }

    @Test
    void updateVisit_shouldReturnYouNeedToSpecifyTime() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId(1236)
                .date(LocalDate.of(2020, 5, 5))
                .pin(4569)
                .build();
        //when
        String result = manager.updateVisit(thirdVisit);
        //then
        assertEquals("You need to specify Time", result);
    }

    @Test
    void updateVisit_shouldReturnInvalidPin() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId(1236)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 10))
                .pin(9999999)
                .build();
        //when
        String result = manager.updateVisit(thirdVisit);
        //then
        assertEquals("Invalid Pin", result);
    }

    @Test
    void updateVisit_shouldReturnInvalidUser() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId(9991236)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 10))
                .pin(1324)
                .build();
        //when
        String result = manager.updateVisit(thirdVisit);
        //then
        assertEquals("Invalid user Id", result);
    }

    @Test
    void updateVisit_shouldReturnNewPin() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId(1236)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 10))
                .pin(1234)
                .build();
        when(vetManager.checkAvailability(thirdVisit)).thenReturn(true);
        //when
        String result = manager.updateVisit(thirdVisit);
        //then
        assertEquals("Visit updated. New pin is: ", result.substring(0,26));
        assertEquals(31, result.length());
    }
    //endregion

    //region deleteVisit
    @Test
    void deleteVisitById_shouldReturnSuccessfullyDeleted() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId(1236)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 10))
                .pin(4569)
                .build();
        doNothing().when(repository).deleteById(anyLong());
        //when
        String result = manager.deleteVisitById(3L);
        //then
        assertEquals("Successfully deleted", result);
    }

    @Test
    void deleteVisitById_shouldReturnNoSuchVisitInDatabase() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(15L)
                .vet(firstVet)
                .customerId(1236)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 10))
                .pin(4569)
                .build();
        //when
        String result = manager.deleteVisitById(3L);
        //then
        assertEquals("No such visit in database", result);
    }

    @Test
    void deleteVisitById_shouldReturnInvalidId() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId(324234)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 10))
                .pin(4569)
                .build();
        //when
        String result = manager.deleteVisitById(3L);
        //then
        assertEquals("Invalid user Id", result);
    }

    @Test
    void deleteVisitById_shouldReturnInvalidPin() {
        //given
        Visit thirdVisit = Visit.builder()
                .id(3L)
                .vet(firstVet)
                .customerId(1236)
                .date(LocalDate.of(2020, 5, 5))
                .time(LocalTime.of(12, 10))
                .pin(999999999)
                .build();
        //when
        String result = manager.deleteVisitById(3L);
        //then
        assertEquals("Invalid Pin", result);
    }
    //endregion
}
