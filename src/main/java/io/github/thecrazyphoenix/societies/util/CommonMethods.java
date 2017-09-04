package io.github.thecrazyphoenix.societies.util;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.text.Text;

import java.util.regex.Pattern;

public class CommonMethods {
    private static final Pattern NAME_VALIDATOR_PATTERN = Pattern.compile("^[a-zA-Z0-9\\-&#;:.?! ]+$");
    private static final Pattern NAME_TO_ID_PATTERN = Pattern.compile("\\s");

    public static boolean isValidName(String value) {
        return NAME_VALIDATOR_PATTERN.matcher(value).matches();
    }

    public static String nameToID(Text name) {
        return NAME_TO_ID_PATTERN.matcher(name.toPlain()).replaceAll("_");      // Slightly faster than String#replaceAll in the long run.
    }

    public static long getVolume(Vector3i c1, Vector3i c2) {
        // Long casts are to force long arithmetic on all operations.
        return ((long) c2.getX() - c1.getX()) * ((long) c2.getY() - c1.getY()) * ((long) c2.getZ() - c1.getZ());
    }
}
