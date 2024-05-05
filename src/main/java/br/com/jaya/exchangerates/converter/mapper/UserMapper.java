package br.com.jaya.exchangerates.converter.mapper;

import br.com.jaya.exchangerates.converter.entity.User;
import br.com.jaya.exchangerates.converter.to.UserInBound;
import br.com.jaya.exchangerates.converter.to.UserOutbound;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser (UserInBound userInBound);
    UserOutbound toUserOutbound (User user);
}
