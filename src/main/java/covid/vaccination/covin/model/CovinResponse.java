package covid.vaccination.covin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * DTO object for the CovinClient Respone.
 */
@Getter
@Setter
@ToString
public class CovinResponse {
    @JsonProperty(value = "centers")
    public List<Centre> centres;
}
