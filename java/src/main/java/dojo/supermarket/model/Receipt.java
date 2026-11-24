package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Receipt {

    private final List<ReceiptItem> items = new ArrayList<>();
    private final List<Discount> discounts = new ArrayList<>();

    public Money getTotalPrice() {
        Money itemsTotal = items.stream().map(ReceiptItem::totalPrice).reduce(Money.ZERO, Money::plus);
        Money discountsTotal = discounts.stream().map(Discount::discountAmount).reduce(Money.ZERO, Money::plus);
        return itemsTotal.plus(discountsTotal);
    }

    public void addProduct(Product p, double quantity, Money price) {
        items.add(new ReceiptItem(p, quantity, price));
    }

    public List<ReceiptItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addDiscount(Discount discount) {
        discounts.add(discount);
    }

    public List<Discount> getDiscounts() {
        return Collections.unmodifiableList(discounts);
    }
}
