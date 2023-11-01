package net.app.savable.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchedulerEventService {

    @PersistenceContext
    private EntityManager entityManager;

    @Scheduled(cron = "59 3 8 * * *") // 매일 7시 59분 30초에 실행
    @Transactional(readOnly = false)
    public void addEventReward(){
        log.info("SchedulerEventService.addEventReward() 실행");
        Query query = entityManager.createNativeQuery("with satisfied_challenge AS " +
                "(SELECT m.id, m.reward, m.event_completed_at " +
                "FROM participation_challenge pc join member m on pc.member_id=m.id " +
                "where m.event_completed_at is null " +
                "and m.account_state = 'ACTIVE' " +
                "and pc.participation_state='SUCCESS' " +
                "and pc.verification_goal>=3) " +
                "UPDATE member SET reward=reward+1000, " +
                "event_completed_at=CURRENT_TIMESTAMP " +
                "where id in (select id from satisfied_challenge)");
        query.executeUpdate();
    }
}
