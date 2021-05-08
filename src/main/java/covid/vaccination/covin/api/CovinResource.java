package covid.vaccination.covin.api;

import covid.vaccination.covin.model.CovinResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import covid.vaccination.covin.service.CovinService;

@RestController
@Slf4j
@RequestMapping("api")
public class CovinResource {

    public static final String AVAILIBILITY = "/v1/availability";

    @Autowired
    private CovinService covinService;


    @ResponseBody
    @GetMapping(value = AVAILIBILITY)
    public ResponseEntity<CovinResponse> getVaccineAvailability(@RequestParam int districtId,
                                                                @RequestParam int age,
                                                                @RequestParam boolean onlyAvailableSlot) {
        log.info("Infos received : district id {}, state id {}, age {}, onlyAvailableSlot {} ", districtId, age, onlyAvailableSlot);
        //todo validation
        CovinResponse covinResponse = covinService.getVaccineAvailability(districtId, age, onlyAvailableSlot);
        return new ResponseEntity<>(covinResponse, HttpStatus.OK);
    }


}
