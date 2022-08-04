package com.study.paging.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.paging.entity.Board;
import com.study.paging.request.BoardSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.study.paging.entity.QBoard.board;

@RequiredArgsConstructor
public class BoardQueryRepositoryImpl implements BoardQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Board> searchBoards(BoardSearch boardSearch) {
        // Board List만 조회하여 반환
        return queryFactory.selectFrom(board)
                .offset(boardSearch.getPageable().getOffset())
                .limit(boardSearch.getSize())
                .fetch();
    }

    @Override
    public Page<Board> searchBoardsWithPage(BoardSearch boardSearch) {
        // Board List를 Page 인터페이스로 반환
        List<Board> contents = queryFactory
                .selectFrom(board)
                .offset(boardSearch.getPageable().getOffset())
                .limit(boardSearch.getPageable().getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(board)
                .offset(boardSearch.getPageable().getOffset())
                .limit(boardSearch.getPageable().getPageSize())
                .fetchCount();

        return new PageImpl<>(contents, boardSearch.getPageable(), total);
    }

    @Override
    public Page<Board> searchBoardsWithCountQuery(BoardSearch boardSearch) {
        // Board List를 Page 인터페이스로 반환
        List<Board> contents = queryFactory
                .selectFrom(board)
                .offset(boardSearch.getPageable().getOffset())
                .limit(boardSearch.getPageable().getPageSize())
                .fetch();

        JPAQuery<Board> countQuery = queryFactory
                .selectFrom(board)
                .offset(boardSearch.getPageable().getOffset())
                .limit(boardSearch.getPageable().getPageSize());

        // boards의 상태에 따라 count쿼리를 호출하거나 호출 안 할 수 있다.
        // Count 쿼리가 생략 가능한 경우 생략해서 처리
        // 페이지 시작이면서 컨텐츠 사이즈가 페이지 사이즈보다 작을 때
        // 마지막 페이지일 때(offset + 컨텐츠 사이즈를 더해서 총 갯수를 구함)
        return PageableExecutionUtils.getPage(contents, boardSearch.getPageable(), () -> countQuery.fetchCount());
    }

}
