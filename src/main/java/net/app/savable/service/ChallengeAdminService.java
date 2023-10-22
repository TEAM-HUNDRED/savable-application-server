package net.app.savable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.app.savable.domain.challenge.ChallengeRepository;
import net.app.savable.domain.challenge.dto.ChallengeResponseDto;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChallengeAdminService {
    private final ChallengeRepository challengeRepository;

    public List<ChallengeResponseDto> getChallenges() {
        return challengeRepository.findAll(Sort.by(Sort.Order.asc("id"))).stream()
                .map(ChallengeResponseDto::new)
                .toList();
    }
}
