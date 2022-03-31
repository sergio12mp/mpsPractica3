package ad;

import java.util.ArrayList;
import java.util.List;

/**
 * Board to publish advertisements
 *
 * @author Antonio J. Nebro
 */
public class AdvertisementBoard {
    private static int MAX_BOARD_SIZE = 3;
    private List<Advertisement> advertisementList;

    /**
     * Constructor
     */
    public AdvertisementBoard() {
        advertisementList = new ArrayList<>();
        Advertisement advertisement = new Advertisement(
                "Welcome",
                "This board is intended for your advertisements",
                "THE Company");
        advertisementList.add(advertisement);
    }

    /**
     * Returns the number of advertisements published
     *
     * @return
     */
    public int numberOfPublishedAdvertisements() {
        return advertisementList.size();
    }

    /**
     * Publishes an advertisement. The advertiser called "THE Company" is the owner of the advertisement
     * board, so it can publish freely, without any constraints
     */
    public void publish(Advertisement advertisement,
                        AdvertiserDatabase advertiserDatabase,
                        PaymentDatabase paymentDatabase) {

        if(advertisementList.size()==MAX_BOARD_SIZE){
            throw new RuntimeException();
        }
        if (advertisement.advertiser.equals("THE Company") && (advertisementList.size() < MAX_BOARD_SIZE))
            advertisementList.add(advertisement);
        else {
            for (Advertisement ad : advertisementList) {
                if ((ad.title.equals(advertisement.title) && (ad.advertiser.equals(advertisement.advertiser)))) {

                    return;
                }
            }
            if ((advertiserDatabase.findAdviser(advertisement.advertiser)) &&
                    (paymentDatabase.advertiserHasFunds(advertisement.advertiser)) && (advertisementList.size() < MAX_BOARD_SIZE)) {
                advertisementList.add(advertisement);
                paymentDatabase.advertisementPublished(advertisement.advertiser);

            }
        }
    }

    /**
     * Finds an advertisement form its title
     *
     * @param
     * @return
     */
    public Advertisement findByTitle(String title) {
        Advertisement result = null;
        for (Advertisement advertisement : advertisementList) {
            if (advertisement.title.equals(title))
                result = advertisement;
        }
        return result;
    }

    /**
     * Deletes an advertisement from its name and advertiser
     *
     * @param title
     * @param advertiserName
     */
    public void deleteAdvertisement(String title, String advertiserName) {
        for (Advertisement advertisement : advertisementList) {
            if ((advertisement.title.equals(title) && (advertisement.advertiser.equals(advertiserName)))) {
                advertisementList.remove(advertisement);
            }
        }
    }
}
