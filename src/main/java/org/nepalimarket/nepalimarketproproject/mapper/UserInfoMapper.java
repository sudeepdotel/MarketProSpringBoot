package org.nepalimarket.nepalimarketproproject.mapper;

import org.mapstruct.Mapper;
import org.nepalimarket.nepalimarketproproject.dto.UserInfoRequestDto;
import org.nepalimarket.nepalimarketproproject.dto.UserInfoResponseDto;
import org.nepalimarket.nepalimarketproproject.entity.UserInfo;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {


    UserInfoResponseDto userInfoToUserInfoResponseDto ( UserInfo userInfo );


    UserInfo userInfoRequestDtoToUserInfo ( UserInfoRequestDto userInfoRequestDto );


}
