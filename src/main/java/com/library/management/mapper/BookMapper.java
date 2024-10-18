package com.library.management.mapper;

import com.library.management.model.Book;
import com.library.management.model.BookBean;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface BookMapper {

    @Mapping(target = "initialCopies", source = "availableCopies")
    BookBean toBean(final Book book);

    Book toPojo(final BookBean bean);

}
