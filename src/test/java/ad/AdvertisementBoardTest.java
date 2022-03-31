package ad;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.TestExecutionListeners;

class AdvertisementBoardTest {
    @Test
    public void ABoardHasAnAdvertisementWhenItIsCreated() {
        var ad = new Advertisement("titulo", "texto", "anunciador");
        var tablon = new AdvertisementBoard();
        assertNotEquals(0, tablon.numberOfPublishedAdvertisements());
    }

    @Test
    public void PublishAnAdvertisementByTheCompanyIncreasesTheNumberOfAdvertisementsByOne() {
        var ad = new Advertisement("titulo", "texto", "THE Company");
        var tablon = new AdvertisementBoard();
        int previusSize = tablon.numberOfPublishedAdvertisements();

        PaymentDatabase PagoBD = Mockito.mock(PaymentDatabase.class);
        AdvertiserDatabase AnuncianteBD = Mockito.mock(AdvertiserDatabase.class);

        tablon.publish(ad, AnuncianteBD, PagoBD);

        assertEquals(previusSize + 1, tablon.numberOfPublishedAdvertisements());

    }

    @Test
    public void WhenAnAdvertiserHasNoFoundsTheAdvertisementIsNotPublished() {
        var ad = new Advertisement("titulo", "texto", "Pepe Gotera y Otilio");
        var tablon = new AdvertisementBoard();
        int previusSize = tablon.numberOfPublishedAdvertisements();

        PaymentDatabase PagoBD = Mockito.mock(PaymentDatabase.class);
        AdvertiserDatabase AnuncianteBD = Mockito.mock(AdvertiserDatabase.class);

        Mockito.when(AnuncianteBD.findAdviser("Pepe Gotera y Otilio")).thenReturn(false);

        tablon.publish(ad, AnuncianteBD, PagoBD);

        assertEquals(previusSize, tablon.numberOfPublishedAdvertisements());

    }

    @Test
    public void AnAdvertisementIsPublishedIfTheAdvertiserIsRegisteredAndHasFunds() {

        var ad = new Advertisement("titulo", "texto", "Robin Robot");
        var tablon = new AdvertisementBoard();
        int previousSize = tablon.numberOfPublishedAdvertisements();

        PaymentDatabase PagoBD = Mockito.mock(PaymentDatabase.class);
        AdvertiserDatabase AnuncianteBD = Mockito.mock(AdvertiserDatabase.class);

        Mockito.when(AnuncianteBD.findAdviser("Robin Robot")).thenReturn(true);
        Mockito.when(PagoBD.advertiserHasFunds("Robin Robot")).thenReturn(true);
        tablon.publish(ad, AnuncianteBD, PagoBD);

        assertEquals(previousSize + 1, tablon.numberOfPublishedAdvertisements());

    }

    @Test
    public void WhenTheOwnerMakesTwoPublicationsAndTheFirstOneIsDeletedItIsNotInTheBoard() {

        var ad1 = new Advertisement("titulo1", "texto1", "THE Company");
        var ad2 = new Advertisement("titulo2", "texto2", "THE Company");

        var tablon = new AdvertisementBoard();


        PaymentDatabase PagoBD = Mockito.mock(PaymentDatabase.class);
        AdvertiserDatabase AnuncianteBD = Mockito.mock(AdvertiserDatabase.class);

        tablon.publish(ad1, AnuncianteBD, PagoBD);
        tablon.publish(ad2, AnuncianteBD, PagoBD);

        int previousSize = tablon.numberOfPublishedAdvertisements();

        tablon.deleteAdvertisement("titulo1", "THE Company");

        assertNull(tablon.findByTitle("titulo1"));
        assertNotEquals(previousSize - 1, tablon.numberOfPublishedAdvertisements());

    }

    @Test
    public void AnExistingAdvertisementIsNotPublished() {
        var ad1 = new Advertisement("titulo", "texto", "anunciador");
        var ad2 = new Advertisement("titulo", "texto", "anunciador");
        var tablon = new AdvertisementBoard();


        PaymentDatabase PagoBD = Mockito.mock(PaymentDatabase.class);
        AdvertiserDatabase AnuncianteBD = Mockito.mock(AdvertiserDatabase.class);

        Mockito.when(AnuncianteBD.findAdviser("anunciador")).thenReturn(true);
        Mockito.when(PagoBD.advertiserHasFunds("anunciador")).thenReturn(true);

        tablon.publish(ad1, AnuncianteBD, PagoBD);
        tablon.publish(ad2, AnuncianteBD, PagoBD);

        int previousSize = tablon.numberOfPublishedAdvertisements();


        assertEquals(previousSize, tablon.numberOfPublishedAdvertisements());
        Mockito.verify(AnuncianteBD, Mockito.times(1)).findAdviser("anunciador");

    }

    @Test
    public void AnExceptionIsRaisedIfTheBoardIsFullAndANewAdvertisementIsPublished() {


        var ad1 = new Advertisement("titulo1", "texto1", "THE Company");
        var ad2 = new Advertisement("titulo2", "texto2", "THE Company");
        var ad3 = new Advertisement("titulo3", "texto3", "Tim O'Theo");

        var tablon = new AdvertisementBoard();

        PaymentDatabase PagoBD = Mockito.mock(PaymentDatabase.class);
        AdvertiserDatabase AnuncianteBD = Mockito.mock(AdvertiserDatabase.class);

        Mockito.when(AnuncianteBD.findAdviser("Tim O'Theo")).thenReturn(true);
        Mockito.when(PagoBD.advertiserHasFunds("Tim O'Theo")).thenReturn(true);

        tablon.publish(ad1, AnuncianteBD, PagoBD);
        tablon.publish(ad2, AnuncianteBD, PagoBD);
        int previousSize = tablon.numberOfPublishedAdvertisements();

        assertThrows(RuntimeException.class, () -> tablon.publish(ad3, AnuncianteBD, PagoBD));
        assertEquals(previousSize, tablon.numberOfPublishedAdvertisements());

    }
}