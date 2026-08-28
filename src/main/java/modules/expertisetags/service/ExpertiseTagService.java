package modules.expertisetags.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import modules.expertisetags.entity.ExpertiseTag;
import modules.expertisetags.enums.EnumExpertiseTagError;
import modules.expertisetags.exception.ExpertiseTagException;
import modules.expertisetags.mapper.ExpertiseTagMapper;
import modules.expertisetags.dto.ExpertiseTagResponse;
import modules.expertisetags.repository.ExpertiseTagRepository;
import modules.expertisetags.dto.CreateExpertiseTagRequest;

@Service
@RequiredArgsConstructor
public class ExpertiseTagService {

    private final ExpertiseTagRepository expertiseTagRepository;
    private final ExpertiseTagMapper expertiseTagMapper;

    @Transactional
    public ExpertiseTagResponse createExpertiseTagService(CreateExpertiseTagRequest expertiseTagRequest) {
        if (expertiseTagRepository.existsByName(expertiseTagRequest.getName())) {
            throw new ExpertiseTagException(EnumExpertiseTagError.EXPERTISE_TAG_NAME_EXISTS);
        }

        ExpertiseTag expertiseTag = expertiseTagRepository.save(
                ExpertiseTag.builder()
                        .name(expertiseTagRequest.getName())
                        .status(true)
                        .build());
        return expertiseTagMapper.toResponse(expertiseTag);
    }
}
