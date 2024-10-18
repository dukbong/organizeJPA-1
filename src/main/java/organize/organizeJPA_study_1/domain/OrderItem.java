package organize.organizeJPA_study_1.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import organize.organizeJPA_study_1.domain.base.BaseInfo;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseInfo {

    @Id @GeneratedValue
    private Long id;

    private int orderPrice;

    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private OrderItem(int orderPrice, int count, Item item) {
        this.orderPrice = orderPrice;
        this.count = count;
        this.item = item;
    }

    //== 비즈니스 로직 ==//
    public static OrderItem of(int orderPrice, int count, Item item) {
        item.removeStock(count);
        return new OrderItem(orderPrice, count, item);
    }

    public int getTotalPrice() {
        return this.orderPrice * this.count;
    }
}
