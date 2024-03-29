package seabattles.repository;

import seabattles.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepo extends JpaRepository<User, Long> {
    User findByLogin(String login);
    User findByLoginAndPassword(String login, String password);
}
