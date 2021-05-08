package covid.vaccination.covin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CovinResponse {
    public List<Center> centers;
}
