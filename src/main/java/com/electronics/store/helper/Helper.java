package com.electronics.store.helper;

import com.electronics.store.dtos.PageableResponse;
import com.electronics.store.dtos.UserDto;
import com.electronics.store.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    // just imagibe <U,V>  = u hamara user(entity) hai and v hamara dto hai
    // Class<V>type = ye declare hua sirf iske liye map(object,type) q ki map me source and destination.lass aata hai iske liye batana pada ki <V> ek type ka class hai
    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page,Class<V>type ){
        List<U> entity = page.getContent();
        List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object,type)).collect(Collectors.toList());

        // PageableResponse me ek <V> type ka object banayenge
        PageableResponse<V> response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPges(page.getTotalPages());
        response.setLastPages(page.isLast());
        return response;
    }
}
