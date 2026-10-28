package com.jordan.skyshoplink.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableSort {

    public static Pageable createPageable(Integer page, Integer size, String sortBy, String direction){
        int pageIndex = page != null ? Math.max((page -1), 0) : 0;
        int pageSize = size != null ? size : 10;

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return PageRequest.of(pageIndex, pageSize, sort);
    }
}
