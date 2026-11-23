package dojo.supermarket.model;

public class ReceiptPrinter implements ReceiptFormatter{


	private static final int DEFAULT_COLUMNS_WIDTH = 40;
    private final int columns;

    public ReceiptPrinter() {
        this(DEFAULT_COLUMNS_WIDTH);
    }

    public ReceiptPrinter(int columns) {
        this.columns = columns;
    }

    @Override
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
