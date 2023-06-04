package ru.sberbank.bonus_points_system.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PageOfBonusOperation {

    private final int totalPages;
    private final long totalElements;
    private final int size;
    private final int pageNumber;
    private final List<BonusOperationDto> users;

}
