package com.gifthub.gifticon.repository.storage;

import com.gifthub.admin.dto.QStorageAdminListDto;
import com.gifthub.admin.dto.StorageAdminListDto;
import com.gifthub.gifticon.dto.GifticonStorageListDto;
import com.gifthub.gifticon.dto.QGifticonStorageListDto;
import com.gifthub.gifticon.entity.GifticonStorage;
import com.gifthub.gifticon.enumeration.StorageStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gifthub.gifticon.entity.QGifticonImage.gifticonImage;
import static com.gifthub.gifticon.entity.QGifticonStorage.*;
import static com.gifthub.gifticon.enumeration.StorageStatus.ADMIN_APPROVAL;
import static com.gifthub.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class GifticonStorageRepositoryImpl implements GifticonStorageRepositorySupport {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GifticonStorage> findStorageByUserId(Long userId, Pageable pageable) {
        List<GifticonStorage> content = getGifticonStorageList(userId, pageable);
        Long count = getGifticonStorageListCount(userId);

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Long getCountByAdminApproval() {
        return queryFactory
                .select(gifticonStorage.count())
                .from(gifticonStorage)
                .where(gifticonStorage.storage_status.eq(ADMIN_APPROVAL))
                .fetchOne();
    }

    @Override
    public Page<StorageAdminListDto> findStorageByStorageStatus(StorageStatus status, Pageable page) {
        List<StorageAdminListDto> content = getStorageListByStatus(status, page);
        Long count = getStorageCountByStatus(status);

        return new PageImpl<>(content, page, count);

    }

    private List<StorageAdminListDto> getStorageListByStatus(StorageStatus status, Pageable pageable) {
        return queryFactory
                .select(new QStorageAdminListDto(
                        gifticonStorage.id,
                        gifticonStorage.barcode,
                        gifticonStorage.due,
                        gifticonStorage.brandName,
                        gifticonStorage.productName,
                        gifticonStorage.user.name,
                        gifticonStorage.gifticonImage.accessUrl,
                        gifticonStorage.storage_status,
                        gifticonStorage.price,
                        gifticonStorage.modifiedDate))
                .from(gifticonStorage)
                .where(gifticonStorage.storage_status.eq(status))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .leftJoin(gifticonStorage.user, user)
                .leftJoin(gifticonStorage.gifticonImage, gifticonImage)
                .orderBy(gifticonStorage.modifiedDate.desc())
                .fetch();
    }

    private Long getStorageCountByStatus(StorageStatus status) {
        return queryFactory
                .select(gifticonStorage.count())
                .from(gifticonStorage)
                .where(gifticonStorage.storage_status.eq(status))
                .fetchOne();
    }

    @Override
    public Page<GifticonStorageListDto> findGifticonStorageDtoByUserId(Long userId, Pageable pageable) {
        List<GifticonStorageListDto> content = getGifticonStoargeList(userId, pageable);
        Long count = getGifticonStorageListCount(userId);

        return new PageImpl<>(content, pageable, count);
    }

    private List<GifticonStorageListDto> getGifticonStoargeList(Long userId, Pageable pageable) {
        return queryFactory
                .select(new QGifticonStorageListDto(
                        gifticonStorage.id,
                        gifticonStorage.brandName,
                        gifticonStorage.productName,
                        gifticonStorage.barcode,
                        gifticonStorage.due,
                        gifticonStorage.price,
                        gifticonStorage.storage_status,
                        gifticonStorage.gifticonImage.accessUrl))
                .from(gifticonStorage)
                .where(gifticonStorage.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .leftJoin(gifticonStorage.user, user)
                .leftJoin(gifticonStorage.gifticonImage, gifticonImage)
                .fetch();
    }

    private List<GifticonStorage> getGifticonStorageList(Long userId, Pageable pageable) {
        return queryFactory.select(gifticonStorage).from(gifticonStorage).where(gifticonStorage.user.id.eq(userId)).offset(pageable.getOffset()).limit(pageable.getPageSize())
                .leftJoin(gifticonStorage.user, user)
                .leftJoin(gifticonStorage.gifticonImage, gifticonImage)
                .fetchJoin().fetch();

    }

    private Long getGifticonStorageListCount(Long userId) {
        return queryFactory
                .select(gifticonStorage.count())
                .from(gifticonStorage)
                .where(gifticonStorage.user.id.eq(userId))
                .fetchOne();
    }
}
