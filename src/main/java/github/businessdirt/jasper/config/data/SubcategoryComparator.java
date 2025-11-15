package github.businessdirt.jasper.config.data;

import java.util.Comparator;

public class SubcategoryComparator implements Comparator<PropertyData> {

    @Override
    public int compare(PropertyData o1, PropertyData o2) {
        return o1.property().subcategory().compareTo(o2.property().subcategory());
    }
}
