package dojo.supermarket.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReceiptPrinterHtmlTest {

    private final ReceiptPrinterHtml printer = new ReceiptPrinterHtml();
    private final Product toothbrush = new Product("toothbrush", ProductUnit.EACH);

    @Test
    void emptyReceiptHasOnlyTheTotalRow() {
        assertEquals("<table class=\"receipt\">\n"
                + "  <tr class=\"total\"><td>Total:</td><td class=\"amount\">0.00</td></tr>\n"
                + "</table>\n", printer.printReceipt(new Receipt()));
    }

    @Test
    void singleUnitItemHasNoDetailRow() {
        Receipt receipt = new Receipt();
        receipt.addProduct(toothbrush, 1, Money.of("0.99"));
        assertEquals("<table class=\"receipt\">\n"
                + "  <tr class=\"item\"><td>toothbrush</td><td class=\"amount\">0.99</td></tr>\n"
                + "  <tr class=\"total\"><td>Total:</td><td class=\"amount\">0.99</td></tr>\n"
                + "</table>\n", printer.printReceipt(receipt));
    }

    @Test
    void itemWithQuantityAndDiscountShowsDetailAndDiscountRows() {
        Receipt receipt = new Receipt();
        receipt.addProduct(toothbrush, 3, Money.of("0.99"));
        receipt.addDiscount(new Discount(toothbrush, "3 for 2", Money.of("-0.99")));
        assertEquals("<table class=\"receipt\">\n"
                + "  <tr class=\"item\"><td>toothbrush</td><td class=\"amount\">2.97</td></tr>\n"
                + "  <tr class=\"detail\"><td colspan=\"2\">0.99 * 3</td></tr>\n"
                + "  <tr class=\"discount\"><td>3 for 2(toothbrush)</td><td class=\"amount\">-0.99</td></tr>\n"
                + "  <tr class=\"total\"><td>Total:</td><td class=\"amount\">1.98</td></tr>\n"
                + "</table>\n", printer.printReceipt(receipt));
    }

    @Test
    void escapesHtmlInProductNames() {
        Receipt receipt = new Receipt();
        receipt.addProduct(new Product("<b>Tom & \"Jerry's\"</b>", ProductUnit.EACH), 1, Money.of("1.00"));
        String html = printer.printReceipt(receipt);
        assertTrue(html.contains("&lt;b&gt;Tom &amp; &quot;Jerry&#39;s&quot;&lt;/b&gt;"));
        assertFalse(html.contains("<b>"));
    }
}
