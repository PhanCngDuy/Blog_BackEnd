package com.dev.memberblog.common.helper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PagingHelper {
    public static Pageable getPageable(int page,int limit,String orderBy){
        Sort sort = null;
        if(orderBy != null){
            String[] orderByParts = orderBy.split(":");
            if (orderByParts.length == 2) {
                String sortBy = orderByParts[0];
                String sortOrder = orderByParts[1].toLowerCase();

                Sort.Direction direction = sortOrder.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                sort = Sort.by(direction, sortBy);
            } else {
                sort = Sort.by(orderBy); // Default to ascending order if no specific order is provided
            }
        }

        Pageable pageable = PageRequest.of(page - 1,limit,sort);
        return pageable;
    }
}
