package au.org.emii.aatams.detection

import au.org.emii.aatams.QueryBuilder

class DetectionQueryBuilder extends QueryBuilder {
    String getViewName(filterParams) {
        return hasSpeciesFilter(filterParams) ? "detection_by_species_view" : "detection_view"
    }

    boolean hasSpeciesFilter(filterParams) {
        def speciesInFilter = filterParams?.filter?.surgeries?.release?.animal?.species?.in

        return speciesInFilter && (speciesInFilter.size() == 2) && (!speciesInFilter[1].isEmpty())
    }
}
