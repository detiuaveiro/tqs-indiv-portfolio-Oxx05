package tqs.lab3meals.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.lab3meals.boundary.RESERVATION_STATE;
import tqs.lab3meals.data.MealsBookingRequest;
import tqs.lab3meals.data.MealRepository;
import java.util.List;
import java.util.Optional;

@Service
public class MealService {
    private final MealRepository mealRepository;

    @Autowired
    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }


    public void deleteMeal(String mealId){
        mealRepository.deleteByToken(mealId);
    }

    public Optional<MealsBookingRequest> getMealsBookingRequest (String mealId){
        return mealRepository.findByToken(mealId);
    }

    public List<MealsBookingRequest> getMealsBookingRequests(){
        return mealRepository.findAll();
    }

    public boolean existsMeal(String mealId){
        return mealRepository.existsMealsBookingRequestByToken(mealId);
    }

    public MealsBookingRequest saveMeal(MealsBookingRequest meal){
        if (mealRepository.existsMealsBookingRequestByToken(meal.getToken())) {
            return null;
        }

        int hour = meal.getDate().getHour();
        int minute = meal.getDate().getMinute();

        boolean isLunch = (hour == 12 || hour == 13);
        boolean isDinner = (hour == 19 || hour == 20);

        if (meal.getDate().isBefore(java.time.LocalDateTime.now()) ||
                !(isLunch || isDinner)) {
            return null;
        }

        if (mealRepository.countByDateAndHourInterval(meal.getDate().toLocalDate(), isLunch? 12:19, isLunch? 14:21) > 10) {
            return null;
        }

        return mealRepository.save(meal);
    }

    public void updateMeal(MealsBookingRequest meal){
        if (mealRepository.existsMealsBookingRequestByToken(meal.getToken())) {
            mealRepository.save(meal);
        }
    }

    public boolean checkIn(MealsBookingRequest meal){
        if (mealRepository.existsMealsBookingRequestByToken(meal.getToken())) {
            if (meal.getState() != RESERVATION_STATE.RESERVED) {
                return false;
            }
            meal.setState(RESERVATION_STATE.USED);
            mealRepository.save(meal);
            return true;
        }
        return false;
    }
}
