package com.orderly.restaurantcontactinfoscraper;

import com.factual.driver.Rectangle;
import javafx.util.Pair;

/**
 * Created by joshuaking on 5/4/16.
 */
public class GeoBlock extends Pair<Coordinates, Coordinates> {
    /**
     * Upper Left {@link Coordinates}
     */
    public final Coordinates ul;
    /**
     * Lower Right {@link Coordinates}
     */
    public final Coordinates lr;

    public GeoBlock(Coordinates upperLeft, Coordinates lowerRight) {
        super(upperLeft, lowerRight);

        this.ul = upperLeft;
        this.lr = lowerRight;
    }
    public GeoBlock(Coordinates upperLeft, Double blockSize) {
        super(upperLeft, Coordinates.moveBy(upperLeft, new Coordinates(-blockSize, blockSize)));

        this.ul = upperLeft;
        this.lr = getValue();
    }
    public Rectangle toRectangle() {
        return new Rectangle(ul.lat, ul.lon, lr.lat, lr.lon);
    }

    @Override
    public String toString() {
        return String.format("%s\n%s", ul, lr);
    }
}