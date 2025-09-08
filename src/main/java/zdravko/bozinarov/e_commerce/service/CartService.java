package zdravko.bozinarov.e_commerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zdravko.bozinarov.e_commerce.repository.CartRepository;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository repo;

    public void add(long userId, long productId, int qty) { repo.upsert(userId, productId, qty); }
    public void set(long userId, long productId, int qty) { repo.setQty(userId, productId, qty); }
    public void remove(long userId, long productId) { repo.remove(userId, productId); }
    public void clear(long userId) { repo.clear(userId); }
}
