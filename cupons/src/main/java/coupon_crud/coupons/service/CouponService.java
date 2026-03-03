package coupon_crud.coupons.service;

import coupon_crud.coupons.model.Coupon;
import coupon_crud.coupons.model.CouponType;
import coupon_crud.coupons.repository.CouponRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CouponService {

  private final CouponRepository couponRepository;

  private final BigDecimal feePercent;

  public CouponService(
      CouponRepository couponRepository,
      @Value("${service.feePercent:0}") BigDecimal feePercent
  ) {
    this.couponRepository = couponRepository;
    this.feePercent = feePercent == null ? BigDecimal.ZERO : feePercent;
  }

  public Coupon create(Coupon coupon) {
    normalize(coupon);
    validateCoupon(coupon);

    ensureCodeUnique(coupon.getCode(), null);

    coupon.setId(null);
    if (coupon.getUsedCount() == null) {
      coupon.setUsedCount(0);
    }
    return couponRepository.save(coupon);
  }

  public List<Coupon> list() {
    return couponRepository.findAll();
  }

  public Coupon getById(Long id) {
    return couponRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));
  }

  public Coupon update(Long id, Coupon coupon) {
    Coupon existing = getById(id);

    normalize(coupon);
    validateCoupon(coupon);

    ensureCodeUnique(coupon.getCode(), id);

    existing.setCode(coupon.getCode());
    existing.setType(coupon.getType());
    existing.setValue(coupon.getValue());
    existing.setMinCartValue(coupon.getMinCartValue());
    existing.setMaxDiscount(coupon.getMaxDiscount());
    existing.setStartAt(coupon.getStartAt());
    existing.setEndAt(coupon.getEndAt());
    existing.setUsageLimit(coupon.getUsageLimit());
    existing.setActive(coupon.isActive());

    // usedCount não muda aqui (regra de negócio típica). Se quiser, crie endpoint próprio (consume).
    return couponRepository.save(existing);
  }

  public void delete(Long id) {
    Coupon existing = getById(id);

    if (existing.getUsedCount() != null && existing.getUsedCount() > 0) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete coupon with usedCount > 0");
    }

    couponRepository.delete(existing);
  }

  public ApplyResult apply(String code, BigDecimal cartValue) {
    if (code == null || code.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "code is required");
    }
    if (cartValue == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cartValue is required");
    }
    if (cartValue.compareTo(BigDecimal.ZERO) < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cartValue must be >= 0");
    }

    Coupon coupon = couponRepository.findByCodeIgnoreCase(code.trim())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));

    OffsetDateTime now = OffsetDateTime.now();

    if (!coupon.isActive()) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Coupon is inactive");
    }

    if (now.isBefore(coupon.getStartAt()) || now.isAfter(coupon.getEndAt())) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Coupon is outside validity period");
    }

    if (coupon.getUsageLimit() != null && coupon.getUsedCount() != null
        && coupon.getUsedCount() >= coupon.getUsageLimit()) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Coupon usage limit reached");
    }

    BigDecimal serviceFee = percentOf(cartValue, feePercent);
    BigDecimal cartValueWithFee = cartValue.add(serviceFee);

    if (cartValueWithFee.compareTo(coupon.getMinCartValue()) < 0) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cart value is below minimum");
    }

    BigDecimal discount = calculateDiscount(coupon, cartValueWithFee);
    BigDecimal finalValue = cartValueWithFee.subtract(discount);
    if (finalValue.compareTo(BigDecimal.ZERO) < 0) {
      finalValue = BigDecimal.ZERO;
    }

    return new ApplyResult(
        coupon.getCode(),
        scale2(cartValue),
        scale2(serviceFee),
        scale2(cartValueWithFee),
        scale2(discount),
        scale2(finalValue)
    );
  }

  public Coupon consume(Long id) {
    Coupon coupon = getById(id);

    OffsetDateTime now = OffsetDateTime.now();
    if (!coupon.isActive()) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Coupon is inactive");
    }
    if (now.isBefore(coupon.getStartAt()) || now.isAfter(coupon.getEndAt())) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Coupon is outside validity period");
    }
    if (coupon.getUsageLimit() != null && coupon.getUsedCount() != null
        && coupon.getUsedCount() >= coupon.getUsageLimit()) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Coupon usage limit reached");
    }

    int current = coupon.getUsedCount() == null ? 0 : coupon.getUsedCount();
    coupon.setUsedCount(current + 1);
    return couponRepository.save(coupon);
  }

  private void ensureCodeUnique(String code, Long ignoreId) {
    couponRepository.findByCodeIgnoreCase(code)
        .ifPresent(found -> {
          if (ignoreId == null || !Objects.equals(found.getId(), ignoreId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Coupon code already exists");
          }
        });
  }

  private void normalize(Coupon coupon) {
    if (coupon == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body is required");
    }
    if (coupon.getCode() != null) {
      coupon.setCode(coupon.getCode().trim());
    }
    if (coupon.getMinCartValue() == null) {
      coupon.setMinCartValue(BigDecimal.ZERO);
    }
    if (coupon.getUsedCount() == null) {
      coupon.setUsedCount(0);
    }
  }

  private void validateCoupon(Coupon c) {
    if (c.getCode() == null || c.getCode().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "code is required");
    }
    if (c.getType() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "type is required");
    }
    if (c.getValue() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "value is required");
    }
    if (c.getStartAt() == null || c.getEndAt() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startAt and endAt are required");
    }
    if (!c.getStartAt().isBefore(c.getEndAt())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startAt must be before endAt");
    }

    if (c.getMinCartValue() == null || c.getMinCartValue().compareTo(BigDecimal.ZERO) < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minCartValue must be >= 0");
    }

    if (c.getUsageLimit() != null && c.getUsageLimit() <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usageLimit must be > 0 when informed");
    }

    if (c.getType() == CouponType.PERCENT) {
      if (c.getValue().compareTo(new BigDecimal("0.01")) < 0
          || c.getValue().compareTo(new BigDecimal("100.00")) > 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Percent value must be between 0.01 and 100.00");
      }
      if (c.getMaxDiscount() == null || c.getMaxDiscount().compareTo(BigDecimal.ZERO) <= 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "maxDiscount is required and must be > 0 for PERCENT");
      }
    } else if (c.getType() == CouponType.FIXED) {
      if (c.getValue().compareTo(BigDecimal.ZERO) <= 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fixed value must be > 0");
      }
      if (c.getMaxDiscount() != null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "maxDiscount must be null for FIXED");
      }
    }
  }

  private BigDecimal calculateDiscount(Coupon coupon, BigDecimal cartValueWithFee) {
    if (coupon.getType() == CouponType.FIXED) {
      BigDecimal discount = coupon.getValue();
      if (discount.compareTo(cartValueWithFee) > 0) {
        discount = cartValueWithFee;
      }
      return discount;
    }

    // PERCENT
    BigDecimal percentDiscount = cartValueWithFee
        .multiply(coupon.getValue())
        .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);

    if (percentDiscount.compareTo(coupon.getMaxDiscount()) > 0) {
      percentDiscount = coupon.getMaxDiscount();
    }
    if (percentDiscount.compareTo(cartValueWithFee) > 0) {
      percentDiscount = cartValueWithFee;
    }
    return percentDiscount;
  }

  private BigDecimal percentOf(BigDecimal base, BigDecimal percent) {
    if (percent == null) {
      return BigDecimal.ZERO;
    }
    if (percent.compareTo(BigDecimal.ZERO) <= 0) {
      return BigDecimal.ZERO;
    }
    return base.multiply(percent).divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
  }

  private BigDecimal scale2(BigDecimal v) {
    return v.setScale(2, RoundingMode.HALF_UP);
  }

  public record ApplyResult(
      String code,
      BigDecimal cartValue,
      BigDecimal serviceFee,
      BigDecimal cartValueWithFee,
      BigDecimal discount,
      BigDecimal finalValue
  ) {}
}