package com.study.paging.repository;

import com.study.paging.entity.Board;
import com.study.paging.request.BoardSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class QueryDslPagingTest {

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
    @DisplayName("QueryDsl로 페이징하기 - 리스트 가져오기")
    public void test1() throws Exception{
        // given
        BoardSearch boardSearch = BoardSearch.builder()
                .page(0)
                .size(10)
                .build();

        // when
        List<Board> boards = boardRepository.searchBoards(boardSearch);

        // then
        assertThat(boards.size()).isEqualTo(10);
        assertThat(boards).extracting("title")
                .containsExactly("글제목1","글제목2","글제목3","글제목4","글제목5"
                        ,"글제목6","글제목7","글제목8","글제목9","글제목10");
    }

    @Test
    @DisplayName("QueryDsl로 - 페이지 가져오기1")
    public void test2() throws Exception{
        // given
        BoardSearch boardSearch = BoardSearch.builder()
                .page(0)
                .size(10)
                .build();

        // when
        Page<Board> boardPage = boardRepository.searchBoardsWithPage(boardSearch);

        // then
        assertThat(boardPage.getContent().size()).isEqualTo(10);
        assertThat(boardPage.getContent()).extracting("title")
                .containsExactly("글제목1","글제목2","글제목3","글제목4","글제목5"
                        ,"글제목6","글제목7","글제목8","글제목9","글제목10");
    }

    @Test
    @DisplayName("QueryDsl로 - 페이지 가져오기2")
    public void test3() throws Exception{
        // given
        BoardSearch boardSearch = BoardSearch.builder()
                .page(0)
                .size(10)
                .build();

        // when
        Page<Board> boardPage = boardRepository.searchBoardsWithCountQuery(boardSearch);

        // then
        assertThat(boardPage.getContent().size()).isEqualTo(10);
        assertThat(boardPage.getContent()).extracting("title")
                .containsExactly("글제목1","글제목2","글제목3","글제목4","글제목5"
                        ,"글제목6","글제목7","글제목8","글제목9","글제목10");
    }

    @Test
    @DisplayName("QueryDsl로 - 페이지 가져오기3 - CountQuery 실행 안하는지 확인")
    public void test4() throws Exception{
        // given
        BoardSearch boardSearch = BoardSearch.builder()
                .page(7)
                .size(7)
                .build();

        // when
        Page<Board> boardPage = boardRepository.searchBoardsWithCountQuery(boardSearch);

        // then
        assertThat(boardPage.getContent().size()).isEqualTo(1);
        assertThat(boardPage.getContent()).extracting("title").containsExactly("글제목50");
        assertThat(boardPage.getTotalElements()).isEqualTo(50);
    }
}