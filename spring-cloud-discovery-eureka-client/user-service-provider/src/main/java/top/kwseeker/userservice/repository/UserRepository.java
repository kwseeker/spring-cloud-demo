package top.kwseeker.userservice.repository;

import org.springframework.stereotype.Repository;
import top.kwseeker.userapi.domain.User;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

    private ConcurrentMap<Long, User> repository = new ConcurrentHashMap<>();
    private static final AtomicLong idGenerator = new AtomicLong(0);

    public Collection<User> findAll() {
        return repository.values();
    }

    public boolean save(User user) {
        Long id = idGenerator.incrementAndGet();
        user.setId(id);
        return repository.putIfAbsent(id, user) == null;
    }
}
