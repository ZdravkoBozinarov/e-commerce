package zdravko.bozinarov.e_commerce.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zdravko.bozinarov.e_commerce.repository.ProductRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class ProductSeedConfig {
    private final ProductRepository products;

    @Bean
    ApplicationRunner seedProducts(
            @Value("${SEED_PRODUCTS:false}") boolean seed,
            @Value("${PRODUCTS_SEED_COUNT:12}") int count) {
        return args -> {
            if (!seed) return;
            products.ensureOptionalColumns();
            if (products.countAll() > 0) return;

            String[] adjectives = {"Classic","Modern","Essential","Premium","Cozy","Sport","Urban","Vintage","Soft","Bold","Clean","Lightweight"};
            String[] items = {"T-Shirt","Hoodie","Sneakers","Jeans","Jacket","Cap","Backpack","Socks","Sweater","Polo","Shorts","Boots"};
            Random r = new Random(42);
            int n = Math.max(1, Math.min(count, 60));

            for (int i = 0; i < n; i++) {
                String name = adjectives[i % adjectives.length] + " " + items[(i * 7) % items.length];
                String desc = "Comfortable " + items[(i * 5) % items.length] + " for everyday wear.";
                BigDecimal price = BigDecimal.valueOf(19.99 + (i % 10) * 5.0 + r.nextDouble())
                        .setScale(2, RoundingMode.HALF_UP);
                String imageUrl = "https://picsum.photos/seed/p" + i + "/600/450";
                int stock = 40 + (i % 12) * 5;
                products.insert(name, desc, price, imageUrl, stock);
            }
        };
    }
}
