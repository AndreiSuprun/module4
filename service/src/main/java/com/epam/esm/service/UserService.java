package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.search.SearchCriteria;
import com.epam.esm.service.search.OrderCriteria;

import java.util.List;

public interface UserService{

    public List<UserDTO> searchUsers(List<SearchCriteria> searchParams, List<OrderCriteria> orderParams, PaginationDTO paginationDTO);

    UserDTO find(Long id);
}
