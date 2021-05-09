package covid.vaccination.covin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO object for the CovinClient Respone.
 */
@Getter
@Setter
@ToString
public class VaccineFee {
    public String vaccine;
    public String fee;
}
