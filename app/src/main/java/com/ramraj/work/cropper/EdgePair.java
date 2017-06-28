package com.ramraj.work.cropper;

/**
 * Created by ramraj on 28/6/17.
 */

public class EdgePair {
    // Member Variables ////////////////////////////////////////////////////////

    public Edge primary;
    public Edge secondary;

    // Constructor /////////////////////////////////////////////////////////////

    public EdgePair(Edge edge1, Edge edge2) {
        primary = edge1;
        secondary = edge2;
    }
}
