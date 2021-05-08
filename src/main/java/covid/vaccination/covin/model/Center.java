package covid.vaccination.covin.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.util.List;

@Getter
@Setter
@ToString
public class Center {
    public int center_id;
    public String name;
    public String address;
    public String state_name;
    public String district_name;
    public String block_name;
    public int pincode;
    public int lat;
    @JsonProperty("long")
    public int lng;
    public String from;
    public String to;
    public String fee_type;
    public List<Session> sessions;
    public List<VaccineFee> vaccine_fees;
}
