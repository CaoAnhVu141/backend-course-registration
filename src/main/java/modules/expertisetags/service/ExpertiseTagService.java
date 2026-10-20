package modules.expertisetags.service;

import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import modules.expertisetags.entity.ExpertiseTag;
import modules.expertisetags.enums.EnumExpertiseTagError;
import modules.expertisetags.exception.ExpertiseTagException;
import modules.expertisetags.mapper.ExpertiseTagMapper;
import modules.expertisetags.dto.ExpertiseTagResponse;
import modules.expertisetags.repository.ExpertiseTagRepository;
import modules.expertisetags.dto.CreateExpertiseTagRequest;
import modules.expertisetags.dto.UpdateExpertiseTagRequest;

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

    @Transactional(readOnly = true)
    public List<ExpertiseTagResponse> getAllExpertiseTagsService() {
        return expertiseTagRepository.findAll()
                .stream()
                .map(expertiseTagMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ExpertiseTagResponse getExpertiseTagByIdService(Long id) {
        if (!expertiseTagRepository.existsById(id)) {
            throw new ExpertiseTagException(EnumExpertiseTagError.EXPERTISE_TAG_NOT_FOUND);
        }
        ExpertiseTag expertiseTag = expertiseTagRepository.findExpertiseTagById(id);
        return expertiseTagMapper.toResponse(expertiseTag);
    }

    @Transactional
    public ExpertiseTagResponse updateExpertiseTagService(UpdateExpertiseTagRequest expertiseTagRequest, Long id) {
        ExpertiseTag expertiseTag = findExpertiseTagById(id);
        if (expertiseTag == null) {
            throw new ExpertiseTagException(EnumExpertiseTagError.EXPERTISE_TAG_NOT_FOUND);
        }
        if (!expertiseTag.getName().equals(expertiseTagRequest.getName())
                && expertiseTagRepository.existsByName(expertiseTagRequest.getName())) {
            throw new ExpertiseTagException(EnumExpertiseTagError.EXPERTISE_TAG_NAME_EXISTS);
        }
        expertiseTag.setName(expertiseTagRequest.getName());
        expertiseTagRepository.save(expertiseTag);
        return expertiseTagMapper.toResponse(expertiseTag);
    }

    @Transactional
    public void deleteExpertiseTagService(Long id) {
        ExpertiseTag expertiseTag = findExpertiseTagById(id);
        expertiseTag.setStatus(false);
        expertiseTagRepository.save(expertiseTag);
    }

    

    private ExpertiseTag findExpertiseTagById(Long id) {
        return expertiseTagRepository.findById(id)
                .orElseThrow(() -> new ExpertiseTagException(EnumExpertiseTagError.EXPERTISE_TAG_NOT_FOUND));
    }
}
