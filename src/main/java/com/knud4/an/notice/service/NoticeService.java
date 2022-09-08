package com.knud4.an.notice.service;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.entity.Role;
import com.knud4.an.account.repository.AccountRepository;
import com.knud4.an.board.Scope;
import com.knud4.an.exception.NotFoundException;
import com.knud4.an.notice.dto.CreateNoticeForm;
import com.knud4.an.notice.dto.NoticeDTO;
import com.knud4.an.notice.entity.Notice;
import com.knud4.an.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Long createNotice(CreateNoticeForm form, Profile writer) {
        Notice notice = Notice.builder()
                .writer(writer)
                .title(form.getTitle())
                .content(form.getContent())
                .scope(form.getScope())
                .releaseLine(form.getReleaseLine())
                .expiredDate(form.getExpiredDate())
                .build();
        noticeRepository.save(notice);
        return notice.getId();
    }

    public Notice findNoticeById(Long noticeId, Long accountId) {
        Notice findNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundException("공지글을 찾을 수 없습니다."));
        if(findNotice.getScope() == Scope.LINE) {
            Account account = accountRepository.findAccountById(accountId)
                    .orElseThrow(() -> new NotFoundException("계정이 존재하지 않습니다."));
            if(account.getRole() != Role.ROLE_MANAGER &&
                    !account.getLine().getName().equals(findNotice.getReleaseLine())) {
                throw new IllegalStateException("접근 권한이 없습니다.");
            }
        }
        return findNotice;
    }

    public List<Notice> findAll(Scope scope, int page, int count, Long accountId) {
        validatePaging(page, count);
        Account account = accountRepository.findAccountById(accountId)
                .orElseThrow(() -> new NotFoundException("계정이 존재하지 않습니다."));
        if(scope.equals(Scope.ALL)) {
            if (account.getRole().equals(Role.ROLE_MANAGER)) return noticeRepository.findAll(page, count, true);
            return noticeRepository.findAllForAll(account.getLine().getName(), page, count, true);
        }
        if (account.getRole().equals(Role.ROLE_MANAGER)) throw new IllegalStateException("관리자는 라인 지정 불가");
        return noticeRepository.findAllForLINE(account.getLine().getName(), page, count, true);
    }

    @Transactional
    public void updateNotice(Long id, NoticeDTO noticeDTO, Long profileId) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("공지글을 찾을 수 없습니다."));
        if(!notice.getWriter().getId().equals(profileId))
            throw new IllegalStateException("수정 권한이 없습니다.");
        notice.changeTitle(noticeDTO.getTitle());
        notice.changeContent(noticeDTO.getContent());
        notice.changeScope(noticeDTO.getScope());
        notice.changeExpiredDate(noticeDTO.getExpiredDate());
        notice.changeReleaseLine(noticeDTO.getReleaseLine());
    }

    @Transactional
    public void deleteById(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundException("공지글을 찾을 수 없습니다.") );
        noticeRepository.delete(notice);
    }

    public Boolean isLastPage(int page, int cnt) {
        validatePaging(page, cnt);
        Long commentCnt = noticeRepository.findNoticeCount();
        return (long) page*cnt >= commentCnt;
    }

    public Boolean isFirstPage(int page) {
        return page == 1;
    }

    private void validatePaging(int page, int cnt) {
        Long num = noticeRepository.findNoticeCount();
        int limit = (page - 1) * cnt;
        if (page != 1 && num<=limit) {
            throw new IllegalStateException("게시글 요청 범위를 초과하였습니다.");
        }
    }

    public Boolean isMine(Notice notice, Long accountId) {
        return Objects.equals(notice.getWriter().getAccount().getId(), accountId);
    }
}
