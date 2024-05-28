package backend.univfit.domain.coverletter.service;

import backend.univfit.domain.apply.entity.AnnouncementEntity;
import backend.univfit.domain.apply.entity.ApplyEntity;
import backend.univfit.domain.apply.entity.enums.ApplyStatus;
import backend.univfit.domain.apply.repository.ApplyJpaRepository;
import backend.univfit.domain.coverletter.api.dto.*;
import backend.univfit.domain.coverletter.entity.CoverLetterEntity;
import backend.univfit.domain.coverletter.entity.CoverLetterQuestionEntity;
import backend.univfit.domain.coverletter.repository.CoverLetterJpaRepository;
import backend.univfit.domain.coverletter.repository.CoverLetterQuestionJpaRepository;
import backend.univfit.domain.member.entity.Member;
import backend.univfit.domain.member.repository.MemberJpaRepository;
import backend.univfit.global.Comparator.CoverLetterQuestionComparator;
import backend.univfit.global.argumentResolver.MemberInfoObject;
import backend.univfit.global.dto.response.GeneralResponse;
import backend.univfit.global.error.exception.CoverLetterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static backend.univfit.global.error.status.ErrorStatus.COVER_LETTER_MEMBER_NOT_MATCH;

@Service
@RequiredArgsConstructor
public class CoverLetterService {
    private final MemberJpaRepository memberJpaRepository;
    private final ApplyJpaRepository applyJpaRepository;
    private final CoverLetterJpaRepository coverLetterJpaRepository;
    private final CoverLetterQuestionJpaRepository coverLetterQuestionJpaRepository;


    public CoverLetterQuestionsResponse showCoverLetterQuestions(Long applyId, MemberInfoObject mio) {
        ApplyEntity applyEntity = applyJpaRepository.findById(applyId).get();
        Member member = memberJpaRepository.findById(mio.getMemberId()).get();
        //만약 다른 멤버가 저장한 공고에 접근하려할 때 에러처리
        if(applyEntity.getMember() != member){
            throw new CoverLetterException(COVER_LETTER_MEMBER_NOT_MATCH);
        }

        //질문 가져오기
        AnnouncementEntity announcementEntity = applyEntity.getAnnouncementEntity();
        String[] questions = announcementEntity.getCoverLetterQuestion().split("\n");

        //응답객체 생성

        ArrayList<String> coverLetterQuestionsArrayList = new ArrayList<>(Arrays.asList(questions));

        return CoverLetterQuestionsResponse.of(coverLetterQuestionsArrayList);

    }

    public SaveCoverLetterResponse saveCoverLetter(SaveCoverLetterRequest clr, MemberInfoObject mio) {
        Member member = memberJpaRepository.findById(mio.getMemberId()).get();
        ApplyEntity applyEntity = applyJpaRepository.findById(clr.getApplyId()).get();


        //다른 유저의 지원정보로 접근하려 할때
        if(applyEntity.getMember() != member){
            throw new CoverLetterException(COVER_LETTER_MEMBER_NOT_MATCH);
        }

        CoverLetterEntity coverLetterEntity = CoverLetterEntity.of(member, clr.getTitle(), applyEntity);;
        coverLetterJpaRepository.save(coverLetterEntity);
        ArrayList<CoverLetterObj> coverLetterObjs = clr.getCoverLetters();
        //자소서 작성
        for(CoverLetterObj obj : coverLetterObjs){
            CoverLetterQuestionEntity coverLetterQuestionEntity = CoverLetterQuestionEntity.of(
                    obj.getQuestion(),
                    obj.getContent(),
                    coverLetterEntity
            );

            coverLetterQuestionJpaRepository.save(coverLetterQuestionEntity);
        }

        return SaveCoverLetterResponse.of(coverLetterEntity.getId());
    }

    public GeneralResponse patchCoverLetter(PathCoverLetterRequest clr, MemberInfoObject mio) {
        Member member = memberJpaRepository.findById(mio.getMemberId()).get();
        CoverLetterEntity coverLetterEntity = coverLetterJpaRepository.findById(clr.getCoverLetterId()).get();
        //다른 유저의 자소서 정보로 접근하려 할때
        if(coverLetterEntity.getMember() != member){
            throw new CoverLetterException(COVER_LETTER_MEMBER_NOT_MATCH);
        }

        //지원공고 바꾸기
        ApplyEntity applyEntity = applyJpaRepository.findById(clr.getApplyId()).get();
        coverLetterEntity.setApplyEntity(applyEntity);

        //제목 바꾸기
        coverLetterEntity.setTitle(clr.getTitle());

        coverLetterJpaRepository.save(coverLetterEntity);

        //자소서 내용 바꾸기
        ArrayList<CoverLetterQuestionEntity> coverLetterQuestionEntities = coverLetterQuestionJpaRepository.findAllByCoverLetterEntity(coverLetterEntity);

        //pk값으로 오름차순 정렬
        coverLetterQuestionEntities.sort(new CoverLetterQuestionComparator());

        //수정한 자소서 내용 가져오기
        ArrayList<CoverLetterObj> coverLetterObjs = clr.getCoverLetters();

        int i = 0; //반복자
        for(CoverLetterObj obj : coverLetterObjs){
            String question = obj.getQuestion();
            String content = obj.getContent();

            CoverLetterQuestionEntity cle = coverLetterQuestionEntities.get(i);
            cle.setQuestion(question);
            cle.setContent(content);

            coverLetterQuestionJpaRepository.save(cle);
            i++;
        }

        return GeneralResponse.of();

    }

    public CoverLetterApplyList getApplyList(MemberInfoObject mio) {
        Member member = memberJpaRepository.findById(mio.getMemberId()).get();
        List<ApplyEntity> applyEntities = applyJpaRepository.findAllByMember(member);

        ArrayList<CoverLetterApplyObj> coverLetterObjArrayList = new ArrayList<>();
        for(ApplyEntity ae : applyEntities){
            if(ae.getAnnouncementEntity().getIsCoverLetterNeed() && ae.getApplyStatus() == ApplyStatus.APPLY){
                CoverLetterApplyObj cla = CoverLetterApplyObj.of(ae.getId(), ae.getAnnouncementEntity().getScholarShipName());
                coverLetterObjArrayList.add(cla);
            }
        }

        return CoverLetterApplyList.of(coverLetterObjArrayList);

    }
}
