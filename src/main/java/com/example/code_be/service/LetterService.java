package com.example.code_be.service;

import com.example.code_be.entity.Letter;
import com.example.code_be.enums.OpenType;
import com.example.code_be.repository.LetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;

    public Letter save(Letter letter) {
        return letterRepository.save(letter);
    }

    public Letter findById(Long id) {
        return letterRepository.findById(id).orElse(null);
    }

    public List<Letter> findInbox(Long userId) {
        return letterRepository.findByToUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Letter> findSent(Long userId) {
        return letterRepository.findByFromUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Letter> findUnopened(Long userId) {
        return letterRepository.findByToUserIdAndIsOpenedFalseOrderByCreatedAtDesc(userId);
    }

    public boolean canOpen(Letter letter, Long userId) {
        if (letter == null)
            return false;
        if (letter.getIsOpened())
            return true;

        switch (letter.getOpenType()) {
            case NOW:
                return true;
            case SCHEDULED:
                return letter.getOpenAt() != null && LocalDateTime.now().isAfter(letter.getOpenAt());
            case BOTH_CONFIRM:
                return letter.getSenderConfirmed() && letter.getReceiverConfirmed();
            default:
                return false;
        }
    }

    public Letter confirmOpen(Long letterId, Long userId) {
        Letter letter = findById(letterId);
        if (letter == null)
            return null;

        if (letter.getFromUserId().equals(userId)) {
            letter.setSenderConfirmed(true);
        } else if (letter.getToUserId().equals(userId)) {
            letter.setReceiverConfirmed(true);
        }

        // Check if both confirmed
        if (letter.getSenderConfirmed() && letter.getReceiverConfirmed()) {
            letter.setIsOpened(true);
        }

        return letterRepository.save(letter);
    }

    public Letter openLetter(Long letterId) {
        Letter letter = findById(letterId);
        if (letter != null && !letter.getIsOpened()) {
            letter.setIsOpened(true);
            return letterRepository.save(letter);
        }
        return letter;
    }
}
