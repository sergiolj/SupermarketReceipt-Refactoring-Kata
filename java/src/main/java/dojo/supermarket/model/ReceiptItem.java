package dojo.supermarket.model;


public record ReceiptItem(Product product, double quantity, double price, double totalPrice) {
	
	public ReceiptItem(Product product, double quantity, double price) {
		this(product, quantity, price, quantity * price);
	}
	
}
