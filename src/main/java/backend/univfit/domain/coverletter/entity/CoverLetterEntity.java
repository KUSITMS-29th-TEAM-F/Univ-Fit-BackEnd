package backend.univfit.domain.coverletter.entity;

import backend.univfit.domain.apply.entity.ApplyEntity;
import backend.univfit.domain.member.entity.Member;
import backend.univfit.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cover_letter_table")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CoverLetterEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    @ManyToOne
    @JoinColumn(name = "apply_id")
    private ApplyEntity applyEntity;

    public static CoverLetterEntity of(
        Member member,
        String title,
        ApplyEntity applyEntity
    ){
        return CoverLetterEntity.builder()
                .member(member)
                .title(title)
                .applyEntity(applyEntity)
                .build();
    }

}
