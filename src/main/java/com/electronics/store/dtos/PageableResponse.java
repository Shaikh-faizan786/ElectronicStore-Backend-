package com.electronics.store.dtos;

import lombok.*;

import java.util.List;

// <T> generic bana ne ke lie sab pe apply hojayega ye
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponse<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPges;
    private boolean lastPages;
}
