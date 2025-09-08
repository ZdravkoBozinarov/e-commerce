package zdravko.bozinarov.e_commerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    public FlywayConfigurationCustomizer flywayCustomizer() {
        return configuration -> configuration.ignoreMigrationPatterns("*:missing");
    }

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy(
            @Value("${FLYWAY_REPAIR_ON_START:false}") boolean repairOnStart) {
        return flyway -> {
            if (repairOnStart) {
                flyway.repair();
            }
            flyway.migrate();
        };
    }
}
