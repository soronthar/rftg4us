package rftg.bundle.cards;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import rftg.bundle.cards.powers.*;
import rftg.game.Constants;
import rftg.game.cards.Design;
import rftg.game.cards.Power;
import rftg.game.cards.VPBonus;

import java.io.File;
import java.util.Scanner;

public class CardsDesigns {
    //(RAF) not a big fan of this, but it DOES speed up things
    Design[] designs = new Design[Constants.MAX_DESIGN];
    int designCount = 0;

    public CardsDesigns() {
    }

    public int count() {
        return designCount;
    }

    public void loadFrom(String fileName) {
        try {
            File designsFile = new File(getClass().getClassLoader().getResource(fileName).toURI());
            LineIterator lineIterator = FileUtils.lineIterator(designsFile);
            int index = -1;

            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if (line.length() == 0) continue;
                char firstChar = line.charAt(0);

                Scanner scanner;
                Design design;
                switch (firstChar) {
                    case 'N':
                        index++;
                        designs[index] = new Design(index, line.substring(2));
                        break;
                    case 'T':
                        design = designs[index];
                        scanner = new Scanner(line.substring(2)).useDelimiter(":");
                        design.type = scanner.nextInt();
                        design.cost = scanner.nextInt();
                        design.vp = scanner.nextInt();
                        break;
                    case 'E':
                        design = designs[index];
                        scanner = new Scanner(line.substring(2)).useDelimiter(":");
                        for (int i = 0; i < Constants.MAX_EXPANSION; i++) {
                            design.expand[i] = scanner.nextInt();
                        }
                        break;
                    case 'F':
                        design = designs[index];
                        scanner = new Scanner(line.substring(2)).useDelimiter(" \\| ");
                        design.flags = 0;
                        while (scanner.hasNext()) {
                            Flags flag = Flags.valueOf(scanner.next());
                            design.flags |= flag.getValue();
                        }
                        break;
                    case 'G':
                        design = designs[index];
                        design.good_type = GoodType.valueOf(line.substring(2));
                        break;
                    case 'P':
                        design = designs[index];
                        scanner = new Scanner(line.substring(2)).useDelimiter(":");

                        Power power = new Power();
                        power.phase = scanner.nextInt();

                        String[] powerFlags = scanner.next().split("\\|");

                        power.code = 0;

                        long powerCode = 0;
                        for (String powerFlag : powerFlags) {
                            powerCode = findPowerCode(power.phase, powerFlag.trim());
                            power.code |= powerCode;
                        }

                        design.powers[design.num_power] = power;
                        design.num_power++;

                        power.value = scanner.nextInt();
                        power.times = scanner.nextInt();

                        break;
                    case 'V':
                        design = designs[index];
                        scanner = new Scanner(line.substring(2)).useDelimiter(":");
                        VPBonus bonus = new VPBonus();
                        bonus.point = scanner.nextInt();
                        bonus.type = VPBonusType.valueOf(scanner.next());
                        bonus.name = scanner.next();

                        design.bonuses[design.num_vp_bonus] = bonus;
                        design.num_vp_bonus++;
                        break;
                    case ' ':
                    case '#':
                        continue;
                    default:
                        break;
                }
            }
            designCount = index + 1;
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    private long findPowerCode(int phase, String powerFlag) {
        long flagCode = 0;
        switch (phase) {
            case 1:
                flagCode = ExplorePowers.valueOf(powerFlag).getValue();
                break;
            case 2:
                flagCode = DevelopPowers.valueOf(powerFlag).getValue();
                break;
            case 3:
                flagCode = SettlePowers.valueOf(powerFlag).getValue();
                break;
            case 4:
                flagCode = ConsumePowers.valueOf(powerFlag).getValue();
                break;
            case 5:
                flagCode = ProducePowers.valueOf(powerFlag).getValue();
                break;
            default:
                //TODO: manage error in the phase of the power.
                break;
        }
        return flagCode;
    }

    /**
     * Returns a Design given its index. if index out of range, returns null.
     * @param index
     * @return Design for given index or null if index out of bound.
     */
    public Design get(int index) {
    	return (0 <= index && index < designCount-1) ? designs[index] : null;
    }
    
    public Design[] getCache() {
    	//(LAPP) speed up things creating a DesignPile
    	return designs;
    }
}
