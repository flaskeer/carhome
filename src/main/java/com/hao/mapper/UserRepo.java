package com.hao.mapper;

import com.hao.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by user on 2016/2/24.
 */
@Repository
public interface UserRepo {

    Long add(User user);

    void insert(List<User> list);

    void delete(Long id);

    User update(User user);

    List<User> query(User user);

    List<User> queryExistUser(User user);

    User queryOne(Long userId);
}
