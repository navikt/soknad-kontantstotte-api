package no.nav.kontantstotte.storage.attachment;

import org.apache.tika.Tika;

public class AttachmentToStorableFormatConverter {

    private final ImageConversionService imageConversionService;

    AttachmentToStorableFormatConverter(ImageConversionService imageConversionService) {

        this.imageConversionService = imageConversionService;
    }

    public byte[] toStorageFormat(byte[] input) {
        Format detectedType = Format.fromMimeType(new Tika().detect(input))
                .orElseThrow(() -> new AttachmentConversionException("Kunne ikke konvertere vedleggstypen"));

        if (Format.PDF.equals(detectedType)) {
            return input;
        } else {
            return imageConversionService.convert(input, detectedType);
        }
    }

}
