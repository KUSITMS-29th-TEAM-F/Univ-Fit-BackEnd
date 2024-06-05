package backend.univfit.domain.apply.api.dto.response;

public record ConditionCheckResponse(
        String subject,
        String light
) {
    public static ConditionCheckResponse of(String subject, String light) {
        return new ConditionCheckResponse(subject, light);
    }
}
