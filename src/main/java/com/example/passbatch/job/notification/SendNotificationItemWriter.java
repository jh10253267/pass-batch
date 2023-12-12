package com.example.passbatch.job.notification;

import com.example.passbatch.adapter.message.KakaoTalkMessageAdapter;
import com.example.passbatch.repository.notification.NotificationEntity;
import com.example.passbatch.repository.notification.NotificationRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Component
public class SendNotificationItemWriter implements ItemWriter<NotificationEntity> {
    private final NotificationRepository notificationRepository;
    private final KakaoTalkMessageAdapter kakaoTalkMessageAdapter;

    public SendNotificationItemWriter(NotificationRepository notificationRepository, KakaoTalkMessageAdapter kakaoTalkMessageAdapter) {
        this.notificationRepository = notificationRepository;
        this.kakaoTalkMessageAdapter = kakaoTalkMessageAdapter;
    }

    @Override
    public void write(List<? extends NotificationEntity> notificationEntities) throws Exception {
        int count = 0;

        for (NotificationEntity notificationEntity : notificationEntities) {
            boolean successful = kakaoTalkMessageAdapter.sendKakaoTalkMessage(notificationEntity.getUuid(), notificationEntity.getText());

            if (successful) {
                notificationEntity.setSent(true);
                notificationEntity.setSentAt(LocalDateTime.now());
                notificationRepository.save(notificationEntity);
                count++;
            }

        }
        log.info("SendNotificationItemWriter - write: 수업 전 알람 {}/{}건 전송 성공", count, notificationEntities.size());

    }

}
