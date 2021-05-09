package covid.vaccination.covin.client;

import covid.vaccination.covin.model.CovinResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * This is the External endpoint call to fetch the details of vaccination centre from CoWin portal.
 */
@Slf4j
@Component
public class CovinClient {

    @Value("${aarogyaSetu.baseUrl}")
    private String baseUrl;

    @Value("${aarogyaSetu.availibilityPath}")
    private String availibilityPath;

    private final RestTemplate covinRestTemplate;

    public CovinClient(@Qualifier("covinRestClient") RestTemplate covinRestTemplate) {
        this.covinRestTemplate = covinRestTemplate;
    }

    /**
     * This method will call the third party service and retrieve the list of centres available for the provided inputs.
     * @param districtId - The ID of the district.
     * @param date - The date to which the search should be made.
     * @return - The list of centers available to the user for the provided input criteria.
     */
    public CovinResponse getVaccineAvailability(Integer districtId, String date) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            //TODO Language
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            ResponseEntity<CovinResponse> response = covinRestTemplate.exchange(uriBuilderForGetVaccineAvailability(districtId, date), HttpMethod.GET, entity, CovinResponse.class);
            return response.getBody();
        }catch (Exception e){
            log.error("There is an error {}", e.getMessage());
            return null;
        }
    }

    /**
     * This is a generic method to bind the URI by combining the districtID and Date
     * @param districtID - The ID of the district.
     * @param date - The date to which the search should be made.
     * @return - Valid URI
     */
    private URI uriBuilderForGetVaccineAvailability(Integer districtID, String date) {
        try {
            URIBuilder b = new URIBuilder(baseUrl + availibilityPath);
            b.addParameter("district_id", String.valueOf(districtID));
            b.addParameter("date", date);
            return b.build();
        } catch (URISyntaxException e) {
            log.error("There is an error to create uri {}", e.getMessage());
            return null;
        }
    }
}
