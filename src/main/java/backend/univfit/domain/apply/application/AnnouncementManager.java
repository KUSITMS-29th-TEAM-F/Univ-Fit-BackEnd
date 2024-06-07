package backend.univfit.domain.apply.application;

import backend.univfit.domain.apply.entity.AnnouncementEntity;
import backend.univfit.domain.apply.entity.ConditionEntity;
import backend.univfit.domain.apply.entity.enums.AnnouncementStatus;
import backend.univfit.domain.apply.exception.ConditionException;
import backend.univfit.domain.apply.repository.ConditionJpaRepository;
import backend.univfit.domain.member.entity.Member;
import backend.univfit.domain.member.entity.MemberPrivateInfo;
import backend.univfit.domain.member.exception.MemberException;
import backend.univfit.domain.member.exception.MemberPrivateInfoException;
import backend.univfit.domain.member.repository.MemberJpaRepository;
import backend.univfit.domain.member.repository.MemberPrivateInfoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static backend.univfit.global.error.status.ErrorStatus.*;

@Service
@RequiredArgsConstructor
public class AnnouncementManager {
    private final ConditionJpaRepository conditionJpaRepository;
    private final MemberPrivateInfoJpaRepository memberPrivateInfoJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    public String checkAnnouncementStatus(AnnouncementEntity announcementEntity) {
        if (!LocalDate.now().isBefore(announcementEntity.getStartApplyDate()) && !LocalDate.now().isAfter(announcementEntity.getEndDocumentDate())) {
            return String.valueOf(AnnouncementStatus.ING);
        } else if (LocalDate.now().isBefore(announcementEntity.getStartApplyDate())) {
            return String.valueOf(AnnouncementStatus.UPCOMING);
        } else {
            return String.valueOf(AnnouncementStatus.FINISHED);
        }
    }

    public String checkEligibility(AnnouncementEntity announcementEntity, Long memberId) {
        ConditionEntity condition = conditionJpaRepository.findByAnnouncementEntity(announcementEntity)
                .orElseThrow(() -> new ConditionException(CONDITION_NOT_FOUND));
        Member member = memberJpaRepository.findById(memberId).orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        MemberPrivateInfo memberInfo = memberPrivateInfoJpaRepository.findById(member.getMemberPrivateInfo().getId())
                .orElseThrow(() -> new MemberPrivateInfoException(MEMBER_PRIVATE_NOT_FOUND));

        String s = compareConditions(condition, memberInfo);
        System.out.println("s = " + s);
        return s;
    }

    private String compareConditions(ConditionEntity condition, MemberPrivateInfo memberInfo) {
        System.out.println("condition = " + condition.getSemester());
        boolean basicInfoMatch = compareBasicInfo(condition, memberInfo);
        System.out.println("basicInfoMatch = " + basicInfoMatch);

        boolean gradesMatch = compareGrades(condition, memberInfo);
        System.out.println("gradesMatch = " + gradesMatch);

        boolean incomeMatch = compareIncome(condition, memberInfo);
        System.out.println("incomeMatch = " + incomeMatch);

//        // 조건 중 하나라도 일치하지 않는 경우, 지원불가로 처리
//        if (!basicInfoMatch || !gradesMatch || !incomeMatch) {
//            return "지원불가";
//        }
//
//        // 모두 일치하는 경우
//        if (basicInfoMatch && gradesMatch && incomeMatch) {
//            // exceptionValue 있으면 판단 불가로 함, 그게 아니면 지원 대상
//            if (condition.getExceptionValue() != null) {
//                return "판단불가";
//            }
//            return "지원대상";
//        }
//
//        // condition 필드는 있는데 memberInfo에 해당 필드가 null인 경우는 판단불가
//        if ((condition.getDeptType() != null && memberInfo.getDeptType() == null) ||
//                (condition.getDeptName() != null && memberInfo.getDeptName() == null) ||
//                (condition.getUnderPrivilegedInfo() != null && memberInfo.getUnderPrivilegedInfo() == null) ||
//                (condition.getSemester() != null && memberInfo.getSemester() == null) ||
//                (condition.getSchoolType() != null && memberInfo.getSchoolType() == null) ||
//                (condition.getSchoolName() != null && memberInfo.getSchoolName() == null) ||
//                (condition.getIsPresent() != null && memberInfo.getIsPresent() == null) ||
//                (condition.getResidence() != null && memberInfo.getResidence() == null) ||
//                (condition.getResidenceType() != null && memberInfo.getResidenceType() == null) ||
//                (condition.getGender() != null && memberInfo.getGender() == null) ||
//                (condition.getAge() != null && memberInfo.getBirthYear() == null) ||
//                (condition.getMonthlyIncome() != null && memberInfo.getMonthlyIncome() == null) ||
//                (condition.getIncomeQuality() != null && memberInfo.getIncomeQuality() == null) ||
//                (condition.getSupportSection() != null && memberInfo.getSupportSection() == null)) {
//            return "판단불가";
//        }
//
//        return "판단불가";
        // condition 필드가 null이 아닌데 memberInfo 필드가 null인 경우는 판단불가
        if ((condition.getDeptType() != null && memberInfo.getDeptType() == null) ||
                (condition.getDeptName() != null && memberInfo.getDeptName() == null) ||
                (condition.getUnderPrivilegedInfo() != null && memberInfo.getUnderPrivilegedInfo() == null) ||
                (condition.getSemester() != null && memberInfo.getSemester() == null) ||
                (condition.getSchoolType() != null && memberInfo.getSchoolType() == null) ||
                (condition.getSchoolName() != null && memberInfo.getSchoolName() == null) ||
                (condition.getIsPresent() != null && memberInfo.getIsPresent() == null) ||
                (condition.getResidence() != null && memberInfo.getResidence() == null) ||
                (condition.getResidenceType() != null && memberInfo.getResidenceType() == null) ||
                (condition.getGender() != null && memberInfo.getGender() == null) ||
                (condition.getAge() != null && memberInfo.getBirthYear() == null) ||
                (condition.getMonthlyIncome() != null && memberInfo.getMonthlyIncome() == null) ||
                (condition.getIncomeQuality() != null && memberInfo.getIncomeQuality() == null) ||
                (condition.getSupportSection() != null && memberInfo.getSupportSection() == null)) {
            return "판단불가";
        }

        // 조건 중 하나라도 일치하지 않는 경우, 지원불가로 처리
        if (!basicInfoMatch || !gradesMatch || !incomeMatch) {
            return "지원불가";
        }

        // 모두 일치하는 경우
        if (basicInfoMatch && gradesMatch && incomeMatch) {
            // exceptionValue 있으면 판단 불가로 함, 그게 아니면 지원 대상
            if (condition.getExceptionValue() != null) {
                return "판단불가";
            }
            return "지원대상";
        }

        return "판단불가";
    }

    private boolean compareBasicInfo(ConditionEntity condition, MemberPrivateInfo memberInfo) {
        boolean deptTypeMatch = true;
        if (condition.getDeptType() != null && !condition.getDeptType().isEmpty()) {
            List<String> allowedDeptTypes = Arrays.asList(condition.getDeptType().split("\\s*,\\s*"));
            deptTypeMatch = allowedDeptTypes.contains(memberInfo.getDeptType());
        }
        System.out.println("deptTypeMatch = " + deptTypeMatch); //T

        boolean underPrevilegedMatch = true;
        if (condition.getUnderPrivilegedInfo() != null && !condition.getUnderPrivilegedInfo().isEmpty()) {
            List<String> allowedUnderPrivilegedTypes = Arrays.asList(condition.getUnderPrivilegedInfo().split("\\s*,\\s*"));
            underPrevilegedMatch = allowedUnderPrivilegedTypes.contains(memberInfo.getUnderPrivilegedInfo());
        }

        System.out.println("underPrevilegedMatch = " + underPrevilegedMatch); //T

        boolean semesterMatch = true;
        System.out.println("condition = " + condition.getSemester());
        System.out.println("memberInfo.getSemester() = " + memberInfo.getSemester());
        if (condition.getSemester() != null && !condition.getSemester().isEmpty()) {
            List<String> semester = Arrays.asList(condition.getSemester().split("\\s*,\\s*"));
            semesterMatch = semester.contains(String.valueOf(memberInfo.getSemester()));
        }

        System.out.println("semesterMatch = " + semesterMatch); //T

        int currentYear = Year.now().getValue();
        int age = currentYear - memberInfo.getBirthYear();

        return (condition.getSchoolType() == null || Objects.equals(condition.getSchoolType(), memberInfo.getSchoolType())) &&
                (condition.getSchoolName() == null || Objects.equals(condition.getSchoolName(), memberInfo.getSchoolName())) &&
                deptTypeMatch &&
                (condition.getIsPresent() == null || Objects.equals(condition.getIsPresent(), memberInfo.getIsPresent())) &&
                semesterMatch &&
                (condition.getResidence() == null || Objects.equals(condition.getResidence(), memberInfo.getResidence())) &&
                (condition.getResidenceType() == null || Objects.equals(condition.getResidenceType(), memberInfo.getResidenceType())) &&
                (condition.getGender() == null || Objects.equals(condition.getGender(), memberInfo.getGender())) &&
                (condition.getAge() == null || (condition.getAge() >= age)) &&
                underPrevilegedMatch; //T
    }

    private boolean compareGrades(ConditionEntity condition, MemberPrivateInfo memberInfo) {
        boolean useThree = memberInfo.getLastGradeOfThree() != null;

        if (useThree) {
            return (condition.getTotalGradeOfThree() == null ||
                    (memberInfo.getTotalGradeOfThree() != null && condition.getTotalGradeOfThree() <= memberInfo.getTotalGradeOfThree())) &&
                    (condition.getLastGradeOfThree() == null ||
                            (memberInfo.getLastGradeOfThree() != null && condition.getLastGradeOfThree() <= memberInfo.getLastGradeOfThree()));
        } else {
            return (condition.getTotalGradeOfFive() == null ||
                    (memberInfo.getTotalGradeOfFive() != null && condition.getTotalGradeOfFive() <= memberInfo.getTotalGradeOfFive())) &&
                    (condition.getLastGradeOfFive() == null ||
                            (memberInfo.getLastGradeOfFive() != null && condition.getLastGradeOfFive() <= memberInfo.getLastGradeOfFive()));
        }
    }

    private boolean compareIncome(ConditionEntity condition, MemberPrivateInfo memberInfo) {
        return (condition.getIncomeQuality() == null ||
                (memberInfo.getIncomeQuality() != null && condition.getIncomeQuality() >= memberInfo.getIncomeQuality())) &&
                (condition.getSupportSection() == null ||
                        (memberInfo.getSupportSection() != null && condition.getSupportSection() >= memberInfo.getSupportSection())) &&
                (condition.getMonthlyIncome() == null ||
                        (memberInfo.getMonthlyIncome() != null && condition.getMonthlyIncome() >= memberInfo.getMonthlyIncome()));
    }

}
