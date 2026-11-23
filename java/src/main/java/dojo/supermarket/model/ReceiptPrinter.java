package dojo.supermarket.model;

import java.util.stream.Collectors;

public class ReceiptPrinter implements ReceiptPrinterFormatter{

	private static final int DEFAULT_COLUMNS_WIDTH = 40;
	private static final String BLANK_LINE = "\n";
    private final int columns;

    public ReceiptPrinter() {
        this(DEFAULT_COLUMNS_WIDTH);
    }

    public ReceiptPrinter(int columns) {
        this.columns = columns;
    }

    @Override
    public String printReceipt(Receipt receipt) {
    	return presentItems(receipt)
    			+ presentDiscounts(receipt)
    			+ BLANK_LINE
    			+ presentTotal(receipt);
    }

    private String presentItems(Receipt receipt) {
    	return receipt.getItems()
    			.stream()
    			.map(this::presentReceiptItem)
    			.collect(Collectors.joining());
    }
    
    private String presentDiscounts(Receipt receipt) {
    	return receipt.getDiscounts()
    			.stream()
    			.map(this::presentDiscount)
    			.collect(Collectors.joining());
    }

    private String presentReceiptItem(ReceiptItem item) {
        String totalPricePresentation = ReceiptFormat.price(item.totalPrice());
        String name = item.product().name();

        StringBuilder line = new StringBuilder(formatLineLeftAndRightAlignment(name, totalPricePresentation));

        if (!item.hasSingleQuantity()) {
        	line.append("  " + ReceiptFormat.price(item.price()) + " * " + ReceiptFormat.quantity(item) + "\n");
        }
        return line.toString();
    }
    
    private String presentDiscount(Discount discount) {
        String name = discount.description() + "(" + discount.product().name() + ")";
        String value = ReceiptFormat.price(discount.discountAmount());

        return formatLineLeftAndRightAlignment(name, value);
    }

    private String presentTotal(Receipt receipt) {
        String value = ReceiptFormat.price(receipt.getTotalPrice());
        
        return formatLineLeftAndRightAlignment(ReceiptFormat.total(), value);
    }

    private String formatLineLeftAndRightAlignment(String name, String value) {
        StringBuilder line = new StringBuilder();
        line.append(name);
        int whitespaceSize = this.columns - name.length() - value.length();
        line.append(" ".repeat(Math.max(0, whitespaceSize)));
        line.append(value);
        line.append('\n');
        return line.toString();
    }
}
