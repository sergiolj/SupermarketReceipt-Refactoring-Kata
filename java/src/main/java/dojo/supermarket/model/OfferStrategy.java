package dojo.supermarket.model;

import java.util.Optional;

/**
 * Regra de uma promoção aplicada a um único produto do carrinho.
 */
public interface OfferStrategy {

    /**
     * Calcula o desconto da promoção para um produto.
     *
     * @param product   produto em oferta
     * @param quantity  quantidade total do produto no carrinho
     * @param unitPrice preço unitário do produto no catálogo
     * @return o desconto (valor negativo), ou vazio quando a promoção não se aplica
     */
    Optional<Discount> discountFor(Product product, Quantity quantity, Money unitPrice);
}
