package dojo.supermarket.model;


public record ReceiptItem(Product product, double quantity, double price) {
	
	public double totalPrice() {
		return this.price * quantity;
	}
	
	public boolean hasSingleQuantity() {
		return quantity == 1;
	}
	
	public boolean isProductDiscrete() {
		return this.product.unit().isDiscrete();
	}
}
