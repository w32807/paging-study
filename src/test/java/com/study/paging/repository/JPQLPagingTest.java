package com.study.paging.repository;

import com.study.paging.entity.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class JPQLPagingTest {

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
    @DisplayName("Not Null 테스트")
    public void test1() throws Exception{
        assertThat(boardRepository).isNotNull();
        assertThat(em).isNotNull();
    }

    @Test
    @DisplayName("JPQL로 페이징하기")
    public void test2() throws Exception{
        // given
        int page = 1;
        int size = 10;
        TypedQuery<Board> query =
                em.createQuery("select b FROM Board b ORDER BY b.id DESC"
                        , Board.class);

        // when
        query.setFirstResult(page * size - size);
        query.setMaxResults(page * size);
        List<Board> boards = query.getResultList();

        // then
        assertThat(boards.size()).isEqualTo(10);
        boards.stream().forEach(board -> {
            System.out.println("board.getId() = " + board.getId());
        });
    }
}