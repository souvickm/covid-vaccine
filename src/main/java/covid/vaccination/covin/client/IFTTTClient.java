package covid.vaccination.covin.client;

import covid.vaccination.covin.model.CovinResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * This is the External endpoint call to notify the user the details of vaccination centre from CoWin portal.
 * The user will be notified only if user has a valid IFTTT account [Key and notification details]
*/
@Slf4j
@Component
public class IFTTTClient {

    @Value("${ifttt.baseUrl}")
    private String baseUrl;

    @Value("${ifttt.triggerFunction}")
    private String triggerFunction;

    @Value("${ifttt.key}")
    private String key;

    @Value("${ifttt.triggerPath}")
    private String triggerPath;

    @Value("${ifttt.keyPath}")
    private String keyPath;

    private final RestTemplate iftttRestTemplate;

    public IFTTTClient(@Qualifier("covinRestClient") RestTemplate iftttRestTemplate) {
        this.iftttRestTemplate = iftttRestTemplate;
    }

    /**
     * This method will notify the details of the list of postal codes of the vacciantion centers along with the
     * URL to the self registration portal for CoWin to the users IFTTT app.
     * @param postCode - List of postal codes for the corresponding available centres.
     */
    public void sendNotification(String postCode) {

        if (null == triggerFunction || triggerFunction.isEmpty() || null== key || key.isEmpty() ){
            log.warn("Please add your IFTTT key and notify function to get notified in your phone");
            return;
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
            body.add("value1", postCode);
            StringBuilder iftttUrl = new StringBuilder(baseUrl)
                    .append(triggerPath).append(triggerFunction).append(keyPath).append(key);
            URIBuilder uri = new URIBuilder(iftttUrl.toString());
            HttpEntity<Object> entity = new HttpEntity<Object>(body, headers);
            ResponseEntity<String> response = iftttRestTemplate.exchange(uri.build(), HttpMethod.POST, entity, String.class);
            log.info("Successfully sent notification");
        }catch (Exception e){
            log.error("There is an error while sending notification to mobile {}", e.getMessage());
        }
    }
}
