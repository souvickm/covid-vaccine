package covid.vaccination.covin.api;

import covid.vaccination.covin.model.CovinResponse;
import covid.vaccination.covin.service.CovinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This class has the main API endpoint defined for checking the vaccine availability.
 */
@RestController
@Slf4j
@RequestMapping("api")
public class CovinResource {

    public static final String AVAILIBILITY_PATH = "/v1/availability";
    private static final Integer DEFAULT_DISTRICT_ID = 294;
    private static final Integer DEFAULT_AGE = 18;
    private static final Boolean DEFAULT_ONLY_AVAILABLE_SLOT = Boolean.TRUE;
    private static final String DEFAULT_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy"));
    private Integer districtId;
    private Integer age;
    private Boolean onlyAvailableSlot;
    private String date;

    @Autowired
    private CovinService covinService;

    /**
     *This endpoint will retrieve the available vaccine centres with the provided input.If not provided default values will be set.
     * @param districtId - The ID of the district user want to search of the available vaccine centres. Eg: 294 - BBMP
     * @param age - The AGE of the user to retrieve the available slots falling under that category.
     * @param date - The DATE to which user would like to search for the avaialblity. List of centres from 7 days to the DATE will be displayed.
     * @param onlyAvailableSlot - True will provide the list of centres which are avaialbe, False will display all the centres in that specific districtID for that age group.
     * @return List of all the Vaccination Centres.
     * Also List of the available postal codes will be notified along with the link to self registration portal of CoWin to your IFTTT app.
     */
    @ResponseBody
    @GetMapping(value = AVAILIBILITY_PATH)
    public ResponseEntity<CovinResponse> getVaccineAvailability(@RequestParam(required = false) Integer districtId,
                                                                @RequestParam(required = false) Integer age,
                                                                @RequestParam(required = false) String date,
                                                                @RequestParam(required = false) Boolean onlyAvailableSlot) {
        this.districtId=districtId;
        this.age=age;
        this.onlyAvailableSlot=onlyAvailableSlot;
        this.date = date;
        log.info("Info received : district id {}, age {}, date {}, onlyAvailableSlot {} ", districtId, age, date, onlyAvailableSlot);
        validateAndsetDefaultValues(districtId,age,date,onlyAvailableSlot);
        log.debug("After Validation : district id {}, age {}, date {}, onlyAvailableSlot {} ", this.districtId, this.age, this.date, this.onlyAvailableSlot);
        CovinResponse covinResponse = covinService.getVaccineAvailability(this.districtId,
                this.age, this.date, this.onlyAvailableSlot);
        return new ResponseEntity<>(covinResponse, HttpStatus.OK);
    }

    /**
     * This endpoint is internally scheduled to run every 1000ms to check the availabilty of the vaccination centres for default values.
     * Default DistrictID - set to 294
     * Default Age - set to 18
     * Default Date - set to the current date
     * Default display of only available slots - set to true
     * Returns - List of the available postal codes will be notified along with the link to self registration portal of CoWin to your IFTTT app.
     */
    @Scheduled(fixedRate = 30000)
    @ResponseBody
    @GetMapping(value = AVAILIBILITY_PATH+"/check/always")
    public void getVaccineAvailabilityScheduled() {

        log.info("values set by default : district id {}, age {}, date {}, onlyAvailableSlot {} ",
                DEFAULT_DISTRICT_ID,
                DEFAULT_AGE, DEFAULT_DATE, DEFAULT_ONLY_AVAILABLE_SLOT);
        CovinResponse covinResponse = covinService.getVaccineAvailability(DEFAULT_DISTRICT_ID,
                DEFAULT_AGE, DEFAULT_DATE, DEFAULT_ONLY_AVAILABLE_SLOT);
    }

    /**
     * Basic validation for the input parameters are done. Null or zero leads to invalid input parameter.
     * @param districtId - The ID of the corresponding District - If invalid, then set to 294 [BBMP]
     * @param age - The age of the user - If invalid, then set to 18.
     * @param date - The date to check the availability for. If invalid, then set to today's date.
     * @param onlyAvailableSlot - If invalid - then set to true to display only the avaialable slots.
     */
    private void validateAndsetDefaultValues(Integer districtId, Integer age, String date, Boolean onlyAvailableSlot){
        String dateRegex = "^([0-2][0-9]||3[0-1])-(0[0-9]||1[0-2])-([2-9][1-9])$";
        if (null == districtId || districtId == 0){
            this.districtId = DEFAULT_DISTRICT_ID;
        }
        if (null == age || age == 0){
            this.age =DEFAULT_AGE;
        }
        if(null == date || date.isEmpty() || !date.matches(dateRegex)){
            this.date = DEFAULT_DATE;
        }
        if (null == onlyAvailableSlot || !onlyAvailableSlot){
            this.onlyAvailableSlot = DEFAULT_ONLY_AVAILABLE_SLOT;
        }

    }


}
