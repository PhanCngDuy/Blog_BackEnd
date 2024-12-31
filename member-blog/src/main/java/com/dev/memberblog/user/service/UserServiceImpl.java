package com.dev.memberblog.user.service;
import com.dev.memberblog.common.dto.PagingDTO;
import com.dev.memberblog.common.helper.PagingHelper;
import com.dev.memberblog.files.upload.FilesStorageService;
import com.dev.memberblog.role.model.Role;
import com.dev.memberblog.role.repository.RoleRepository;
import com.dev.memberblog.user.dto.UserDetailsAndRolesDTO;
import com.dev.memberblog.user.dto.UserDetailsDTO;
import com.dev.memberblog.user.dto.UserDetailsUpdateDTO;
import com.dev.memberblog.user.mapper.UserMapper;
import com.dev.memberblog.user.model.User;
import com.dev.memberblog.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FilesStorageService filesStorageService;

    @Override
    public PagingDTO findAll(int page, int limit, String orderBy) {
        Pageable pageable = PagingHelper.getPageable(page,limit,orderBy);
        Page<User> usersPage = repository.findAll(pageable);
         return PagingDTO
                 .builder()
                 .data(usersPage.stream()
                                .map(user -> UserDetailsAndRolesDTO.builder().userDetails(UserMapper.INSTANCE.toUserDetailsDTO(user)).roles(user.getRoles().stream().toList()).build())
                                .collect(Collectors.toList()))
                 .total(usersPage.getTotalElements())
                 .limit(limit)
                 .page(page)
                 .build();
    }

    @Override
    public UserDetailsDTO getUserDetail(String userId) {
        Optional<User> userOpt = repository.findById(userId);
        if(userOpt.isEmpty())
            return null;

        return UserMapper.INSTANCE.toUserDetailsDTO(userOpt.get());
    }
    @Override
    public UserDetailsDTO getUserDetailsByUsername(String username) {
        Optional<User> userOpt = repository.findByUsername(username);
        if(userOpt.isEmpty())
            return null;
        User user = userOpt.get();
        UserDetailsDTO dto =  UserMapper.INSTANCE.toUserDetailsDTO(user);
        return dto;
    }

    @Override
    public UserDetailsDTO updateUserDetails(String userId, UserDetailsUpdateDTO dto) {
        Optional<User> userOpt = repository.findById(userId);

        if(userOpt.isEmpty())
            return null;
        User user = userOpt.get();

        if(dto.getAvatar() != null && !dto.getAvatar().equals(user.getAvatar())){
            String avatar = dto.getAvatar();
            String fileName = filesStorageService.uploadImage(avatar);
            if(fileName != null){
                filesStorageService.delete(user.getAvatar());
                dto.setAvatar(fileName);
            }
        }else{
            dto.setAvatar(user.getAvatar());
        }
        User userUpdated = UserMapper.INSTANCE.updateToUserDetails(dto,user);

        repository.save(userUpdated);
        return UserMapper.INSTANCE.toUserDetailsDTO(userUpdated);
    }

    @Override
    public UserDetailsAndRolesDTO updateUser(String userId, UserDetailsAndRolesDTO dto) {
        Optional<User> userOpt = repository.findById(userId);

        if(userOpt.isEmpty())
            return null;
        User user = userOpt.get();

        if(!dto.getUserDetails().getUsername().equals(user.getUsername())){
            Optional<User> userExisted = repository.findByUsername(dto.getUserDetails().getUsername());
            if(userExisted.isPresent())
                return null;
        }

        if(!dto.getUserDetails().getEmail().equals(user.getEmail())){
            Optional<User> userExisted = repository.findByEmail(dto.getUserDetails().getEmail());
            if(userExisted.isPresent())
                return null;
        }

        UserMapper.INSTANCE.update(dto.getUserDetails(),user);
        Set<Role> roles = new HashSet<>(user.getRoles());

        roles.stream()
                .filter(role -> dto.getRoles().stream().noneMatch(roleDto -> role.getId().equals(roleDto.getId())))
                .forEach(role -> user.removeRole(role));

        dto.getRoles().stream()
                .filter(roleDto -> roles.stream().noneMatch(role -> role.getId().equals(roleDto.getId())))
                .forEach(roleDto -> {
                    Optional<Role> roleOpt = roleRepository.findById(roleDto.getId());
                    roleOpt.ifPresent(role -> user.addRole(role));
                });

        repository.save(user);
        return UserDetailsAndRolesDTO.builder()
                .userDetails(UserMapper.INSTANCE.toUserDetailsDTO(user))
                .roles(user.getRoles().stream().toList())
                .build();
    }
}
