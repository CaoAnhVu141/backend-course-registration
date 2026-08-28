package modules.expertisetags.mapper;

import modules.expertisetags.dto.ExpertiseTagResponse;
import modules.expertisetags.entity.ExpertiseTag;
import org.springframework.stereotype.Component;

@Component
public class ExpertiseTagMapper {
    public ExpertiseTagResponse toResponse(ExpertiseTag expertiseTag) {
        return ExpertiseTagResponse.builder()
                .id(expertiseTag.getId())
                .name(expertiseTag.getName())
                .status(expertiseTag.isStatus())
                .build();
    }
}
