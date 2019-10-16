package com.neotys.AdunoVisecaProtocol.decoder;

import com.neotys.AdunoVisecaProtocol.common.NeoLoadMessageEncoder;
import com.neotys.extensions.codec.functions.Decoder;

import javax.annotation.Nullable;
import java.io.UnsupportedEncodingException;

public class SoapResponseDecoder  implements Decoder {
    @Nullable
    @Override
    public Object apply(@Nullable byte[] bytes) {
        NeoLoadMessageEncoder messageEncoder =new NeoLoadMessageEncoder();
        messageEncoder.initialize(SoapResponseDecoder.class.getClassLoader());

        try {
            return messageEncoder.getResponse(bytes);
        } catch (Exception e) {
            String toParse = null;
            try {
                toParse = new String(bytes, "UTF-8");
                System.out.println("Issue to decode the response :"+toParse+" exeption "+ e.getMessage());

            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return null;
    }
}
