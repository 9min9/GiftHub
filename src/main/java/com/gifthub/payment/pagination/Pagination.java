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
        this.criteria = criteria;

        begin = criteria.getPage();

        if (begin <= 2) {
            begin = 1;
        } else if(begin >= 3) {
            begin = criteria.getPage() - 1;
        }

        end = criteria.getPage() + 2;

        if (end <= 3) {
            end = 4;
        }

        long pages = criteria.getTotalAmount() / criteria.getSize();
        endPage = criteria.getTotalAmount() % criteria.getSize() == 0 ? pages : pages + 1;

        System.out.println(endPage);
    }

}
