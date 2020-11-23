package com.dicipa.cf;

import com.oracle.e1.aisclient.AISClientUtilities;
import com.oracle.e1.aisclient.CapabilityException;
import com.oracle.e1.aisclient.FSREvent;
import com.oracle.e1.aisclient.FormRequest;
import com.oracle.e1.aisclient.GridAction;
import com.oracle.e1.aisclient.GridRowInsertEvent;
import com.oracle.e1.aisclient.GridRowUpdateEvent;
import com.oracle.e1.aisclient.JDERestServiceException;
import com.oracle.e1.aisclient.JDERestServiceProvider;
import com.oracle.e1.aisclient.LoginEnvironment;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Actualizar {
    public Actualizar() {
        super();
    }
    
    public void P5642LCT(String ipAIS, String userAIS, String passAIS, String KCO, String DCT, String DOC, String respuestaTralix) throws CapabilityException, IOException,
                                                                               JDERestServiceException {
        //Parametros para Login de AIS Server
        final String AIS_SERVER = ipAIS;
        final String USER_NAME = userAIS;
        final String PASSWORD = passAIS;
        final String DEVICE = "Java";
        String respuesta = "CANCELADO";
        String estatus = "NO PROCESADO";
        //Cargar capacidades
        final String REQ_CAPABILITIES = "grid";
        
        try{
            //Login de AIS Server
            LoginEnvironment loginEnv = new LoginEnvironment(AIS_SERVER, USER_NAME, PASSWORD, DEVICE, REQ_CAPABILITIES);
            
            //Informacion de solicitud
            FormRequest formRequest = new FormRequest(loginEnv);
            formRequest.setFormName("P5642LCT_W5642LCTC");
            //formRequest.setVersion("ZJDE0001");
            formRequest.setFormServiceAction("U");

            
            FSREvent w5642LCTCInsertFSREvent = new FSREvent();
            w5642LCTCInsertFSREvent.setFieldValue("15", KCO); //Compañia
            w5642LCTCInsertFSREvent.setFieldValue("17", DCT); //Tipo de Documento
            w5642LCTCInsertFSREvent.setFieldValue("19", DOC); //No. Documento
            w5642LCTCInsertFSREvent.setFieldValue("22", "1"); //No. de Línea
            w5642LCTCInsertFSREvent.setFieldValue("24", respuestaTralix); //Respuesta Tralix
            //Fecha
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            w5642LCTCInsertFSREvent.setFieldValue("26", dateFormat.format(date)); //Fecha
            //Hora
            DateFormat hourFormat = new SimpleDateFormat("hh:mm:ss");
            w5642LCTCInsertFSREvent.setFieldValue("28", hourFormat.format(date));//Hora
            w5642LCTCInsertFSREvent.setFieldValue("30", "AUTO"); //Tipo Estatus
            //Estatus F4211
            if(respuesta.equals(respuestaTralix)){
                estatus = "PROCESADO";
                w5642LCTCInsertFSREvent.setFieldValue("32", estatus);
            }else{
                w5642LCTCInsertFSREvent.setFieldValue("32", estatus);
            }
            
            //Acción botón OK 
            w5642LCTCInsertFSREvent.doControlAction("11"); 
            
            //add the events to the form request
            formRequest.addFSREvent(w5642LCTCInsertFSREvent);
            
            String response = JDERestServiceProvider.jdeRestServiceCall(loginEnv, formRequest, JDERestServiceProvider.POST_METHOD, JDERestServiceProvider.FORM_SERVICE_URI);
            
            if(!response.isEmpty()){
            System.out.println("Log de Cancelación Agregada");
            }
            
            //***********************************************
            //ACTUALIZACION ORDEN EN F4211
            //***********************************************
            if (respuesta.equals(respuestaTralix)){
                    try {
                        
                    //Informacion de solicitud
                    FormRequest formRequest4211 = new FormRequest(loginEnv);
                    formRequest4211.setFormName("P5642LCT_W5642LCTG");
                    //formRequest.setVersion("ZJDE0001");
                    formRequest4211.setFormServiceAction("U");
                    
                    FSREvent w5642LCTGUpdateFSREvent = new FSREvent();
                    w5642LCTGUpdateFSREvent.setFieldValue("17", KCO);
                    w5642LCTGUpdateFSREvent.setFieldValue("19", DOC);
                    w5642LCTGUpdateFSREvent.setFieldValue("21", DCT);
                        
                    w5642LCTGUpdateFSREvent.doControlAction("28");// Boton FIND
                    w5642LCTGUpdateFSREvent.setFieldValue("32", "CA");
                    
                    w5642LCTGUpdateFSREvent.doControlAction("12"); //Boton OK
                    
                    formRequest4211.addFSREvent(w5642LCTGUpdateFSREvent);
                    
                    String response4211 = JDERestServiceProvider.jdeRestServiceCall(loginEnv,formRequest4211,JDERestServiceProvider.POST_METHOD,JDERestServiceProvider.FORM_SERVICE_URI);
                    
                    if(!response4211.isEmpty()){
                        System.out.println("\nOrden Actualizada en F4211");
                    }
                    
                }catch(Exception e){
                    System.out.println(e.toString());
                }
            }
            
            //Logout AIS
            AISClientUtilities.logout(loginEnv);
            
        }catch (JDERestServiceException exp) {
            String error = JDERestServiceProvider.handleServiceException(exp);
            System.out.println(error);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
