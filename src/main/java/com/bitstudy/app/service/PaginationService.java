package com.bitstudy.app.service;

import com.jayway.jsonpath.internal.function.numeric.Max;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

/** 게시판 페이지 페이지네이션 서비스 구현*/
@Service
public class PaginationService {

    /** 페이지네이션 바의 길이(개수) - 한번에 보여줄 페이지 숫자 결정*/
    private static final int BAR_LENGTH = 5;

    /** return : 숫자 리스트를 받아 뷰에 보내줄 용도
     * int currentPageNumber : 현재 위치한 페이지 번호
     * int totalPages : 전체 페이지 수*/
    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {
        /*startNumber, endNumber 구하기
        * 현재 선택한 페이지 번호가 페이징 부분의 가운데에 위치하게 만들기*/
        int startNumber = Math.max(0, currentPageNumber - (BAR_LENGTH/2));
        /*공식이 음수가 될 경우 0이 되도록*/
        int endPage = Math.min(startNumber + BAR_LENGTH, totalPages);
        return IntStream.range(startNumber, endPage).boxed().toList();
        /*boxed() 메서드는 IntStream같은 원시타입에 대한 스트림 지원을 한다.*/
    }
    /** 페이징 바 개수 getter*/
    public int currentBarLength() {
        return BAR_LENGTH;
    }
}
