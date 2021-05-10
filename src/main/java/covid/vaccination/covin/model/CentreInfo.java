package covid.vaccination.covin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CentreInfo {

    public int pincode;
    public String name;
    public String district_name;
    public String date;
}
