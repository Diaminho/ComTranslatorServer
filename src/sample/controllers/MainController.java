package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import sample.dictionary.Translator;

public class MainController {

    private static SerialPort port;
    private static String text;

    @FXML
    private TextArea logID;

    public MainController(){
    }



    public static void closePort(){
        try {
            if (port!=null && port.isOpened()) {
                port.closePort();
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onLaunchButton(){
        port=new SerialPort("/dev/pts/3");
        openPort(port);
        logID.setText("Запущен сервер\n");
        logID.setText(logID.getText()+"Порт сервера: "+port.getPortName()+"\n");
    }


    private void openPort(SerialPort port){
        try {
            port.openPort();
            port.setEventsMask(SerialPort.MASK_RXCHAR);
            port.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            port.addEventListener(new EventListener());
            //port.addEventListener(new EventListener(), SerialPort.MASK_RXCHAR);
        } catch (SerialPortException ex) {
            System.out.println("There are an error on writing string to port т: " + ex);
        }
    }


    private void sendData(String text){
        try {
            port.writeString(text);
            logID.setText(logID.getText()+"Перевод: "+ text+'\n'+'\n');
        }
        catch (SerialPortException ex) {
            System.out.println("There are an error on writing string to port: " + ex);
        }
    }



    class EventListener implements SerialPortEventListener {

        public void serialEvent (SerialPortEvent event) {
            if (event.isRXCHAR () && event.getEventValue () > 0){
                try {
                    text=port.readString();
                    System.out.println("Получено слово от клиента: "+text);
                    logID.setText(logID.getText()+"Получено слово от клиента: "+text+'\n');
                    text=Translator.doTranslate(text);
                    System.out.println("Перевод: "+text);
                    sendData(text);
                }
                catch (SerialPortException ex) {
                    System.out.println (ex);
                }
            }
        }
    }
}