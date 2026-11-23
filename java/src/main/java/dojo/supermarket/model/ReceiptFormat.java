package dojo.supermarket.model;

import java.util.Locale;

public class ReceiptFormat {
	
	private static final String DISCRETE_UNIT_FORMAT = "%d";
	private static final String TOTAL_LABEL = "Total: ";
	private static final String PRICE_FORMAT ="%.2f";
	private static final String WEIGHT_FORMAT = "%.3f";
	
	private ReceiptFormat() {
		
	}
	
	public static String price(double value) {
		return String.format(Locale.UK, PRICE_FORMAT, value);
	}
	
	public static String quantity(ReceiptItem item) {
		return item.isProductDiscrete()
        ? String.format(DISCRETE_UNIT_FORMAT, (int) item.quantity())
        : String.format(Locale.UK, WEIGHT_FORMAT, item.quantity());
	}
	
	public static String total() {
		return TOTAL_LABEL;
	}
}
