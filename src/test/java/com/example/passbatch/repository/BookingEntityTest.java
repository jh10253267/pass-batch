package com.example.passbatch.repository;

import com.example.passbatch.repository.packaze.PackageEntity;
import com.example.passbatch.repository.packaze.PackageRepository;
import com.example.passbatch.repository.pass.PassEntity;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Log4j2
class BookingEntityTest {
    @Autowired
    private PackageRepository packageRepository;

    @Test
    void name() {
        // Given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디 챌린지 PT 12주");
        packageEntity.setPeriod(84);
        // When
        packageRepository.save(packageEntity);
        // Then
        assertThat(packageEntity.getPackageSeq());
    }

    @Test
    void test_findByCreatedAtAfter() {
        // Given
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("학생 전용 3개월");
        packageEntity.setPeriod(90);
        packageRepository.save(packageEntity);

        PackageEntity packageEntity1 = new PackageEntity();
        packageEntity1.setPackageName("학생 전용 6개월");
        packageEntity1.setPeriod(100);
        packageRepository.save(packageEntity1);

        // When
        final List<PackageEntity> packageEntities = packageRepository.findByCreatedAtAfter(dateTime, PageRequest.of(0, 1, Sort.by("packageSeq").descending()));
        // Then
        assertThat(packageEntities.size()).isEqualTo(1);
        assertThat(packageEntity1.getPackageSeq()).isEqualTo(packageEntities.get(0).getPackageSeq());
    }

    @Test
    void test_updateCountAndPeriod() {
        // Given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디프로필 이벤트 4개월");
        packageEntity.setPeriod(90);
        packageRepository.save(packageEntity);

        // When
        int updateCount = packageRepository.updateCountAndPeriod(packageEntity.getPackageSeq(), 30, 120);
        final PackageEntity updatedPackageEntity = packageRepository.findById(packageEntity.getPackageSeq()).get();

        // Then
        assertThat(updateCount).isEqualTo(1);
        assertThat(updatedPackageEntity.getCount()).isEqualTo(30);
        assertThat(updatedPackageEntity.getPeriod()).isEqualTo(120);
    }

    @Test
    void test_delete() {
        // Given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("제거할 이용권");
        packageEntity.setCount(1);
        PackageEntity newPackageEntity = packageRepository.save(packageEntity);

        // When
        packageRepository.deleteById(newPackageEntity.getPackageSeq());

        // Then
        assertThat(packageRepository.findById(newPackageEntity.getPackageSeq())).isEmpty();
    }


}