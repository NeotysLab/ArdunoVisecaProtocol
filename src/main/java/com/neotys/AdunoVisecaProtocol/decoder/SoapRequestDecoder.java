package com.neotys.AdunoVisecaProtocol.decoder;

import com.neotys.AdunoVisecaProtocol.common.NeoLoadMessageEncoder;
import com.neotys.extensions.codec.functions.Decoder;

import javax.annotation.Nullable;

public class SoapRequestDecoder  implements Decoder {
    @Nullable
    @Override
    public Object apply(@Nullable byte[] bytes) {
        NeoLoadMessageEncoder messageEncoder =new NeoLoadMessageEncoder();
        messageEncoder.initialize(SoapRequestDecoder.class.getClassLoader());

        try {
            return messageEncoder.getReques(bytes);
        } catch (Exception e) {
            System.out.println("Issue to decode  request : "+ bytes.toString()+" the requet "+ e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
