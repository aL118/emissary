package emissary.id;

import emissary.place.IServiceProviderPlace;
import emissary.test.core.junit5.IdentificationTest;

import org.junit.jupiter.params.provider.Arguments;

import java.io.IOException;
import java.util.stream.Stream;

class UnixFilePlaceTest extends IdentificationTest {

    public static Stream<? extends Arguments> data() {
        return getMyTestParameterFiles(UnixFilePlaceTest.class);
    }

    @Override
    public IServiceProviderPlace createPlace() throws IOException {
        return new UnixFilePlace();
    }
}
