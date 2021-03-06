package org.gmdev.pdftrick.manager;

import io.github.giansluca.jargs.Jargs;
import io.github.giansluca.jargs.exception.JargsException;
import org.gmdev.pdftrick.swingmanager.UserInterfaceBuilder;
import org.gmdev.pdftrick.utils.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class PdfTrickStarterTest {

    private static final String HOME_FOR_TEST = "src/test/resources/home-for-test";

    @Test
    void isShouldBuildPdfBag() throws JargsException {
        // Given
        Path fakeHomeFolderPath = Path.
                of(System.getProperty("user.dir") + File.separator + HOME_FOR_TEST);

        String os = SetupUtils.getOs();
        Path fakeNativeLibraryPath = SetupUtils.setAndGetNativeLibrary(fakeHomeFolderPath, os);

        MockedStatic<FileUtils> utilsMock = Mockito.mockStatic(FileUtils.class);
        MockedStatic<UserInterfaceBuilder> userInterfaceBuilderMock = Mockito.mockStatic(UserInterfaceBuilder.class);
        MockedStatic<Messages> messagesMock = Mockito.mockStatic(Messages.class);

        String schema = String.format("%s*, %s*", "os", "version");
        String[] args = {"-os", "mac", "-version", "test-version"};
        Jargs arguments = new Jargs(schema, args);

        // When
        PdfTrickStarter.start(arguments, fakeHomeFolderPath, fakeNativeLibraryPath);

        // Then
        PdfTrickBag bag = PdfTrickBag.INSTANCE;
        assertThat(bag.getNativeLibraryPath()).isEqualTo(fakeNativeLibraryPath);
        assertThat(bag.getNativeObjectManager()).isNotNull();

        // Finally
        utilsMock.close();
        userInterfaceBuilderMock.close();
        messagesMock.close();

        bag.getNativeObjectManager().unloadNativeLib();
        assertThat(fakeNativeLibraryPath.toFile().delete()).isTrue();
    }


}