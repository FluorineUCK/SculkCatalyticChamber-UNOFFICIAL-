package net.dragonegg.sculkcatalyticchamber.registry;

import com.jozufozu.flywheel.core.PartialModel;
import net.dragonegg.sculkcatalyticchamber.SculkCatalyticChamber;

public class PartialModelRegistry {

    public static final PartialModel MECHANICAL_SHRIEKER_COG =
            new PartialModel(SculkCatalyticChamber.asResource("block/mechanical_shrieker/inner"));

    public static void init() {
        // init static fields
    }

}
