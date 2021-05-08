package covid.vaccination.covin.service;

import covid.vaccination.covin.client.CovinClient;
import covid.vaccination.covin.model.Center;
import covid.vaccination.covin.model.CovinResponse;
import covid.vaccination.covin.model.Session;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CovinService {

    private final CovinClient covinClient;
    private final int AGE_CHECK_CONSTANT = 26;

    public CovinService(CovinClient covinClient) {
        this.covinClient = covinClient;
    }

    public CovinResponse getVaccineAvailability(int districtId, int age, boolean onlyAvailableSlot) {
        //External Call
        CovinResponse covinResponse = covinClient.getVaccineAvailability(districtId);

        List<Center> centerList = covinResponse.getCenters();
        if(onlyAvailableSlot) {
             centerList = centerList.stream()
                    .filter(s -> s.getSessions().stream().allMatch(avl -> avl.getAvailable_capacity() > 0))
                    .collect(Collectors.toList());
        }

        //min_age_limit
        List<Center> finalCenterList = centerList.stream()
                .filter(s -> s.getSessions().stream().allMatch(avl -> (getMinAgeLimit(age) == avl.getMin_age_limit())))
                .collect(Collectors.toList());

        covinResponse.setCenters(finalCenterList);
        log.debug("The response from aarogya setu is {}", covinResponse);
        return covinResponse;
    }


    private int getMinAgeLimit(int age){
        int min_age_limit = 0;
        if( age >= 18 && age < 45){
            min_age_limit = 18;
        }else if(age  >=45){
            min_age_limit = 45;
        }
        return min_age_limit;
    }
}
