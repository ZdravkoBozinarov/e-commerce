package zdravko.bozinarov.e_commerce.service;

import org.springframework.stereotype.Service;
import zdravko.bozinarov.e_commerce.dto.*;
import zdravko.bozinarov.e_commerce.repository.AdminRepository;

import java.util.List;

@Service
public class AdminService {
    private final AdminRepository repo;

    public AdminService(AdminRepository repo) { this.repo = repo; }

    public List<ProductSalesDTO> getTopSellers(int limit) { return repo.topSellers(limit); }
    public List<MonthlyRevenueDTO> getMonthlyRevenue() { return repo.monthlyRevenue(); }
    public List<LowStockDTO> getLowStock(int threshold) { return repo.lowStock(threshold); }
}
