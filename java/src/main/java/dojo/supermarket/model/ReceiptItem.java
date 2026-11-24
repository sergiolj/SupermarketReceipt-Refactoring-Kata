package dojo.supermarket.model;

public record ReceiptItem(Product product, Quantity quantity, Money price) {
	
	public Money totalPrice() {
		return this.price.times(quantity);
	}
	
	public boolean hasSingleQuantity() {
		return quantity.isOne();
	}
	
	public boolean isProductDiscrete() {
		return this.product.unit().isDiscrete();
	}
}
