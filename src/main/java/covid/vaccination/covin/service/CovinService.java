package covid.vaccination.covin.service;

import covid.vaccination.covin.client.CovinClient;
import covid.vaccination.covin.client.IFTTTClient;
import covid.vaccination.covin.model.Centre;
import covid.vaccination.covin.model.CovinResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is the service layer of Covid Vaccination Availability Service.
 */
@Service
@Slf4j
public class CovinService {

    private final CovinClient covinClient;
    private final IFTTTClient iftttClient;


    public CovinService(CovinClient covinClient, IFTTTClient iftttClient) {
        this.covinClient = covinClient;
        this.iftttClient = iftttClient;
    }

    /**
     * This method will retrieve the list of all vaccination centres for the provided input and filter the data
     * as per the user's requirement.
     * @param districtId - The ID of the district
     * @param age - The age of the user.
     * @param date - The date to which the search needs to be made.
     * @param onlyAvailableSlot - True - will filter and display only the vaccination centres that has available slots.
     * @return - List of vaccination centres that has been filtered out based on the users criteria.
     */
    public CovinResponse getVaccineAvailability(Integer districtId, Integer age, String date, Boolean onlyAvailableSlot) {
        CovinResponse covinResponse = covinClient.getVaccineAvailability(districtId,date);
        if(null!= covinResponse) {
            List<Centre> centreList = covinResponse.getCentres();
            if(centreList.size()>0) {
                if (onlyAvailableSlot) {
                    centreList = centreList.stream()
                            .filter(s -> s.getSessions().stream().allMatch(avl -> avl.getAvailable_capacity() > 0))
                            .collect(Collectors.toList());
                }

                List<Centre> finalCentreList = centreList.stream()
                        .filter(s -> s.getSessions().stream().allMatch(avl -> (getMinAgeLimit(age) == avl.getMin_age_limit())))
                        .collect(Collectors.toList());

                /**
                 * Send notification to the user only if there are available slots for vaccination in any centre.
                 */
                if (finalCentreList.size() > 0) {
                    sendNotification(finalCentreList);
                }
                covinResponse.setCentres(finalCentreList);
                log.info("The centre details received from aarogya setu  {}", covinResponse.getCentres());
            }
        }
        return covinResponse;
    }


    /**
     * Logic to get the min_age_limit from the user's age
     * @param age - The age provided by the user
     * @return - The min_age_limit
     */
    private Integer getMinAgeLimit(Integer age){
        Integer min_age_limit = 0;
        if( age >= 18 && age < 45){
            min_age_limit = 18;
        }else if(age  >=45){
            min_age_limit = 45;
        }
        return min_age_limit;
    }

    /**
     * This method will make a call to the iftttClient with the list of postal codes
     * of the available vaccination centres.
     * @param finalCentreList - The list of vaccination centres.
     */
    private void sendNotification(List<Centre> finalCentreList){
        List<Integer> postcodes = new ArrayList<>();
        finalCentreList.forEach(s -> postcodes.add(s.getPincode()));
        log.info("Postcodes : {}", postcodes.toString());
        iftttClient.sendNotification(postcodes.toString());
    }
}
