package com.e_comm.E_comm.repository;

import com.e_comm.E_comm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
