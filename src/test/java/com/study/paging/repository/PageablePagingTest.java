package com.study.paging.repository;

import com.study.paging.entity.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.stream.IntStream;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PageablePagingTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    public EntityManager em;

    @BeforeEach
    public void setup(){
        IntStream.rangeClosed(1, 50).forEach(i -> {
            boardRepository.save(
                    Board.builder()
                    .title("글제목" + i)
                    .content("글내용" + i)
                    .build());
        });
    }

    @Test
    @DisplayName("Pagable로 페이징하기")
    public void test2() throws Exception{
        // given
        int page = 1;
        int size = 10;

        // page, size를 입력하여 생성
        Pageable pageable1 = PageRequest.of(page, size);

        // page, size, 정렬 기준 1개를 입력하여 생성
        Pageable pageable2 = PageRequest.of(page, size, Sort.by("id").descending());
        Pageable pageable3 = PageRequest.of(page, size, Sort.by("title").ascending());

        // page, size, 정렬 기준 여러 개를 입력하여 생성
        // Direction의 설정대로 모든 properties가 정렬됩니다.
        // ex. 여기서는 ORDER BY id DESC, content DESC 가 됩니다.
        Pageable pageable4 = PageRequest.of(page, size, Sort.Direction.DESC, "id", "content");

        // page, size, 정렬 기준 여러 개를 입력하여 생성
        // 각각의 정렬 기준의 내림, 올림차순을 제어할 수 있습니다.
        // ex. 여기서는 ORDER BY id ASC, content DESC 가 됩니다.
        Sort sort = Sort.by(
                new Sort.Order(Sort.Direction.ASC, "id")
                , new Sort.Order(Sort.Direction.DESC, "title"));
        Pageable pageable5 = PageRequest.of(page, size, sort);

        boardRepository.findAll(pageable1);
        boardRepository.findAll(pageable2);
        boardRepository.findAll(pageable3);
        boardRepository.findAll(pageable4);
        boardRepository.findAll(pageable5);



        /**
         * page request 생성자에 따른 차이?
         * page와 pageable 차이?
         * PagingAndSortingRepository ??
         * page 타입? slice 타입? list 타입 반환?
         */

    }

    @Test
    @DisplayName("Pagable로 페이징하기")
    public void test3() throws Exception{
        // given
        int page = 1;
        int size = 10;

        // page, size를 입력하여 생성
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boardPage = boardRepository.findAll(pageable);

        // 현재 페이지
        System.out.println("boardPage.getNumber() = " + boardPage.getNumber());

        // 현재 페이지
        System.out.println("boardPage.getSize() = " + boardPage.getSize());

        // 전체 페이지 수
        System.out.println("boardPage.getTotalPages() = " + boardPage.getTotalPages());

        // 페이지 당 데이터 갯수
        System.out.println("boardPage.getNumberOfElements() = " + boardPage.getNumberOfElements());

        // 전체 데이터 갯수
        System.out.println("boardPage.getTotalElements() = " + boardPage.getTotalElements());

        // 이전 페이지 여부
        System.out.println("boardPage.hasPrevious() = " + boardPage.hasPrevious());

        // 현재 페이지가 첫 페이지인지 여부
        System.out.println("boardPage.isFirst() = " + boardPage.isFirst());

        // 다음 페이지 존재여부
        System.out.println("boardPage.hasNext() = " + boardPage.hasNext());

        // 현재 페이지가 마지막 페이지인지 여부
        System.out.println("boardPage.isLast() = " + boardPage.isLast());

        // 다음 페이지 객체 얻어오기 (없으면 null)
        System.out.println("boardPage.nextPageable() = " + boardPage.nextPageable());

        // 이전 페이지 객체 얻어오기 (없으면 null)
        System.out.println("boardPage.previousPageable() = " + boardPage.previousPageable());

        // 현재 페이지로 조회된 데이터
        System.out.println("boardPage.getContent() = " + boardPage.getContent());

        // 조회된 데이터 존재여부
        System.out.println("boardPage.hasPrevious() = " + boardPage.hasPrevious());

        // 정렬 정보
        System.out.println("boardPage.getSort() = " + boardPage.getSort());



        /**
         * page request 생성자에 따른 차이?
         * page와 pageable 차이?
         * PagingAndSortingRepository ??
         * page 타입? slice 타입? list 타입 반환?
         */

    }
}