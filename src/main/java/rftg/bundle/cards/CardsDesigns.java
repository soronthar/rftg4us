package rftg.bundle.cards;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import rftg.game.Constants;
import rftg.game.cards.Design;
import rftg.game.cards.Power;

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
                switch (firstChar) {
                    case 'N':
                        index++;
                        designs[index] = new Design(index, line.substring(2));
                        break;
                    case 'T':
                        scanner = new Scanner(line.substring(2)).useDelimiter(":");
                        designs[index].type = scanner.nextInt();
                        designs[index].cost = scanner.nextInt();
                        designs[index].vp = scanner.nextInt();
                        break;
                    case 'E':
                        scanner = new Scanner(line.substring(2)).useDelimiter(":");
                        for (int i = 0; i < Constants.MAX_EXPANSION; i++) {
                            designs[index].expand[i] = scanner.nextInt();
                        }
                        break;
                    case 'F':
                        scanner = new Scanner(line.substring(2)).useDelimiter(" \\| ");
                        designs[index].flags = 0;
                        while (scanner.hasNext()) {
                            Flags flag = Flags.valueOf(scanner.next());
                            designs[index].flags |= flag.getValue();
                        }
                        break;
                    case 'G':
                        designs[index].good_type = GoodType.valueOf(line.substring(2));
                        break;
                    case 'P':
                        scanner = new Scanner(line.substring(2)).useDelimiter(":");

                        Power power = new Power();
                        power.phase = scanner.nextInt();


                        designs[index].powers[designs[index].num_power] = power;
                        designs[index].num_power++;

                        String[] powerFlags = scanner.next().split("\\|");


                        break;
                    case 'V':
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

    //TODO: check boundary: an index that is bigger than the number of designs in the file

    public Design get(int index) {
        return designs[index];
    }
}