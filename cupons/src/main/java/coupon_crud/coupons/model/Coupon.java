package coupon_crud.coupons.model;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "coupons")
public class Coupon {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String code;

 @Enumerated(EnumType.STRING)
@Column(nullable = false, length = 20)
private CouponType type;

  @Column(name = "discount_value", nullable = false, precision = 19, scale = 2)
private BigDecimal value;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal minCartValue = BigDecimal.ZERO;

  // Obrigatório apenas para percentual
  @Column(precision = 19, scale = 2)
  private BigDecimal maxDiscount;

  @Column(nullable = false)
  private OffsetDateTime startAt;

  @Column(nullable = false)
  private OffsetDateTime endAt;

  // Pode ser null (ilimitado)
  private Integer usageLimit;

  @Column(nullable = false)
  private Integer usedCount = 0;

  @Column(nullable = false)
  private boolean active = true;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public CouponType getType() {
    return type;
  }

  public void setType(CouponType type) {
    this.type = type;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getMinCartValue() {
    return minCartValue;
  }

  public void setMinCartValue(BigDecimal minCartValue) {
    this.minCartValue = minCartValue;
  }

  public BigDecimal getMaxDiscount() {
    return maxDiscount;
  }

  public void setMaxDiscount(BigDecimal maxDiscount) {
    this.maxDiscount = maxDiscount;
  }

  public OffsetDateTime getStartAt() {
    return startAt;
  }

  public void setStartAt(OffsetDateTime startAt) {
    this.startAt = startAt;
  }

  public OffsetDateTime getEndAt() {
    return endAt;
  }

  public void setEndAt(OffsetDateTime endAt) {
    this.endAt = endAt;
  }

  public Integer getUsageLimit() {
    return usageLimit;
  }

  public void setUsageLimit(Integer usageLimit) {
    this.usageLimit = usageLimit;
  }

  public Integer getUsedCount() {
    return usedCount;
  }

  public void setUsedCount(Integer usedCount) {
    this.usedCount = usedCount;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}