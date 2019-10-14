package com.neotys.AdunoVisecaProtocol.decoder;

import com.neotys.AdunoVisecaProtocol.common.NeoLoadMessageEncoder;
import com.neotys.extensions.codec.functions.Decoder;

import javax.annotation.Nullable;

public class SoapResponseDecoder  implements Decoder {
    @Nullable
    @Override
    public Object apply(@Nullable byte[] bytes) {
        NeoLoadMessageEncoder messageEncoder =new NeoLoadMessageEncoder();
        messageEncoder.initialize(SoapResponseDecoder.class.getClassLoader());

        try {
            return messageEncoder.getResponse(bytes);
        } catch (Exception e) {
            System.out.println("Issue to decode the response "+ e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
