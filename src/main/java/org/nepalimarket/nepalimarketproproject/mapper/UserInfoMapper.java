package org.nepalimarket.nepalimarketproproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.nepalimarket.nepalimarketproproject.dto.UserInfoRequestDto;
import org.nepalimarket.nepalimarketproproject.dto.UserInfoResponseDto;
import org.nepalimarket.nepalimarketproproject.entity.UserInfo;
import org.nepalimarket.nepalimarketproproject.entity.UserRole;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {



    UserInfoResponseDto userInfoToUserInfoResponseDto(UserInfo userInfo);


    UserInfo userInfoRequestDtoToUserInfo(UserInfoRequestDto userInfoRequestDto);


}
