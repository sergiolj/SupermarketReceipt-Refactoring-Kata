package dojo.supermarket.model;


public record ReceiptItem(Product product, double quantity, double price) {
	
	public double totalPrice() {
		return this.price * quantity;
	}
	
}
