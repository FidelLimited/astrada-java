package co.astrada.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import co.astrada.client.model.CreateSubaccountConfigurationsAMEX;
import co.astrada.client.model.CreateSubaccountConfigurationsMASTERCARD;
import co.astrada.client.model.CreateSubaccountConfigurationsVISA;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import org.junit.jupiter.api.Test;

/** countries is a list of country codes or "*": every form is sent and read back as it is. */
class CountriesTest {
    private static final String LIST = "{\"countries\":[\"USA\",\"CAN\"]}";
    private static final String WILDCARD = "{\"countries\":\"*\"}";

    @Test
    void visa() throws Exception {
        assertEquals(LIST, new CreateSubaccountConfigurationsVISA().countries(Arrays.asList("USA", "CAN")).toJson());
        assertEquals(LIST, new CreateSubaccountConfigurationsVISA()
                .countries(new LinkedHashSet<>(Arrays.asList("USA", "CAN"))).toJson());
        assertEquals(WILDCARD, new CreateSubaccountConfigurationsVISA().countries("*").toJson());
        assertEquals(Arrays.asList("USA", "CAN"), CreateSubaccountConfigurationsVISA.fromJson(LIST).getCountries());
        assertEquals("*", CreateSubaccountConfigurationsVISA.fromJson(WILDCARD).getCountries());
    }

    @Test
    void mastercard() throws Exception {
        assertEquals("{\"countries\":[\"USA\"]}",
                new CreateSubaccountConfigurationsMASTERCARD().countries(Collections.singletonList("USA")).toJson());
        assertEquals(WILDCARD, new CreateSubaccountConfigurationsMASTERCARD().countries("*").toJson());
        assertEquals(Arrays.asList("USA", "CAN"), CreateSubaccountConfigurationsMASTERCARD.fromJson(LIST).getCountries());
        assertEquals("*", CreateSubaccountConfigurationsMASTERCARD.fromJson(WILDCARD).getCountries());
    }

    @Test
    void amex() throws Exception {
        assertEquals("{\"countries\":[\"USA\"]}",
                new CreateSubaccountConfigurationsAMEX().countries(Collections.singleton("USA")).toJson());
        assertEquals(WILDCARD, new CreateSubaccountConfigurationsAMEX().countries("*").toJson());
        assertEquals(Arrays.asList("USA", "CAN"), CreateSubaccountConfigurationsAMEX.fromJson(LIST).getCountries());
        assertEquals("*", CreateSubaccountConfigurationsAMEX.fromJson(WILDCARD).getCountries());
    }
}
