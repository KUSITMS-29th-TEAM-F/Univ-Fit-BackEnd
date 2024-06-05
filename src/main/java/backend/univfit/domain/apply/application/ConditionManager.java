package backend.univfit.domain.apply.application;

import backend.univfit.domain.apply.api.dto.response.ConditionCheckResponse;
import backend.univfit.domain.apply.entity.AnnouncementConditionEntity;
import backend.univfit.domain.apply.entity.AnnouncementEntity;
import backend.univfit.domain.apply.entity.ConditionEntity;
import backend.univfit.domain.apply.exception.AnnouncementConditionException;
import backend.univfit.domain.apply.exception.ConditionException;
import backend.univfit.domain.apply.repository.AnnouncementConditionJpaRepository;
import backend.univfit.domain.apply.repository.AnnouncementJpaRepository;
import backend.univfit.domain.apply.repository.ConditionJpaRepository;
import backend.univfit.domain.member.entity.Member;
import backend.univfit.domain.member.entity.MemberPrivateInfo;
import backend.univfit.domain.member.repository.MemberPrivateInfoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;

import static backend.univfit.global.error.status.ErrorStatus.ANNOUNCEMENT_CONDITION_NOT_FOUND;
import static backend.univfit.global.error.status.ErrorStatus.CONDITION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ConditionManager {
    private final ConditionJpaRepository conditionJpaRepository;
    private final MemberPrivateInfoJpaRepository memberPrivateInfoJpaRepository;
    private final AnnouncementJpaRepository announcementJpaRepository;
    private final AnnouncementConditionJpaRepository announcementConditionJpaRepository;


    public List<ConditionCheckResponse> checkCondition(AnnouncementEntity announcementEntity, Member member) {
        AnnouncementConditionEntity announcementConditionEntity = announcementConditionJpaRepository.findByAnnouncementEntity(announcementEntity)
                .orElseThrow(() -> new AnnouncementConditionException(ANNOUNCEMENT_CONDITION_NOT_FOUND));
        ConditionEntity conditionEntity = conditionJpaRepository.findByAnnouncementEntity(announcementEntity)
                .orElseThrow(() -> new ConditionException(CONDITION_NOT_FOUND));

        MemberPrivateInfo memberInfo = member.getMemberPrivateInfo();
        String applicationConditions = announcementEntity.getApplicationConditions();
        return evaluateConditions(memberInfo, announcementConditionEntity, applicationConditions, conditionEntity);
    }

    private List<ConditionCheckResponse> evaluateConditions(MemberPrivateInfo memberInfo, AnnouncementConditionEntity announcementConditionEntity,
                                                            String applicationConditions, ConditionEntity conditionEntity) {

        List<ConditionCheckResponse> responses = new ArrayList<>();
        Set<String> evaluatedConditions = new HashSet<>();
        List<String> compareExcept = new ArrayList<>();

        // 최종학점 평가
        if (announcementConditionEntity.getLastGpa() != null) {
            String result = (memberInfo.getLastGradeOfFive() != null && memberInfo.getLastGradeOfFive() >= announcementConditionEntity.getLastGpa()) ? "초록불" : "빨간불";
            responses.add(new ConditionCheckResponse("직전학기 학점 " + announcementConditionEntity.getLastGpa() + " 이상", memberInfo.getLastGradeOfFive() != null ? result : "회색불"));
//            compareExcept.add("직전학기 학점 " + announcementConditionEntity.getLastGpa() + " 이상");
        }

        // 총학점 평가
        if (announcementConditionEntity.getTotalGpa() != null) {
            String result = (memberInfo.getTotalGradeOfFive() != null && memberInfo.getTotalGradeOfFive() >= announcementConditionEntity.getTotalGpa()) ? "초록불" : "빨간불";
            responses.add(new ConditionCheckResponse("전체학점 " + announcementConditionEntity.getTotalGpa() + " 이상", memberInfo.getTotalGradeOfFive() != null ? result : "회색불"));
//            compareExcept.add("전체학점 " + announcementConditionEntity.getTotalGpa() + " 이상");
        }

        // 소득분위 평가
        if (announcementConditionEntity.getIncomeQuality() != null) {
            String result = (memberInfo.getIncomeQuality() != null && memberInfo.getIncomeQuality() <= announcementConditionEntity.getIncomeQuality()) ? "초록불" : "빨간불";
            responses.add(new ConditionCheckResponse(announcementConditionEntity.getIncomeQuality() + "분위 이내", memberInfo.getIncomeQuality() != null ? result : "회색불"));
//            compareExcept.add(announcementConditionEntity.getIncomeQuality() + "분위 이내");
        }

        // 학교지역 평가
        if (announcementConditionEntity.getSchoolRegion() != null) {
            String result = (memberInfo.getSchoolLocation() != null && memberInfo.getSchoolLocation().contains(announcementConditionEntity.getSchoolRegion()) ? "초록불" : "빨간불");
            responses.add(new ConditionCheckResponse(announcementConditionEntity.getSchoolRegion() + "소제 대학", memberInfo.getSchoolLocation() != null ? result : "회색불"));

//            compareExcept.add(announcementConditionEntity.getIncomeQuality() + "분위 이내");
        }

        // 지원 구분 평가
        if (conditionEntity.getSupportSection() != null) {
            String result = (memberInfo.getSupportSection() != null && memberInfo.getSupportSection() <= conditionEntity.getSupportSection()) ? "초록불" : "빨간불";
            responses.add(new ConditionCheckResponse(announcementConditionEntity.getSupportSection() + "구간 이내", memberInfo.getSupportSection() != null ? result : "회색불"));
//            compareExcept.add(announcementConditionEntity.getIncomeQuality() + "구간 이내");
        }

        // 재학 여부 평가
        if (announcementConditionEntity.getIsPresent() != null) {
            String result = (memberInfo.getIsPresent() != null && memberInfo.getIsPresent().equals(announcementConditionEntity.getIsPresent())) ? "초록불" : "빨간불";
            responses.add(new ConditionCheckResponse("재학", memberInfo.getIsPresent() != null ? result : "회색불"));
        }

        // 나이 여부 평가
        if (announcementConditionEntity.getAge() != null) {
            int age = Year.now().getValue() - memberInfo.getBirthYear();
            String result = (memberInfo.getBirthYear() != null && age == announcementConditionEntity.getAge()) ? "초록불" : "빨간불";
            responses.add(new ConditionCheckResponse("만 " + announcementConditionEntity.getAge() + "세 이하", memberInfo.getIsPresent() != null ? result : "회색불"));
        }

        // 학년 or 학기 여부 평가
        if (announcementConditionEntity.getStudentGrade() != null) {
            Integer semester = memberInfo.getSemester();
            String studentGrade = null;
            if (semester == 1 || semester == 2) {
                studentGrade = "1";
            } else if (semester == 3 || semester == 4) {
                studentGrade = "2";
            } else if (semester == 5 || semester == 6) {
                studentGrade = "3";
            } else if (semester == 7 || semester == 8) {
                studentGrade = "4";
            } else {
                return null;
            }
            String[] validGrades = announcementConditionEntity.getStudentGrade().split(",");
            boolean isValidGrade = Arrays.asList(validGrades).contains(studentGrade);
            String result = (isValidGrade) ? "초록불" : "빨간불";
            responses.add(new ConditionCheckResponse(announcementConditionEntity.getStudentGrade() + "학년 지원 가능", memberInfo.getIsPresent() != null ? result : "회색불"));
        }

        // 학교 유형 평가
        if (announcementConditionEntity.getSchoolType() != null) {
            if (announcementConditionEntity.getSchoolType().equals("UNIVERSITY")) {
                String result = (memberInfo.getSchoolType() != null && memberInfo.getSchoolType().toString().equalsIgnoreCase(String.valueOf(conditionEntity.getSchoolType()))) ? "초록불" : "빨간불";
                responses.add(new ConditionCheckResponse("대학4년제", memberInfo.getSchoolType() != null ? result : "회색불"));
            } else {
                String result = (memberInfo.getSchoolType() != null && memberInfo.getSchoolType().toString().equalsIgnoreCase(String.valueOf(conditionEntity.getSchoolType()))) ? "초록불" : "빨간불";
                responses.add(new ConditionCheckResponse("대학2년제", memberInfo.getSchoolType() != null ? result : "회색불"));
            }
        }

        //전공 유형 평가
        if (announcementConditionEntity.getDeptType() != null) {
            if (announcementConditionEntity.getDeptType().equals("전공무관")) {
                if (memberInfo.getDeptType() != null) {
                    responses.add(new ConditionCheckResponse("전공무관", "초록불"));
                }
            } else {
                String[] splitDeptTypes = announcementConditionEntity.getDeptType().split(",");
                boolean isDeptMatch = false; // 전공 일치 여부를 확인하는 플래그
                if (memberInfo.getDeptType() != null) {
                    for (String deptType : splitDeptTypes) {
                        if (deptType.trim().equalsIgnoreCase(memberInfo.getDeptType().toString().trim())) {
                            isDeptMatch = true;
                            break;
                        }
                    }
                }
                String result = isDeptMatch ? "초록불" : "빨간불";
//                compareExcept.add("전공제한");
                responses.add(new ConditionCheckResponse("전공제한", memberInfo.getDeptType() != null ? result : "회색불"));
            }
        }

        //사회적 배려 계층 여부
        if (announcementConditionEntity.getUnderprivilegedInfo() != null) {
            List<String> conditionKeywords = Arrays.asList(announcementConditionEntity.getUnderprivilegedInfo().split(","));
            for (String keyword : conditionKeywords) {
                if (memberInfo != null && memberInfo.getUnderPrivilegedInfo().toLowerCase().contains(keyword.trim().toLowerCase())) {
                    responses.add(new ConditionCheckResponse(keyword, "초록불"));
                } else if (memberInfo != null && !(memberInfo.getUnderPrivilegedInfo().toLowerCase().contains(keyword.trim().toLowerCase()))) {
                    responses.add(new ConditionCheckResponse(keyword, "빨간불"));
                } else {
                    responses.add(new ConditionCheckResponse(keyword, "회색불"));
                }
            }

        }
            //예외 조건 평가

        if (conditionEntity.getExceptionValue() != null) {
            String[] splitExceptionValue = conditionEntity.getExceptionValue().split(",");
            for (String exceptionValue : splitExceptionValue) {
                responses.add(new ConditionCheckResponse(exceptionValue, "회색불"));

            }
        }
        return responses;
    }
}
