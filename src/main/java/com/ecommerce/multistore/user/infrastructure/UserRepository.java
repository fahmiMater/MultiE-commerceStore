package com.ecommerce.multistore.user.infrastructure;

import com.ecommerce.multistore.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByPhone(String phone);
    
    Optional<User> findByDisplayId(String displayId);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);
}
