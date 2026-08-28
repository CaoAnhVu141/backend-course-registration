package modules.expertisetags.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExpertiseTagResponse {
    private Long id;
    private String name;
    private boolean status;
}
