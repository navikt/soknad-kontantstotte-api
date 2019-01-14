package no.nav.kontantstotte.storage.attachment;

import no.nav.kontantstotte.storage.StorageException;

public class AttachmentConversionException extends StorageException {

    public AttachmentConversionException(String message) {
        super(message);
    }

    public AttachmentConversionException(String message, Throwable cause) {
        super(message, cause);
    }

}
