package dojo.supermarket.model;

public enum ProductUnit {
    KILO(false), EACH(true);

    private final boolean discrete;

    ProductUnit(boolean discrete) {
        this.discrete = discrete;
    }

    public boolean isDiscrete() {
        return discrete;
    }

    public boolean acceptsQuantity(double quantity) {
        return !discrete || quantity == Math.floor(quantity);
    }
}