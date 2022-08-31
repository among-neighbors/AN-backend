package com.knud4.an.notice.service;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.entity.Role;
import com.knud4.an.account.repository.AccountRepository;
import com.knud4.an.board.Range;
import com.knud4.an.exception.NotAuthenticatedException;
import com.knud4.an.exception.NotFoundException;
import com.knud4.an.notice.dto.CreateNoticeForm;
import com.knud4.an.notice.entity.Notice;
import com.knud4.an.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .range(form.getRange())
                .releaseLine(form.getReleaseLine())
                .expiredDate(form.getExpiredDate())
                .build();
        noticeRepository.save(notice);
        return notice.getId();
    }

    public Notice findNoticeById(Long noticeId, Long accountId) {
        Notice findNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundException("공지글을 찾을 수 없습니다."));
        if(findNotice.getRange() == Range.LINE) {
            Account account = accountRepository.findAccountById(accountId);
            if(!account.getLine().getName().equals(findNotice.getReleaseLine())) {
                throw new NotAuthenticatedException("접근 권한이 없습니다.");
            }
        }
        return findNotice;
    }

    public List<Notice> findAll(int page, int count, Long accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if(account.getRole().equals(Role.ROLE_MANAGER)) return noticeRepository.findAll(page, count);

        List<Notice> findNotices = noticeRepository.findByRange(Range.ALL, page, count);
        findNotices.addAll(noticeRepository.findByRangeAndLine(Range.LINE, account.getLine().getName(), page, count));
        return findNotices;
    }

    public List<Notice> findAllMyLine(int page, int count, Long accountId) {
        Account account = accountRepository.findAccountById(accountId);
        return noticeRepository.findByLine(account.getLine().getName(), page, count);
    }

    public void deleteById(Long noticeId, Long accountId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NotFoundException("공지글을 찾을 수 없습니다.") );
        Account account = accountRepository.findAccountById(accountId);
        if(account.getRole() != Role.ROLE_MANAGER)
            throw new NotAuthenticatedException("삭제 권한이 없습니다.");
        noticeRepository.delete(notice);
    }
}
