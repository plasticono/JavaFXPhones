package sample.phones;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import sample.Main;

import javax.imageio.ImageIO;
import java.io.*;

public class Phone implements Serializable {
    static private final long serialVersionUID = 3L;

    protected String name;
    protected String year;
    protected String os;
    private transient Image image;

    public Phone(String name, String year, String os) {
        this.name = name;
        this.year = year;
        this.os = os;
    }

    public void say() {
        Main.print(name);
    }

    private void writeObject(ObjectOutputStream outStream) throws IOException {
        // Do generic object write first
        outStream.defaultWriteObject();
        if(image != null) {
            outStream.writeLong(200L);
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outStream);
            Main.print("writing image to object: " + name);
            outStream.writeLong(900L);
        } else {
            outStream.writeLong(800L);
        }
        // 3. Write a long number to create buffer between albums objects and avatar image

    }

    private void readObject(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
        // Do generic object read first
        //inStream.readLong();
        inStream.defaultReadObject();
        long magicNumber = inStream.readLong();
        if (magicNumber != 800) {
            Image image = SwingFXUtils.toFXImage(ImageIO.read(inStream), null);
            this.setImage(image);
            inStream.readLong();
        }

    }

    public static void save(String path) {
        try {
            // Save model
            FileOutputStream file = new FileOutputStream(path);
            serializePhones(file);
            file.close();
            // Update status
            Main.print("All data saved to: " + path);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public static void serializePhones(FileOutputStream outputStream) {
        try {
            // Objects/values will be written to this object stream
            ObjectOutputStream out = new ObjectOutputStream(outputStream);

            // 1. Save the number of albums in list
            out.writeInt(Main.phones.size());
            // 2. Save each album object
            //out.writeLong(100L);
            for (Phone phone : Main.phones) {
                out.writeObject(phone);
            }
            // 3. Write a long number to create buffer between albums objects and avatar image
            //out.writeLong(500L);

            // Done saving; close the object stream
            out.close();
        } catch (Exception ex) {
            System.out.println("Save exception: ");
            ex.printStackTrace();
        }
    }

    public static void restoreData(FileInputStream file) {

        // deserialize the program's data in this order:
        // 1. Number of albums in albums list
        // 2. Each individual album object
        // 3. The avatar image
        try {
            // Objects/values will be read from this object stream
            ObjectInputStream in = new ObjectInputStream(file);
            // 1. Restore the amount of albums in list
            int numberOfAlbums = in.readInt();
            // 2. Restore each album object
            //in.readLong();
            for (int i = 0; i < numberOfAlbums; i = i + 1) {
                Phone nextPhone = (Phone) in.readObject();
                /*try{
                    try {
                        Image image = SwingFXUtils.toFXImage(ImageIO.read(in), null);
                        nextPhone.setImage(image);
                        Main.print("found image for " + nextPhone.getName());
                    }catch (NullPointerException e) {
                        Main.print("Image not found");
                    }
                }catch (IOException e) {
                    Main.print("Image corrupted or not found for " + in.toString());
                }*/
                Main.phones.add(nextPhone);
            }
            // 3. Read and ignore the long buffer between albums objects and avatar image
            //in.readLong();
            // Done restoring data; close the object stream
            in.close();
        } catch (Exception ex) {
            System.out.println("Restore exception: ");
            ex.printStackTrace();
        }
    }

    public static void loadAndroid(File file){
        final String androidFile = Main.loadFile(file.getPath());

        final String[] androidTokens = androidFile.split("~");


        for (String string : androidTokens) {
            String[] parts = string.split("#");
            try {

                String name = parts[0];

                String year = parts[1];
                String os = parts[2];
                float size = Float.parseFloat(parts[3]);
                String displaySize = parts[4];
                String CPU = parts[5];
                String GPU = parts[6];
                String RAM = parts[7];
                String storage = parts[8];

                if (name != null && year != null && os != null && (Float) size != null && displaySize != null && CPU != null && GPU != null && storage != null) {

                    Samsung samsung = new Samsung(name, year, os, size, displaySize, CPU, GPU, RAM, storage);
                    //samsung.say();
                    Main.phones.add(samsung);
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            }

        }
    }
    public static void loadiPhones(File file) {
        final String iPhoneFile = Main.loadFile(file.getPath());

        final String[] iPhoneTokens = iPhoneFile.split("~");


        for (String string : iPhoneTokens) {
            String[] parts = string.split("#");
            try {

                String name = parts[0];

                String os = parts[1];
                String releaseDate = parts[2];
                String discontinued = parts[3];
                String ended = parts[4];
                String finalOs = parts[5];
                String minLifespan = parts[6];
                String maxLifespan = parts[7];
                String price = parts[8];

                if (name != null && releaseDate != null && os != null && discontinued != null && ended != null && finalOs != null && minLifespan != null && maxLifespan != null && price != null) {

                    Integer newPrice = Integer.parseInt(price.replaceAll("\\$", ""));
                    iPhone iPhone = new iPhone(name, releaseDate, os, discontinued, ended, finalOs, minLifespan, maxLifespan, newPrice);
                    Main.phones.add(iPhone);
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                //e.printStackTrace();
            }

        }

    }

    public static Phone getPhoneByDisplayedName(String displayedName) {
        //iphone 4 - 2015
        for (Phone phone : Main.phones) {
            if (displayedName.length() > 4) {
                if (displayedName.substring(0, displayedName.length() - 7).equalsIgnoreCase(phone.getName())) {
                    return phone;
                }
            }
        }
        return null;
    }

    public int getYearFromDate() {
        if (year.length() > 4) {
            int newYear;
            newYear = Integer.parseInt(year.substring(year.length() - 4));
            return newYear;
        } else {
            return Integer.parseInt(year);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
