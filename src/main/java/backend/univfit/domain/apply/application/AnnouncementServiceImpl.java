package backend.univfit.domain.apply.application;

import backend.univfit.domain.apply.api.dto.response.AnnouncementDetailResponse;
import backend.univfit.domain.apply.api.dto.response.AnnouncementListResponse;
import backend.univfit.domain.apply.api.dto.response.AnnouncementResponse;
import backend.univfit.domain.apply.api.dto.response.ScholarShipFoundationResponse;
import backend.univfit.domain.apply.entity.AnnouncementEntity;
import backend.univfit.domain.apply.entity.ApplyEntity;
import backend.univfit.domain.apply.entity.ConditionEntity;
import backend.univfit.domain.apply.entity.LikeEntity;
import backend.univfit.domain.apply.entity.enums.AnnouncementStatus;
import backend.univfit.domain.apply.entity.enums.ApplyStatus;
import backend.univfit.domain.apply.exception.AnnouncementException;
import backend.univfit.domain.apply.exception.ConditionException;
import backend.univfit.domain.apply.exception.LikeException;
import backend.univfit.domain.apply.exception.ScholarShipFoundationException;
import backend.univfit.domain.apply.repository.*;
import backend.univfit.domain.member.entity.Member;
import backend.univfit.domain.member.exception.MemberException;
import backend.univfit.domain.member.repository.MemberJpaRepository;
import backend.univfit.global.argumentResolver.MemberInfoObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static backend.univfit.global.error.status.ErrorStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementJpaRepository announcementJpaRepository;
    private final AnnouncementManager announcementManager;
    private final ConditionJpaRepository conditionJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ScholarShipFoundationJpaRepository scholarShipFoundationJpaRepository;
    private final ApplyJpaRepository applyJpaRepository;
    private final LikeJpaRepository likeJpaRepository;

    /**
     * 전체장학금 조회
     * 아직 지원대상인지, 지원불가인지, 판단불가인지는 안함
     *
     * @return
     */
    @Override
    public AnnouncementListResponse getAnnouncementList(List<String> statuses, MemberInfoObject memberInfoObject) {
        Long memberId = memberInfoObject.getMemberId();
        System.out.println("memberId = " + memberId);
        List<String> finalStatuses = (statuses == null || statuses.isEmpty())
                ? List.of(AnnouncementStatus.ING.name())
                : new ArrayList<>(statuses);

        List<AnnouncementResponse> list = announcementJpaRepository.findAll().stream()
                .map(ar -> {
                    ar.updateStatus(LocalDate.now());
                    announcementJpaRepository.save(ar);
                    return ar;
                })
                .filter(f -> finalStatuses.contains(f.getAnnouncementStatus().name()))
                .map(ar -> {
                    String announcementStatus = announcementManager.checkAnnouncementStatus(ar);
                    Long remainingDay = ChronoUnit.DAYS.between(LocalDate.now(), ar.getEndDocumentDate());
                    String applyPossible = announcementManager.checkEligibility(ar, memberId);

                    return AnnouncementResponse.of(ar.getId(), ar.getScholarShipImage(),
                            ar.getScholarShipName(), ar.getScholarShipFoundation(), announcementStatus,
                            ar.getApplicationPeriod(), remainingDay, applyPossible
                    );
                }).toList();

        return AnnouncementListResponse.of(list, list.size());
    }

    /**
     * 세부장학금 조회
     * @param announcementId
     * @return
     */
    @Override
    public AnnouncementDetailResponse getAnnouncement(Long announcementId, MemberInfoObject memberInfoObject) {
        Long memberId = memberInfoObject.getMemberId();
        Member member = memberJpaRepository.findById(memberId).orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        AnnouncementEntity ae = announcementJpaRepository.findById(announcementId)
                .orElseThrow(() -> new AnnouncementException(ANNOUNCEMENT_NOT_FOUND));

        ConditionEntity ce = conditionJpaRepository.findByAnnouncementEntity(ae)
                .orElseThrow(() -> new ConditionException(CONDITION_NOT_FOUND));

        ae.updateStatus(LocalDate.now());
        AnnouncementEntity announcement = announcementJpaRepository.save(ae);
        Long remainingDay = ChronoUnit.DAYS.between(LocalDate.now(), ae.getEndDocumentDate());
        String applyPossible = announcementManager.checkEligibility(ae, memberId);
        String supportAmount = ae.getSupportAmount();
        List<String> applyCondition = Arrays.stream(ae.getApplicationConditions().split("\\s*,\\s*")).toList();

        Integer likesCount = likeJpaRepository.findByAnnouncementEntity(announcement).size();
        List<LikeEntity> byMember = likeJpaRepository.findByMember(member);

        Boolean isLikedByMember = byMember.stream().anyMatch(like -> like.getAnnouncementEntity().getId().equals(announcementId));
        Boolean isStoredByMember = true;
        if(applyJpaRepository.findByMemberAndAnnouncementEntity(member, announcement)==null){
            isStoredByMember = false;
        }

        return AnnouncementDetailResponse.of(ae.getId(), ae.getScholarShipImage(), ae.getScholarShipName(), ae.getScholarShipFoundation(),
                remainingDay, applyPossible, supportAmount, ae.getApplicationPeriod(),
                ae.getHashTag(), applyCondition, ae.getDetailContents(), likesCount,
                isLikedByMember, isStoredByMember, ae.getFoundationLink());
    }

    @Override
    public ScholarShipFoundationResponse getScholarShipFoundationContents(Long announcementId) {
        AnnouncementEntity ae = announcementJpaRepository.findById(announcementId)
                .orElseThrow(() -> new AnnouncementException(ANNOUNCEMENT_NOT_FOUND));

        return ScholarShipFoundationResponse.of(scholarShipFoundationJpaRepository.findByAnnouncementEntity(ae)
                .orElseThrow(() -> new ScholarShipFoundationException(SCHOLARSHIP_FOUNDATION_NOT_FOUND)).getFoundationInformation()
        );
    }

    @Override
    public void saveAnnouncement(Long announcementId, MemberInfoObject memberInfoObject) {
        Long memberId = memberInfoObject.getMemberId();
        Member member = memberJpaRepository.findById(memberId).orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        AnnouncementEntity ae = announcementJpaRepository.findById(announcementId).orElseThrow(() -> new AnnouncementException(ANNOUNCEMENT_NOT_FOUND));

        applyJpaRepository.save(ApplyEntity.of(null, ApplyStatus.NOT_YET, member, ae));
    }
}
