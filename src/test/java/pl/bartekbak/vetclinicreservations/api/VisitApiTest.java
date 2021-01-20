package pl.bartekbak.vetclinicreservations.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.bartekbak.vetclinicreservations.entity.Vet;
import pl.bartekbak.vetclinicreservations.entity.Visit;
import pl.bartekbak.vetclinicreservations.exceptions.DataCollisionException;
import pl.bartekbak.vetclinicreservations.exceptions.IncompleteDataException;
import pl.bartekbak.vetclinicreservations.exceptions.InvalidCredentialsException;
import pl.bartekbak.vetclinicreservations.exceptions.ResourceNotFoundException;
import pl.bartekbak.vetclinicreservations.manager.RestResponseExceptionHandler;
import pl.bartekbak.vetclinicreservations.manager.VisitManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class VisitApiTest {

    @Mock
    private VisitManager manager;

    @InjectMocks
    private VisitApi controller;

    private List<Visit> visits;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Vet firstVet = Vet.builder()
            .id(1L)
            .build();

    private Visit firstVisit = Visit.builder()
            .id(1L)
            .vet(firstVet)
            .date(LocalDate.of(2021, 05, 04))
            .time(LocalTime.of(11, 30))
            .build();
    private Visit secondVisit = Visit.builder()
            .id(2L)
            .vet(firstVet)
            .date(LocalDate.of(2021, 05, 05))
            .time(LocalTime.of(11, 50))
            .build();


    @BeforeEach
    void setUp() {
        visits = List.of(firstVisit, secondVisit);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new RestResponseExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getVisits_shouldReturnVisits() throws Exception {
        //given
        when(manager.findAll()).thenReturn(visits);
        //when
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/visits/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //then
        final List<Visit> result = objectMapper
                .readValue(mvcResult.getResponse().getContentAsByteArray(), new TypeReference<List<Visit>>() {
                });
        assertEquals(visits, result);
    }

    @Test
    void getVisitById_shouldReturnFirstVisit() throws Exception {
        //given
        when(manager.findById(anyLong())).thenReturn(firstVisit);
        //when
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/visits?id=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //then
        final Visit result = objectMapper
                .readValue(mvcResult.getResponse().getContentAsByteArray(), new TypeReference<Visit>() {
                });
        assertEquals(firstVisit, result);
    }

    @Test
    void getVetVisitsForDate_shouldReturnVisits() throws Exception {
        //given
        when(manager.findVisitsOfVetInDate(anyLong(), any(LocalDate.class))).thenReturn(visits);
        LocalDate date = LocalDate.of(2021, 05, 04);
        //when
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/visits/vet/1")
                        .content(objectMapper.writeValueAsString(date))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //then
        final List<Visit> result = objectMapper
                .readValue(mvcResult.getResponse().getContentAsByteArray(), new TypeReference<List<Visit>>() {
                });
        assertEquals(visits, result);
        verify(manager, times(1)).findVisitsOfVetInDate(1L, date);
    }

    @Test
    void addVisit_shouldInvokePostSaveVisitOnce() throws Exception {
        //given
        when(manager.addVisit(any(Visit.class))).thenReturn("ok");
        //when
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/visits")
                        .content(objectMapper.writeValueAsString(firstVisit))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //then
        verify(manager, times(1)).addVisit(any(Visit.class));
    }

    @Test
    void updateVisit_shouldInvokePutSaveVisitOnce() throws Exception {
        //given
        when(manager.updateVisit(any(Visit.class))).thenReturn("ok");
        //when
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/visits")
                        .content(objectMapper.writeValueAsString(firstVisit))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //then
        verify(manager, times(1)).updateVisit(any(Visit.class));
    }

    @Test
    void updateVisit_shouldInvokePutSaveVisitOnceAndReturn400Status() throws Exception {
        //given
        when(manager.updateVisit(any(Visit.class))).thenThrow(IncompleteDataException.class);
        //when
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/visits")
                        .content(objectMapper.writeValueAsString(firstVisit))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andReturn();
        //then
        verify(manager, times(1)).updateVisit(any(Visit.class));
    }

@Test
    void updateVisit_shouldInvokePutSaveVisitOnceAndReturn401Status() throws Exception {
        //given
        when(manager.updateVisit(any(Visit.class))).thenThrow(InvalidCredentialsException.class);
        //when
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/visits")
                        .content(objectMapper.writeValueAsString(firstVisit))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401))
                .andReturn();
        //then
        verify(manager, times(1)).updateVisit(any(Visit.class));
    }

@Test
    void updateVisit_shouldInvokePutSaveVisitOnceAndReturn404Status() throws Exception {
        //given
        when(manager.updateVisit(any(Visit.class))).thenThrow(ResourceNotFoundException.class);
        //when
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/visits")
                        .content(objectMapper.writeValueAsString(firstVisit))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andReturn();
        //then
        verify(manager, times(1)).updateVisit(any(Visit.class));
    }

@Test
    void updateVisit_shouldInvokePutSaveVisitOnceAndReturn409Status() throws Exception {
        //given
        when(manager.updateVisit(any(Visit.class))).thenThrow(DataCollisionException.class);
        //when
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/visits")
                        .content(objectMapper.writeValueAsString(firstVisit))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(409))
                .andReturn();
        //then
        verify(manager, times(1)).updateVisit(any(Visit.class));
    }

    @Test
    void deleteVisit() throws Exception {
        //given
        when(manager.deleteVisit(any(Visit.class))).thenReturn("ok");
        //when
        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/visits")
                        .content(objectMapper.writeValueAsString(firstVisit))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //then
        verify(manager, times(1)).deleteVisit(any(Visit.class));
    }
}
