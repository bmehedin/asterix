package org.encoder.asterix.services;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class BaseAsterixService {

    public Set<Integer> sanitiseCompoundAsterixIds(Set<String> asterixIds) {

        Set<Integer> subfieldIds = new HashSet<>();
        Set<String> baseIdsToAdd = new HashSet<>();
        Set<String> compoundIdsToRemove = new HashSet<>();

        for (String asterixId : new HashSet<>(asterixIds)) {

            if (asterixId.contains("-")) {
                String[] parts = asterixId.split("-");
                String baseId = parts[0];
                int subfield = Integer.parseInt(parts[1]);

                compoundIdsToRemove.add(asterixId);
                baseIdsToAdd.add(baseId);
                subfieldIds.add(subfield);
            }
        }

        asterixIds.removeAll(compoundIdsToRemove);
        asterixIds.addAll(baseIdsToAdd);

        return subfieldIds;
    }

}
