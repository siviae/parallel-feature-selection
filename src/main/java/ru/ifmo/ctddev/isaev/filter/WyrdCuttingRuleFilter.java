package ru.ifmo.ctddev.isaev.filter;

import ru.ifmo.ctddev.isaev.dataset.Feature;
import ru.ifmo.ctddev.isaev.dataset.FeatureDataSet;
import ru.ifmo.ctddev.isaev.feature.measure.RelevanceMeasure;
import ru.ifmo.ctddev.isaev.result.EvaluatedFeature;
import ru.ifmo.ctddev.isaev.result.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author iisaev
 */
public class WyrdCuttingRuleFilter extends DataSetFilter {

    public FeatureDataSet filterDataSet(FeatureDataSet original, Point measureCosts,
                                        RelevanceMeasure[] measures) {
        List<EvaluatedFeature> filteredFeatures = evaluateFeatures(original, measureCosts, measures).collect(Collectors.toList());
        double mean = filteredFeatures.stream().mapToDouble(EvaluatedFeature::getMeasure).average().getAsDouble();
        double std = Math.sqrt(
                filteredFeatures.stream()
                        .mapToDouble(EvaluatedFeature::getMeasure)
                        .map(x -> Math.pow(x - mean, 2))
                        .average().getAsDouble()
        );
        int inRange = (int) filteredFeatures.stream().filter(f -> isInRange(f, mean, std)).count();
        List<Feature> result = new ArrayList<>(filteredFeatures.subList(0, inRange));
        return new FeatureDataSet(result, original.getClasses(), original.getName());
    }

    private boolean isInRange(EvaluatedFeature feature, double mean, double std) {
        return feature.getMeasure() > (mean - std) && feature.getMeasure() < (mean + std);
    }

}