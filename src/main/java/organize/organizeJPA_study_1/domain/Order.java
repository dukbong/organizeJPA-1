package organize.organizeJPA_study_1.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import organize.organizeJPA_study_1.domain.base.BaseInfo;
import organize.organizeJPA_study_1.domain.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseInfo {

    @Id @GeneratedValue
    private Long id;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /***
     * Order 저장 시 orderItems 안에 있는 OrderItem 도 저장된다.
     * 주문에 관해서는 Order 객체 하나로 처리하기 위해서 Cascade를 사용한다.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private Order(LocalDateTime orderDate, OrderStatus orderStatus, Member member) {
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.member = member;
    }

    //== 비즈니스 로직 ==//
    public static Order of(LocalDateTime time, Member member, OrderItem... orderItems) {
        Order order = new Order(time, OrderStatus.ORDER, member);
        order.orderItems.addAll(Arrays.asList(orderItems));
        return order;
    }

    public int getTotalPrice() {
        return this.orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }

}
