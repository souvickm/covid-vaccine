package covid.vaccination.covin.client;

import covid.vaccination.covin.model.CovinResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

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

    public CovinResponse getVaccineAvailability(int districtId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            //TODO Language
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            ResponseEntity<CovinResponse> response = covinRestTemplate.exchange(uriBuilderForGetVaccineAvailability(districtId), HttpMethod.GET, entity, CovinResponse.class);
            return response.getBody();
        }catch (Exception e){
            log.error("There is an error {}", e.getMessage());
            return null;
        }
    }

    private URI uriBuilderForGetVaccineAvailability(int districtID) {
        try {
            URIBuilder b = new URIBuilder(baseUrl + availibilityPath);
            b.addParameter("district_id", String.valueOf(districtID));
            b.addParameter("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy")));
            return b.build();
        } catch (URISyntaxException e) {
            log.error("There is an error to create uri {}", e.getMessage());
        }
        return null;
    }
}
