package got.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GotHelper {

    public static List<Family> shuffleTheFamilies(int num) {
        List<Family> families = new ArrayList<Family>();
        for (int i = 0; i < num; i++) {
            families.add(Family.values()[i]);
        }
        Random rand = new Random(System.currentTimeMillis());
        Collections.shuffle(families, rand);
        return families;
    }
}
