package dojo.supermarket.model;

public record ReceiptItem(Product product, double quantity, Money price) {
	
	public Money totalPrice() {
		return this.price.times(quantity);
	}
	
	public boolean hasSingleQuantity() {
		return quantity == 1;
	}
	
	public boolean isProductDiscrete() {
		return this.product.unit().isDiscrete();
	}
}
