package ru.ifmo.ctddev.isaev.feature;

import ru.ifmo.ctddev.isaev.Dataset;
import ru.ifmo.ctddev.isaev.EvaluatedFeature;
import ru.ifmo.ctddev.isaev.Feature;
import ru.ifmo.ctddev.isaev.Point;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author iisaev
 */
public class DatasetFilter {
    private final MeasureEvaluator measureEvaluator = new MeasureEvaluator();

    public Dataset filterDataset(Dataset original, Integer preferredSize, Point measureCosts,
                                 RelevanceMeasure... measures) {
        List<Feature> filteredFeatures = measureEvaluator.evaluateFeatureMeasures(original.getFeatures().stream(), original.getClasses(), measureCosts, measures)
                .sorted(Comparator.comparingDouble(EvaluatedFeature::getMeasure))
                .limit(preferredSize)
                .map(EvaluatedFeature::getFeature).collect(Collectors.toList());
        return new Dataset(filteredFeatures, original.getClasses());
    }

}
