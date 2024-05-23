package backend.univfit.domain.member.application;

import backend.univfit.domain.member.dto.request.MakeNickNameRequest;
import backend.univfit.domain.member.dto.request.OnboardingRequest;
import backend.univfit.domain.member.dto.response.AccessTokenResponse;
import backend.univfit.domain.member.dto.response.OnboardingResponse;
import backend.univfit.domain.member.entity.KakaoSocialLogin;
import backend.univfit.domain.member.entity.Member;
import backend.univfit.domain.member.entity.MemberPrivateInfo;
import backend.univfit.domain.member.entity.NaverSocialLogin;
import backend.univfit.domain.member.repository.KakaoSocialLoginJpaRepository;
import backend.univfit.domain.member.repository.MemberPrivateInfoJpaRepository;
import backend.univfit.domain.member.repository.MemberJpaRepository;
import backend.univfit.domain.member.repository.NaverSocialLoginJpaRepository;
import backend.univfit.global.argumentResolver.MemberInfoObject;
import backend.univfit.global.dto.response.GeneralResponse;
import backend.univfit.global.error.exception.OnboardException;
import backend.univfit.global.utils.JwtUtils;
import backend.univfit.global.utils.KakaoApi;
import backend.univfit.global.utils.NaverApi;
import backend.univfit.domain.member.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static backend.univfit.global.error.status.ErrorStatus.ONBOARD_DUPLICATED_NICKNAME;

@Service
@Slf4j
@RequiredArgsConstructor
public class OnboardService {
    @Value("${jwt.secret}")
    private String jwtSecret;
    private final String KAKAO = "kakao";
    private final String NAVER = "naver";

    private final KakaoSocialLoginJpaRepository kakaoSocialLoginJpaRepository;
    private final NaverSocialLoginJpaRepository naverSocialLoginJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final MemberPrivateInfoJpaRepository memberPrivateInfoJpaRepository;


    public LoginResponse login(String sn, String accessToken) throws ParseException {
        String socialId = null;
        boolean isOnboarding = false;
        Member member = null;
        Long memberId = null;
        Long socialLoginId = null;
        String socialLoginPlatForm = null;
        int flag = 0;
        // 카카오 로그인 했을 경우
        if(sn.equals(KAKAO)){
            socialId = KakaoApi.getUserInfo(accessToken);
            KakaoSocialLogin kakaoSocialLogin = kakaoSocialLoginJpaRepository.findByKakaoNumber(socialId);
            if(kakaoSocialLogin == null){
                kakaoSocialLogin = new KakaoSocialLogin();
                kakaoSocialLogin.setKakaoNumber(socialId);
                kakaoSocialLoginJpaRepository.save(kakaoSocialLogin);

            }
            member = kakaoSocialLogin.getMember();
            socialLoginPlatForm = KAKAO;
            socialLoginId = kakaoSocialLogin.getId();


        }
        //네이버 로그인 했을 경우
        if(sn.equals(NAVER)){
            socialId = NaverApi.getResponse(accessToken);
            NaverSocialLogin naverSocialLogin = naverSocialLoginJpaRepository.findByNaverNumber(socialId);
            if(naverSocialLogin == null){
                naverSocialLogin = new NaverSocialLogin();
                naverSocialLogin.setNaverNumber(socialId);
                naverSocialLoginJpaRepository.save(naverSocialLogin);

            }
            member = naverSocialLogin.getMember();
            socialLoginPlatForm = NAVER;
            socialLoginId = naverSocialLogin.getId();

        }

        if(member != null){
            memberId = member.getId();
            if(member.getMemberPrivateInfo() != null){
                isOnboarding = true;
            }
        }

        String ServiceAccessToken = JwtUtils.createAccessToken(socialLoginPlatForm, socialLoginId, memberId, jwtSecret);
        String refreshToken = JwtUtils.createRefreshToken(jwtSecret);


        return LoginResponse.of(memberId,isOnboarding,ServiceAccessToken, refreshToken);

    }


    public AccessTokenResponse makeNickName(MakeNickNameRequest mnr, MemberInfoObject mio) {
        String nickName = mnr.getNickName();
        //중복체크
        if(isDuplicatedNickName(nickName)){
            throw new OnboardException(ONBOARD_DUPLICATED_NICKNAME);
        }

        Member member = Member.builder().build();
        member.setNickName(nickName);
        memberJpaRepository.save(member);

        if(mio.getSocialLoginInfo().equals(NAVER)){
            NaverSocialLogin nsl = naverSocialLoginJpaRepository.findById(mio.getSocialPK()).get();
            nsl.setMember(member);
            naverSocialLoginJpaRepository.save(nsl);
        }
        if(mio.getSocialLoginInfo().equals(KAKAO)){
            KakaoSocialLogin ksl = kakaoSocialLoginJpaRepository.findById(mio.getSocialPK()).get();
            ksl.setMember(member);
            kakaoSocialLoginJpaRepository.save(ksl);
        }

        log.info("socialLoginInfo = {}", mio.getSocialLoginInfo());
        String serviceAccessToken = JwtUtils.createAccessToken(mio.getSocialLoginInfo(), mio.getSocialPK(), member.getId(), jwtSecret);

        return AccessTokenResponse.of(serviceAccessToken);
    }

    public boolean isDuplicatedNickName(String nickName) {
        return memberJpaRepository.findByNickName(nickName) != null;
    }


    public GeneralResponse onboarding(OnboardingRequest obr, MemberInfoObject mio) {
        Member member = memberJpaRepository.findById(mio.getMemberId()).get();
        MemberPrivateInfo memberPrivateInfo = member.getMemberPrivateInfo();
        if(memberPrivateInfo == null){
            memberPrivateInfo = MemberPrivateInfo.builder().build();
        }

        saveMemberPrivateInfo(memberPrivateInfo, member, obr);

        return GeneralResponse.of();
    }

    private void saveMemberPrivateInfo(MemberPrivateInfo memberPrivateInfo, Member member, OnboardingRequest obr) {
        memberPrivateInfo.setGender(obr.getGender());
        memberPrivateInfo.setBirthYear(obr.getBirthYear());
        memberPrivateInfo.setResidence(obr.getResidence());
        memberPrivateInfo.setResidenceType(obr.getResidenceType());
        memberPrivateInfo.setSchoolType(obr.getSchoolType());
        memberPrivateInfo.setSchoolLocation(obr.getSchoolLocation());
        memberPrivateInfo.setSchoolName(obr.getSchoolName());
        memberPrivateInfo.setDeptType(obr.getDeptType());
        memberPrivateInfo.setDeptName(obr.getDeptName());
        memberPrivateInfo.setIsPresent(obr.getIsPresent());
        memberPrivateInfo.setSemester(obr.getSemester());
        if(obr.getTotalFullGrade() >= 4.5){
            memberPrivateInfo.setTotalGradeOfFive(obr.getTotalGrade());
        }
        else{
            memberPrivateInfo.setTotalGradeOfThree(obr.getTotalGrade());
        }

        if(obr.getLastFullGrade() >= 4.5){
            memberPrivateInfo.setLastGradeOfFive(obr.getLastGrade());
        }
        else{
            memberPrivateInfo.setLastGradeOfThree(obr.getLastGrade());
        }
        memberPrivateInfo.setIncomeQuality(obr.getIncomeQuality());
        memberPrivateInfo.setSupportSection(obr.getSupportSection());
        memberPrivateInfo.setMonthlyIncome(obr.getMonthlyIncome());
        memberPrivateInfo.setUnderPrivilegedInfo(obr.getUnderPrivilegedInfo());

        memberPrivateInfoJpaRepository.save(memberPrivateInfo);

        member.setMemberPrivateInfo(memberPrivateInfo);
        memberJpaRepository.save(member);

    }


    public OnboardingResponse updateOnboarding(MemberInfoObject mio) {
        Member member = memberJpaRepository.findById(mio.getMemberId()).get();
        MemberPrivateInfo memberPrivateInfo = member.getMemberPrivateInfo();

        Float totalFullGrade = null;
        Float totalGrade = null;
        if(memberPrivateInfo.getTotalGradeOfFive() != null){
            totalFullGrade = 4.5f;
            totalGrade = memberPrivateInfo.getTotalGradeOfFive();
        }
        if(memberPrivateInfo.getTotalGradeOfThree() != null){
            totalFullGrade = 4.3f;
            totalGrade = memberPrivateInfo.getTotalGradeOfThree();
        }

        Float lastFullGrade = null;
        Float lastGrade = null;
        if(memberPrivateInfo.getLastGradeOfFive() != null){
            lastFullGrade = 4.5f;
            lastGrade = memberPrivateInfo.getLastGradeOfFive();
        }
        if(memberPrivateInfo.getLastGradeOfThree() != null){
            lastFullGrade = 4.3f;
            lastGrade = memberPrivateInfo.getLastGradeOfThree();
        }


        return OnboardingResponse.of(
                memberPrivateInfo.getSchoolType().ordinal(),
                memberPrivateInfo.getSchoolName(),
                memberPrivateInfo.getSchoolLocation(),
                memberPrivateInfo.getDeptType(),
                memberPrivateInfo.getDeptName(),
                memberPrivateInfo.getIsPresent(),
                memberPrivateInfo.getSemester(),
                memberPrivateInfo.getResidence(),
                memberPrivateInfo.getResidenceType(),
                memberPrivateInfo.getGender().ordinal(),
                memberPrivateInfo.getBirthYear(),
                memberPrivateInfo.getUnderPrivilegedInfo(),
                totalFullGrade,
                totalGrade,
                lastFullGrade,
                lastGrade,
                memberPrivateInfo.getIncomeQuality(),
                memberPrivateInfo.getMonthlyIncome(),
                memberPrivateInfo.getSupportSection()
        );
    }
}
