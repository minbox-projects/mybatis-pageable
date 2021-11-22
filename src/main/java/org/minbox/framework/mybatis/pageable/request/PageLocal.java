package org.minbox.framework.mybatis.pageable.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.minbox.framework.mybatis.pageable.Page;
import org.minbox.framework.mybatis.pageable.common.PagingStatus;

/**
 * 本地分页封装对象
 *
 * @author 恒宇少年
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageLocal {
    @Setter
    private PagingStatus pagingStatus;
    private Page<?> page;

    private PageLocal(PagingStatus pagingStatus, Page<?> page) {
        this.pagingStatus = pagingStatus;
        this.page = page;
    }

    public static PageLocal instance(Page<?> page) {
        return new PageLocal(PagingStatus.STARTED, page);
    }
}
