package tqs.lab3meals.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.lab3meals.data.MealsBookingRequest;
import tqs.lab3meals.data.MealRepository;
import java.util.List;
import java.util.Optional;

@Service
public class MealService {

    @Autowired
    private MealRepository mealRepository;


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
        return mealRepository.save(meal);
    }

    public void updateMeal(MealsBookingRequest meal){
        if (mealRepository.existsMealsBookingRequestByToken(meal.getToken())) {
            mealRepository.save(meal);
        }
    }
}
