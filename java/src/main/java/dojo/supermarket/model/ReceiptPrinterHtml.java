package dojo.supermarket.model;

/**
 * Imprime a nota fiscal como uma tabela HTML. Textos vindos de produtos e descontos são escapados.
 */
public class ReceiptPrinterHtml implements ReceiptPrinterFormatter {

	private static final String TABLE_OPEN = "<table class=\"receipt\">\n";
	private static final String TABLE_CLOSE = "</table>\n";

	@Override
	public String printReceipt(Receipt receipt) {
		StringBuilder html = new StringBuilder(TABLE_OPEN);
		receipt.getItems().forEach(item -> html.append(presentItem(item)));
		receipt.getDiscounts().forEach(discount -> html.append(presentDiscount(discount)));
		html.append(row("total", ReceiptFormat.total().trim(), ReceiptFormat.price(receipt.getTotalPrice())));
		return html.append(TABLE_CLOSE).toString();
	}

	private String presentItem(ReceiptItem item) {
		StringBuilder rows = new StringBuilder(
				row("item", item.product().name(), ReceiptFormat.price(item.totalPrice())));
		if (!item.hasSingleQuantity()) {
			String detail = ReceiptFormat.price(item.price()) + " * " + ReceiptFormat.quantity(item);
			rows.append("  <tr class=\"detail\"><td colspan=\"2\">").append(escape(detail)).append("</td></tr>\n");
		}
		return rows.toString();
	}

	private String presentDiscount(Discount discount) {
		String label = discount.description() + "(" + discount.product().name() + ")";
		return row("discount", label, ReceiptFormat.price(discount.discountAmount()));
	}

	private String row(String cssClass, String label, String value) {
		return "  <tr class=\"" + cssClass + "\"><td>" + escape(label) + "</td><td class=\"amount\">"
				+ escape(value) + "</td></tr>\n";
	}

	private static String escape(String text) {
		return text.replace("&", "&amp;")
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("\"", "&quot;")
				.replace("'", "&#39;");
	}
}
