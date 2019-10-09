package com.neotys.AdunoVisecaProtocol.decoder;



import com.neotys.AdunoVisecaProtocol.common.NeoLoadMessageEncoder;
import com.neotys.AdunoVisecaProtocol.datamodel.NeoloadSoapRequest;
import com.neotys.extensions.codec.functions.Encoder;

import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SoapRequestEncoder implements Encoder {


    @Nullable
    @Override
    public byte[] apply(@Nullable Object o) {
        NeoloadSoapRequest request=(NeoloadSoapRequest ) o ;
        NeoLoadMessageEncoder messageEncoder =new NeoLoadMessageEncoder();
        messageEncoder.initialize(SoapRequestEncoder.class.getClassLoader());
        ByteArrayOutputStream outSt = new ByteArrayOutputStream();
        try {
            messageEncoder.writeMessage(outSt,request.getData());
            return  outSt.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
