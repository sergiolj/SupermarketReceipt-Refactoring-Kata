package dojo.supermarket.model;

import java.util.List;

public class Teller {

    private final SupermarketCatalog catalog;
    private final SpecialOffers offers = new SpecialOffers();

    public Teller(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    public void addSpecialOffer(SpecialOfferType offerType, Product product, double fixedValueOrQuantity) {
        offers.add(product, OfferStrategies.from(offerType, fixedValueOrQuantity));
    }

    public void addSpecialOffer(Product product, OfferStrategy strategy) {
        offers.add(product, strategy);
    }

    /**
     * Cadastra uma oferta de pacote: ao levar uma unidade de cada produto, há o percentual de desconto
     * sobre o total do conjunto.
     */
    public void addBundleOffer(List<Product> products, double percent) {
        offers.addBundle(new BundleOffer(products, percent));
    }

    public Receipt checksOutArticlesFrom(ShoppingCart theCart) {
        Receipt receipt = new Receipt();
        List<ProductQuantity> productQuantities = theCart.getItems();
        
        addItensToReceipt(productQuantities, receipt);
        offers.applyTo(theCart.productQuantities(), catalog, receipt);

        return receipt;
    }
    
    private void addItensToReceipt(List<ProductQuantity> productQuantities, Receipt receipt) {
        for (ProductQuantity productQtd: productQuantities) {
            Product currentProduct = productQtd.product();
            
            double quantity = productQtd.quantity();
            double unitPrice = catalog.getUnitPrice(currentProduct);
            
            receipt.addProduct(currentProduct, quantity, unitPrice);
        }
    }
}
