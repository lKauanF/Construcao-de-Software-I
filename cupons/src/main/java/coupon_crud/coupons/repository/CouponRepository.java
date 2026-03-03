package coupon_crud.coupons.repository;

import coupon_crud.coupons.model.Coupon;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

  boolean existsByCodeIgnoreCase(String code);

  Optional<Coupon> findByCodeIgnoreCase(String code);
}