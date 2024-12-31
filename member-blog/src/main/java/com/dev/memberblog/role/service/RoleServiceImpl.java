package com.dev.memberblog.role.service;

import com.dev.memberblog.role.dto.RoleDTO;
import com.dev.memberblog.role.mapper.RoleMapper;
import com.dev.memberblog.role.model.Role;
import com.dev.memberblog.role.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository repository;

    @Override
    public List<Role> findAll() {
        return repository.findAll().stream().toList();
    }

    @Override
    public RoleDTO save(RoleDTO dto){
        Role role = RoleMapper.INSTANCE.toEntity(dto);
        repository.save(role);
        return RoleMapper.INSTANCE.toDTO(role);
    }

    @Override
    public boolean removeRole(String roleId) {
        Optional<Role> roleOpt = repository.findById(roleId);
        if(roleOpt.isPresent()) {
            repository.delete(roleOpt.get());
            return true;
        }
        return false;
    }

    @Override
    public RoleDTO updateRole(String roleId, RoleDTO dto) {
        Optional<Role> roleOpt = repository.findById(roleId);
        if(roleOpt.isEmpty())
            return null;

        Role role = roleOpt.get();
        if(!dto.getCode().equals(role.getCode())){
            Optional<Role> roleNameExisted = repository.findByCode(dto.getCode());
            if(roleNameExisted.isPresent())
                return null;
        }
        role.setCode(dto.getCode());
        role.setDescription(dto.getDescription());
        repository.save(role);

        return RoleMapper.INSTANCE.toDTO(role);
    }

}
