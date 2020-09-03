package com.shorten.api.stats;

import java.util.List;

/**
 * Statistics calculation method declarations.
 */
public interface Statistics {

    Double calculateMean(List<Double> values);

    Double calculateStd(List<Double> values);
}
