package dojo.supermarket.model;

import java.awt.event.ItemEvent;
import java.util.Locale;

public class ReceiptPrinter {

	private static final String TOTAL_LABEL = "Total: ";
	private static final String PRICE_FORMAT ="%.2f";
	private static final String WEIGHT_FORMAT = "%.3f";
	private static final int DEFAULT_COLUMNS = 40;
    private final int columns;

    public ReceiptPrinter() {
        this(DEFAULT_COLUMNS);
    }

    public ReceiptPrinter(int columns) {
        this.columns = columns;
    }

    public String printReceipt(Receipt receipt) {
        StringBuilder result = new StringBuilder();
        for (ReceiptItem item : receipt.getItems()) {
            result.append(presentReceiptItem(item));
        }
        for (Discount discount : receipt.getDiscounts()) {
            result.append(presentDiscount(discount));
        }

        result.append("\n");
        result.append(presentTotal(receipt));
        return result.toString();
    }

    private String presentReceiptItem(ReceiptItem item) {
        String totalPricePresentation = presentPrice(item.totalPrice());
        String name = item.product().name();

        StringBuilder line = new StringBuilder(formatLineWithWhitespace(name, totalPricePresentation));

        if (hasMoreThanOneProduct(item)) {
        	line.append("  " + presentPrice(item.price()) + " * " + presentQuantity(item) + "\n");
        }
        return line.toString();
    }
    
    private boolean hasMoreThanOneProduct(ReceiptItem item) {
    	return item.quantity() > 1;
    }

    private String presentDiscount(Discount discount) {
        String name = discount.description() + "(" + discount.product().name() + ")";
        String value = presentPrice(discount.discountAmount());

        return formatLineWithWhitespace(name, value);
    }

    private String presentTotal(Receipt receipt) {
        String value = presentPrice(receipt.getTotalPrice());
        return formatLineWithWhitespace(TOTAL_LABEL, value);
    }

    private String formatLineWithWhitespace(String name, String value) {
        StringBuilder line = new StringBuilder();
        line.append(name);
        int whitespaceSize = this.columns - name.length() - value.length();
        line.append(" ".repeat(Math.max(0, whitespaceSize)));
        line.append(value);
        line.append('\n');
        return line.toString();
    }

    private static String presentPrice(double price) {
        return String.format(Locale.UK, PRICE_FORMAT, price);
    }

    private static String presentQuantity(ReceiptItem item) {
        return ProductUnit.EACH.equals(item.product().unit())
                ? String.format("%d", (int)item.quantity())
                : String.format(Locale.UK, WEIGHT_FORMAT, item.quantity());
    }
}
