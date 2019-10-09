package com.neotys.AdunoVisecaProtocol.common;

import com.bsiag.commons.Base64Utility;
import com.bsiag.commons.StringUtility;
import com.bsiag.scout.shared.proxy.http.*;
import com.neotys.AdunoVisecaProtocol.datamodel.NeoLoadExceptionResponse;
import com.neotys.AdunoVisecaProtocol.datamodel.NeoLoadSoapResponse;
import com.neotys.AdunoVisecaProtocol.datamodel.NeoloadSoapRequest;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class NeoLoadMessageEncoder  {
    private static final Pattern beginDataTag = Pattern.compile("[<]([a-zA-Z0-9]+:)?data\\s*>");
    private static final Pattern endDataTag = Pattern.compile("[<][/]([a-zA-Z0-9]+:)?data\\s*>");
    private String m_originAddress;



    public NeoLoadMessageEncoder() {
    }

    public void initialize(ClassLoader rawClassLoader) {

        try {
            this.m_originAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Throwable var3) {
            ;
        }

    }


    public void writeMessage(OutputStream out, Object msg) throws IOException {
        StringBuilder buf = new StringBuilder();
        buf.append("<SOAP-ENV:Envelope SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n");
        buf.append("<SOAP-ENV:Body>\n");
        if (msg instanceof HttpProxyHandlerRequest) {
            HttpProxyHandlerRequest req = (HttpProxyHandlerRequest)msg;
            buf.append("  <request version=\"");
            buf.append(req.getVersion());
            buf.append("\" format=\"");
            buf.append(req.getLocale().toString());
            buf.append("\" language=\"");
            buf.append(req.getNlsLocale().toString());
            buf.append("\" service=\"");
            buf.append(req.getServiceInterfaceClassName());
            buf.append("\" operation=\"");
            buf.append(req.getOperation());
            buf.append("\"/>\n");
        } else if (msg instanceof HttpProxyHandlerResponse) {
            HttpProxyHandlerResponse res = (HttpProxyHandlerResponse)msg;
            if (res.getException() == null) {
                buf.append("  <response status=\"OK\"");
                Object x = res.getData();
                if (x != null) {
                    buf.append(" type=\"" + x.getClass().getSimpleName() + "\"");
                } else {
                    buf.append(" type=\"\"");
                }

                buf.append("/>\n");
            } else {
                buf.append("  <response status=\"ERROR\">\n");
                buf.append("    <exception type=\"" + res.getException().getClass().getSimpleName() + "\">");
                buf.append(res.getException().getMessage());
                buf.append("</exception>\n");
                buf.append("  </response>\n");
            }
        }

        buf.append("  <data>");
        this.setData(buf, msg);
        buf.append("</data>\n");
        buf.append("  <info");
        buf.append(" origin=\"" + this.m_originAddress + "\"");
        buf.append("/>\n");
        buf.append("</SOAP-ENV:Body>");
        buf.append("</SOAP-ENV:Envelope>");



        ((OutputStream)out).write(buf.toString().getBytes("UTF-8"));

        /*finally {
            if (DEBUG) {
                String sentData = ((DebugOutputStream)out).getContent("UTF-8");
                int lastWrittenCharacter = ((DebugOutputStream)out).getLastWrittenCharacter();
                Throwable lastThrownException = ((DebugOutputStream)out).getLastThrownException();
                logger.warn("DEBUG:MessageEncoder lastWrittenCharacter=" + lastWrittenCharacter + ",lastThrownException=" + lastThrownException + ", sentData: " + sentData);
            }

        }*/

    }

    protected void setData(StringBuilder buf, Object msg) throws IOException {
        Deflater deflater = null;
        HttpProxyHandlerOutputStream serialout = null;

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            deflater = new Deflater(1);
            DeflaterOutputStream deflaterStream = new DeflaterOutputStream(bos, deflater);
            serialout = new HttpProxyHandlerOutputStream(deflaterStream);
            serialout.writeObject(msg);
            serialout.flush();
            deflaterStream.finish();
            serialout.close();
            serialout = null;
            String base64Data = StringUtility.wrapText(Base64Utility.encode(bos.toByteArray()), 10000);
            buf.append(base64Data);
        } finally {
            if (serialout != null) {
                try {
                    serialout.close();
                } catch (Throwable var15) {
                    ;
                }
            }

            if (deflater != null) {
                try {
                    deflater.end();
                } catch (Throwable var14) {
                    ;
                }
            }

        }

    }

    public NeoLoadSoapResponse getResponse(byte[] bytes) throws Exception {
        String toParse = new String(bytes, "UTF-8");
        Document SOapDocument;
        ByteArrayInputStream inputStream=new ByteArrayInputStream(bytes);
        if (isSoapLike(toParse))
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();
            SOapDocument = builder.parse(inputStream);
            SOapDocument.getDocumentElement().normalize();
            XPathFactory xpathfactory = XPathFactory.newInstance();

            XPath xpath = xpathfactory.newXPath();
            //----get info------------
            XPathExpression expr = xpath.compile("//info");
            NodeList nodes = (NodeList) expr.evaluate(SOapDocument, XPathConstants.NODESET);
            String origin="";
            for(int i=0;i<nodes.getLength();i++)
            {
                origin=nodes.item(i).getAttributes().getNamedItem("origin").getNodeValue();
            }

            //----get status-----
            XPathExpression exprstatus = xpath.compile("//response");
            NodeList nodesstatus = (NodeList) exprstatus.evaluate(SOapDocument, XPathConstants.NODESET);
            String status="";
            for(int i=0;i<nodesstatus.getLength();i++)
            {
                status=nodesstatus.item(i).getAttributes().getNamedItem("status").getNodeValue();
            }


            HttpProxyHandlerResponse response= (HttpProxyHandlerResponse) readMessage(new ByteArrayInputStream(bytes));
            return new NeoLoadSoapResponse(response,origin,status);
        }
        else
            return null;
    }
    public NeoloadSoapRequest getReques(byte[] bytes) throws Exception {
       String toParse = new String(bytes, "UTF-8");
       Document SOapDocument;
       ByteArrayInputStream inputStream=new ByteArrayInputStream(bytes);
       if (isSoapLike(toParse))
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();
            SOapDocument = builder.parse(inputStream);
            SOapDocument.getDocumentElement().normalize();
            XPathFactory xpathfactory = XPathFactory.newInstance();

            XPath xpath = xpathfactory.newXPath();
            XPathExpression expr = xpath.compile("//info");
            NodeList nodes = (NodeList) expr.evaluate(SOapDocument, XPathConstants.NODESET);
            String origin="";
            for(int i=0;i<nodes.getLength();i++)
            {
                origin=nodes.item(i).getAttributes().getNamedItem("origin").getNodeValue();
            }

            HttpProxyHandlerRequest request= (HttpProxyHandlerRequest) readMessage(new ByteArrayInputStream(bytes));
            return new NeoloadSoapRequest(request,origin);
        }
        else
            return null;
    }
    public static boolean isSoapLike(String inXMLStr) {

        boolean retBool = false;

        // IF WE HAVE A STRING
        if (inXMLStr != null && inXMLStr.trim().length() > 0) {

            // IF WE EVEN RESEMBLE XML
            retBool =inXMLStr.trim().startsWith("<SOAP-ENV:Envelope");

        }
        // ELSE WE ARE FALSE
        return retBool;
    }
    public Object readMessage(InputStream in) throws Exception {


            String dataPart = null;


            Reader r = new BufferedReader(new InputStreamReader((InputStream)in, "UTF-8"));
            StringBuilder buf = new StringBuilder();

            int ch;
            while((ch = r.read()) >= 0) {
                buf.append((char)ch);
            }

            String xml = buf.toString();
            buf.setLength(0);
            Matcher m1 = beginDataTag.matcher(xml);
            Matcher m2 = endDataTag.matcher(xml);
            int lastMatchingIndex = 0;
            if (!m1.find() || !m2.find(m1.start())) {
                throw new IOException("missing a data tag");
            }

            while(true) {
                 lastMatchingIndex = m2.start();
                if (!m2.find()) {
                    dataPart = xml.substring(m1.end(), lastMatchingIndex);
                    break;
                }
            }




        Object res = this.getData(dataPart);
        return res;
    }

    protected Object getData(String dataPart) throws IOException, ClassNotFoundException {
        Inflater inflater = null;
        NeoLoadHttpInputstream serialin = null;

        Object var8;
        try {
            String base64Data = dataPart.replaceAll("[\\n\\r]", "");
            inflater = new Inflater();
            InflaterInputStream inflaterStream = new InflaterInputStream(new ByteArrayInputStream(Base64Utility.decode(base64Data)), inflater);
            serialin = new NeoLoadHttpInputstream(inflaterStream);
            Object res = serialin.readObject();
            var8 = res;
        } finally {
            if (serialin != null) {
                try {
                    serialin.close();
                } catch (Throwable var15) {
                    ;
                }
            }

            if (inflater != null) {
                try {
                    inflater.end();
                } catch (Throwable var14) {
                    ;
                }
            }

        }

        return var8;
    }
}
