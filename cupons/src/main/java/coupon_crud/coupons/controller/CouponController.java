package coupon_crud.coupons.controller;

import coupon_crud.coupons.model.Coupon;
import coupon_crud.coupons.service.CouponService;
import coupon_crud.coupons.service.CouponService.ApplyResult;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupons")
public class CouponController {

  private final CouponService couponService;

  public CouponController(CouponService couponService) {
    this.couponService = couponService;
  }

  @PostMapping
  public ResponseEntity<Coupon> create(@RequestBody Coupon coupon) {
    Coupon created = couponService.create(coupon);
    return ResponseEntity.status(201).body(created);
  }

  @GetMapping
  public ResponseEntity<List<Coupon>> list() {
    return ResponseEntity.ok(couponService.list());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Coupon> getById(@PathVariable Long id) {
    return ResponseEntity.ok(couponService.getById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Coupon> update(@PathVariable Long id, @RequestBody Coupon coupon) {
    return ResponseEntity.ok(couponService.update(id, coupon));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    couponService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/apply")
  public ResponseEntity<ApplyResult> apply(@RequestBody ApplyRequest request) {
    ApplyResult result = couponService.apply(request.code(), request.cartValue());
    return ResponseEntity.ok(result);
  }

  // Opcional
  @PostMapping("/{id}/consume")
  public ResponseEntity<Coupon> consume(@PathVariable Long id) {
    return ResponseEntity.ok(couponService.consume(id));
  }

  public record ApplyRequest(String code, BigDecimal cartValue) {}
}