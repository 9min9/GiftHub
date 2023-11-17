package com.gifthub.payment.pagination;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagination {

    private static final int SHOW_PAGE = 4;

    private Criteria criteria;

    // 보여줄 페이지의 시작과 끝
    private long begin;
    private long end;

    // 마지막 페이지
    private long endPage;

    public Pagination(Criteria criteria) {
        begin = 1;

        end = 1 + SHOW_PAGE - 1;

        long pages = criteria.getTotalAmount() / criteria.getSize();
        endPage = pages == 0 ? pages : pages + 1;
    }

}
