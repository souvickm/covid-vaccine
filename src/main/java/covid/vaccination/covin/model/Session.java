package covid.vaccination.covin.model;

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
public class Session{
    public String session_id;
    public String date;
    public int available_capacity;
    public int min_age_limit;
    public String vaccine;
    public List<String> slots;
}

