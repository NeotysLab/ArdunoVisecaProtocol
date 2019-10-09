package com.neotys.AdunoVisecaProtocol.binder;

import com.neotys.AdunoVisecaProtocol.datamodel.NeoloadSoapRequest;
import com.neotys.AdunoVisecaProtocol.decoder.SoapRequestDecoder;
import com.neotys.AdunoVisecaProtocol.decoder.SoapRequestEncoder;
import com.neotys.AdunoVisecaProtocol.decoder.SoapResponseDecoder;
import com.neotys.extensions.codec.AbstractBinder;
import com.neotys.extensions.codec.predicates.MorePredicates;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.neotys.extensions.codec.predicates.MorePredicates.isRequestEntity;
import static com.neotys.extensions.codec.predicates.MorePredicates.isResponseEntity;

public class NeoloadAdunoBinder extends AbstractBinder {

        private static final String PATH="bsicrm/process";
        @Override
        protected void configure() {

            whenEntity(and(MorePredicates.urlContains(PATH),isResponseEntity())).decodeWith(SoapResponseDecoder.class);
            whenEntity(and(MorePredicates.urlContains(PATH),isRequestEntity())).decodeWith(SoapRequestDecoder.class);
            whenObject(instanceOf(NeoloadSoapRequest.class)).encodeWith(SoapRequestEncoder.class);
        }





}
