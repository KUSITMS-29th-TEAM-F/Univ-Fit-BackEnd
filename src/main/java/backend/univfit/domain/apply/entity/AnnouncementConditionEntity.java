package backend.univfit.domain.apply.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "announcement_condition_table")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AnnouncementConditionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "announcement_id")
    private AnnouncementEntity announcementEntity;

    private Double lastGpa; //직전학기 학점
    private Double totalGpa; //총 학점
    private Integer incomeQuality; //소득분위
    private Integer supportSection; //학자금 지원구간
    private Boolean isPresent; //재학 여부
    private String schoolType; //학교 타입 ( 4년제, 2년제)
    private String underprivilegedInfo; //사회적배려계층정보
    private String deptType;
    private Integer age;
    private String StudentGrade;
    private String schoolRegion;
}
