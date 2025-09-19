package com.nexi.iso8583.extension.test;

import com.nexi.iso8583.extension.runtime.ISOSerializer;
import io.quarkus.test.QuarkusUnitTest;
import jakarta.inject.Inject;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.iso.packager.ISO93APackager;
import org.jpos.iso.packager.XMLPackager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class Iso8583ExtensionTest {

    // Start unit test with your extension loaded
    private static Class<?>[] testClasses = {
            MyMessage.class,
            ISOSerializer.class
    };

    @Inject
    ISOSerializer isoSerializer;

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
            .withApplicationRoot((jar) -> jar
                    .addAsResource("application.properties")
                    .addClasses(testClasses));

    @Test
    public void writeYourOwnUnitTest() throws ISOException {
        MyMessage myMessage = new MyMessage();
        myMessage.setPan("5425233430109903");
        myMessage.setAmount("500");
        myMessage.setStan("031450");
        myMessage.setLocalTransactionDateTime("241004154301");
        myMessage.setExpirationDate("0131");
        //myMessage.setPosDataCode("100050J00100");
        myMessage.setFunctionCode("100");
        myMessage.setMerchantCategoryCode("5945");
        myMessage.setRetrievalReferenceNumber(ISOSerializer.rrnGenerate());
        MyMessage.DigitalPaymentData paymentData = new MyMessage.DigitalPaymentData();
        paymentData.setDsrpCryptogram("123456789".getBytes());
        myMessage.setDigitalPaymentData(paymentData);
        ISOPackager isoPackager = new ISO93APackager();
        byte[] packed = isoSerializer.serialize(myMessage, isoPackager);
        System.out.println(new String(packed, StandardCharsets.UTF_8));
        // Write your unit tests here - see the testing extension guide https://quarkus.io/guides/writing-extensions#testing-extensions for more information
        Assertions.assertTrue(true, "Add some assertions to " + getClass().getName());
    }

}
