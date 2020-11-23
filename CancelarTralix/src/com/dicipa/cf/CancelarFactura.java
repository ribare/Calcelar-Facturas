package com.dicipa.cf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import com.dicipa.cf.Actualizar;
import com.oracle.e1.aisclient.CapabilityException;
import com.oracle.e1.aisclient.JDERestServiceException;

public class CancelarFactura {
    public CancelarFactura() {
        super();
    }

    public static void main(String[] args) throws CapabilityException, JDERestServiceException, IOException {
        final String WS_URL = args[0];
        final String DCT = args[1];
        final String DOC = args[2];
        final String RFC = args [3];
        final String KEY = args[4];
        final String KCO = args[5];
        final String IP_AIS = args[6];
        final String USER_AIS = args[7];
        final String PASS_AIS = args[8];
        
        try{
            //Make a webservice HTTP request
            //https://[XSA_DOMAIN]/xsamanager/cancelCfdWebView?serie=[serie]&folio=[folio]&rfc=[rfc]&key=[key]
            //String wsURL = "https://portal4.xsa.com.mx/xsamanager/cancelCfdWebView?serie=EA&folio=12028&rfc=DCP790511D36&key=5f9fcb77-505f-4018-bacf-26dcb45b96bc";
            String wsURL = WS_URL + "serie=" + DCT + "&folio=" + DOC + "&rfc=" + RFC + "&key=" + KEY;
            URL url = new URL(wsURL);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection)connection;
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            
            //Response WebService.
            String responseString = "";
            String respuesta = "";
            InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            while ((responseString = br.readLine()) != null) {
                respuesta = respuesta + responseString;
            }
            br.close();
            
            System.out.println("\nURL: " + wsURL);
            System.out.println("Compañia: " + KCO);
            System.out.println("Serie: " + DCT);
            System.out.println("Folio: " + DOC);
            System.out.println("RFC: " + RFC);
            System.out.println("Key: " + KEY);
            System.out.println(respuesta);
            System.out.println("\n");
            
            //AGREGAR LOG P5642LCT
            new Actualizar().P5642LCT(IP_AIS, USER_AIS, PASS_AIS, KCO, DCT, DOC, respuesta);
            
        }catch(MalformedURLException urlexc){
            System.out.println("Error URL: " + urlexc.toString());
            }catch(IOException e){
                //AGREGAR LOG P5642LCT
                String exception = "ERROR: " + e.getMessage();
                new Actualizar().P5642LCT(IP_AIS, USER_AIS, PASS_AIS, KCO, DCT, DOC, exception);
                System.out.println("Error: " + e.toString());
            }
    }
}
