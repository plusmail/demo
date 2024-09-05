package kroryi.demo.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kroryi.demo.domain.Board;
import kroryi.demo.domain.QBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends
        QuerydslRepositorySupport
        implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search(Pageable pageable) {

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        BooleanBuilder builder = new BooleanBuilder();
        // title = '1'
        builder.or(board.title.contains("1"));
        // or 연결
        // content = '11'
        builder.or(board.content.contains("11"));
        // where ( title ='1' or content='11')
        query.where(builder);

        // and 연결
        // where (      or     ) and bno > 10
        query.where(board.bno.gt(10L));

        // select * from board where ( title = '1' or content = '11 ) and bno > 10;
        // pageable 0, 10

        this.getQuerydsl().applyPagination(pageable, query);
        // select * from board where ( title = '1' or content = '11 ) and bno > 10 limit ? , ?;

        List<Board> list = query.fetch();
        System.out.println("--------->" + list);

        long count = query.fetchCount();

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        if ((types != null) && (types.length > 0) && keyword != null) {
            BooleanBuilder builder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t":
                        builder.or(board.title.like('%' + keyword + '%'));
                        break;
                    case "c":
                        builder.or(board.content.like('%' + keyword + '%'));
                        break;
                    case "w":
                        builder.or(board.writer.like('%' + keyword + '%'));
                        break;
                    case "tc":
                        builder.or(board.title.like('%' + keyword + '%'));
                        builder.or(board.content.like('%' + keyword + '%'));
                        break;
                    case "tcw":
                        builder.or(board.title.like('%' + keyword + '%'));
                        builder.or(board.content.like('%' + keyword + '%'));
                        builder.or(board.writer.like('%' + keyword + '%'));
                        break;

                }
            }
            query.where(builder);
        }

        query.where(board.bno.gt(0L));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();
        long count = query.fetchCount();

        Page<Board> boardPage = new PageImpl<>(list, pageable, count);

        return boardPage;
    }
}
