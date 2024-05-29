package backend.univfit.domain.coverletter.entity;

import backend.univfit.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cover_letter_question_table ")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CoverLetterQuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cover_letter_id")
    private CoverLetterEntity coverLetterEntity;

    public static CoverLetterQuestionEntity of(
            String question,
            String content,
            CoverLetterEntity coverLetterEntity
    ){
        return CoverLetterQuestionEntity.builder()
                .question(question)
                .content(content)
                .coverLetterEntity(coverLetterEntity)
                .build();
    }

}
