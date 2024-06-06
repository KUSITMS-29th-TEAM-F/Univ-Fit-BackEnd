package backend.univfit.domain.review.entity;

import backend.univfit.domain.apply.entity.enums.ApplyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entity_id")
    private Long id;

    private Integer star;
    private String name;
    private String foundation;
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private ApplyStatus status;

    private String contents;


}
