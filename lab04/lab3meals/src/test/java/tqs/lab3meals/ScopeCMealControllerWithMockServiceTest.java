package tqs.lab3meals;



import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tqs.lab3meals.boundary.MealRestController;
import tqs.lab3meals.data.MealsBookingRequest;
import tqs.lab3meals.service.MealService;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * WebMvcTest loads a simplified web environment for the tests. Note that the normal
 * auto-discovery of beans (and dependency injection) is limited
 * This strategy deploys the required components to a test-friendly web framework, that can be accessed
 * by injecting a MockMvc reference
 */

@WebMvcTest(MealRestController.class)
public class ScopeCMealControllerWithMockServiceTest {


    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MealService service;

    @Test
    void whenPostMeal_thenCreateMeal( ) throws Exception {
        MealsBookingRequest meal = new MealsBookingRequest(LocalDateTime.of(2024, Month.JUNE , 20, 13, 0), "alex");

        when( service.saveMeal(Mockito.any()) ).thenReturn(meal);

        mvc.perform(
                        post("/api/meals").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(meal)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is("alex")));

        verify(service, times(1)).saveMeal(Mockito.any());

    }

    @Test
    void givenManyMeals_whenGetMeals_thenReturnJsonArray() throws Exception {
        MealsBookingRequest alex = new MealsBookingRequest(LocalDateTime.of(2024, Month.JUNE , 20, 13, 0),"alex");
        MealsBookingRequest john = new MealsBookingRequest(LocalDateTime.of(2024, Month.JUNE , 20, 13, 0), "john");
        MealsBookingRequest bob = new MealsBookingRequest(LocalDateTime.of(2024, Month.JUNE , 20, 13, 0), "bob");

        List<MealsBookingRequest> allMeals = Arrays.asList(alex, john, bob);

        when( service.getMealsBookingRequests()).thenReturn(allMeals);

        mvc.perform(
                        get("/api/meals").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].userId", is(alex.getUserId())))
                .andExpect(jsonPath("$[1].userId", is(john.getUserId())))
                .andExpect(jsonPath("$[2].userId", is(bob.getUserId())));

        verify(service, times(1)).getMealsBookingRequests();
    }
}
