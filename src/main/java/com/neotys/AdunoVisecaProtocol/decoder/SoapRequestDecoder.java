package com.neotys.AdunoVisecaProtocol.decoder;

import com.neotys.AdunoVisecaProtocol.common.NeoLoadMessageEncoder;
import com.neotys.extensions.codec.functions.Decoder;

import javax.annotation.Nullable;
import java.io.UnsupportedEncodingException;

public class SoapRequestDecoder  implements Decoder {
    @Nullable
    @Override
    public Object apply(@Nullable byte[] bytes) {
        NeoLoadMessageEncoder messageEncoder =new NeoLoadMessageEncoder();
        messageEncoder.initialize(SoapRequestDecoder.class.getClassLoader());

        try {
            return messageEncoder.getReques(bytes);
        } catch (Exception e) {
            try {
                String toParse = new String(bytes, "UTF-8");
                System.out.println("Issue to decode  request : "+ toParse+" the requet "+ e.getMessage());

            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
             e.printStackTrace();
        }
        return null;
    }
}
