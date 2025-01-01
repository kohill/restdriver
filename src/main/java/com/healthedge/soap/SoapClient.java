package com.healthedge.soap;

import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * This class should be extended in a given project to be used for a specific endpoint/context
 */
public abstract class SoapClient {

    public final SoapContext soapContext = new SoapContext();

    public SOAPMessage getEmptySoapMessageObj() {
        try {
            return MessageFactory.newInstance().createMessage();
        } catch (SOAPException e) {
            throw new SOAPException(e.getMessage());
        }
    }

    public String convertSOAPMessageToXMLString(SOAPMessage soapMessage) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try {
            soapMessage.writeTo(stream);
        } catch (IOException | SOAPException e) {
            throw new SOAPException(e.getMessage());
        }

        return stream.toString();
    }

    public SOAPMessage getSOAPMessageFromRawString(String rawXmlString) {
        try {
            MessageFactory factory = MessageFactory.newInstance();
            return factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(rawXmlString.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException | SOAPException e) {
            throw new SOAPException(e.getMessage());
        }
    }

}
