package com.study.paging.repository;

import com.study.paging.entity.Board;
import com.study.paging.request.BoardSearch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BoardQueryRepository {

    List<Board> searchBoards(BoardSearch boardSearch);

    Page<Board> searchBoardsWithPage(BoardSearch boardSearch);

    Page<Board> searchBoardsWithCountQuery(BoardSearch boardSearch);

}
