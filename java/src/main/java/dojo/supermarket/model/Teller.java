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
            double price = quantity * unitPrice;
            
            receipt.addProduct(currentProduct, quantity, unitPrice, price);
        }
    }
}
