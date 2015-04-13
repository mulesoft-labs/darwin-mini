/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package test;

import java.awt.*;
import java.awt.event.*; 
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import java.util.*;
import java.io.*;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.Robot.*;
import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.awt.MouseInfo;

/**
 * Clase cliente Bluetooth para servidor desde dispositivo. Utiliza modo
 * Serial Port Profile (SPP). Y usa la API JSR-82.
 * @author Felipe Diaz,Alejandro Merello
 * @see <a href="http://java.sun.com/javame/reference/apis/jsr082/">API de JSR-82 </a>BlueCove - a JSR-82
 *      implementation</a>
 */
public class ClientePC implements DiscoveryListener{

	/**
     * Constructor. Inicializa un nuevo ClientePC
	 */
	public ClientePC() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
				startDeviceInquiry();
			}
		});
	}

	/**
	 * Método para la búsqueda de dispositivos
	 */
	private void startDeviceInquiry() {
		try {
			deviceList.removeAllElements();
			log("Buscando dispositivos - podría tardar unos segundos...");
			getAgent().startInquiry(inquiryMode, this);
		} catch (Exception e) {log(e);}
	}

	/**
	 * Método para dispositivo encontrado (Implementando DiscoveryListener)
	 * @param btDevice	Dispositivo Bluetooth
	 * @param cod		Clase del dispositivo
	 */
	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
		log("Un dispositivo descubierto (" + getDeviceStr(btDevice) + ")");
		deviceList.addElement(btDevice);
		if (!combo.isEnabled()) {
			combo.setEnabled(true);
			combo.addItemListener(comboSelectionListener);
		}
	}
	
	/**
	 * Método invocado al finalizar la búsqueda de dispositivos (Implementando DiscoveryListener)
	 * @param discType	No es usado en la implementación
	 */
	public void inquiryCompleted(int discType) {
		log("Búsqueda finalizada.Seleccione un dispositivo de la lista inferior.");
	}

	/**
	 * Método para la búsqueda de servicios
	 * @param device Dispositivo al cual se le buscan servicios
	 */
	private void startServiceSearch(RemoteDevice device) {
		try {
			log("Comienza la búsqueda por servicios SSP desde " + getDeviceStr(device));
			UUID uuids[] = new UUID[] { uuid };
			getAgent().searchServices(null, uuids, device, this);
		} catch (Exception e) {log(e);}
	}

	/**
	 * Este método es llamado cuando uno o mas servicios son descubiertos.
	 * Este comienza una tarea que maneja el intercambio de datos con el servidor. (Implementando DiscoveryListener)
	 * @param transId	No es usado por la implementación
	 * @param records	Registro de servicios
	 */
	public void servicesDiscovered(int transId, ServiceRecord[] records) {
		log("Servicio encontrado.");
		for (int i = 0; i < records.length; i++) {
			ServiceRecord rec = records[i];
			String url = rec.getConnectionURL(connectionOptions, false);
			handleConnection(url);
		}
	}

	/**
	 * Este método es llamado cuando uno o mas servicios son descubiertos. (Implementando DiscoveryListener)
	 * @param transID	No es usado por la implementación
	 * @param respCode	Resultado de la búsqueda de servicios
	 */
	public void serviceSearchCompleted(int transID, int respCode) {
		String msg = null;
		switch (respCode) {
		case SERVICE_SEARCH_COMPLETED:
			msg = "La búsqueda de servicios se completo normalmente";
			break;
		case SERVICE_SEARCH_TERMINATED:
			msg = "La búsqueda de servicios requerida fue cancelada por una llamada a DiscoveryAgent.cancelServiceSearch()";
			break;
		case SERVICE_SEARCH_ERROR:
			msg = "Ocurrió un error mientras se procesaba el requerimiento";
			break;
		case SERVICE_SEARCH_NO_RECORDS:
			msg = "No records were found during the service search";//se entiende mas asi...
			break;
		case SERVICE_SEARCH_DEVICE_NOT_REACHABLE:
			msg = "El dispositivo especificado en la búsqueda no pudo ser alcanzado o el dispositivo local no pudo establecer una conexión con el dispositivo remoto";
			break;
		}
		log("Búsqueda de servicios completada - " + msg);
		if (respCode == SERVICE_SEARCH_ERROR) startDeviceInquiry();
	}

	/**
	 * Manejo de la conexión actual.
	 * @param url Dirección del dispositivo remoto
	 */
	private void handleConnection(final String url) {
		Thread echo = new Thread() {
			public void run() {
				StreamConnection stream = null;
				int modo=0,i=0;
				try {
					log("Conectando al servidor con url: " + url);
					stream = (StreamConnection) Connector.open(url);
					log("Bluetooth stream abierto.");
					InputStream in = stream.openInputStream();
					OutputStream out = stream.openOutputStream();
					log("Modo Mouse.");
					while (true) {
						Point actual = MouseInfo.getPointerInfo().getLocation();
						
						int r = in.read();
						out.flush();
						if(modo==0){	//MODO MOUSE
							try {
								Robot robot = new Robot();
								switch(r){
									case 4:	robot.mouseMove(actual.x-5,actual.y);			break;	//4 - Izquierda
									case 6:	robot.mouseMove(actual.x+5,actual.y);			break;	//6 - Derecha
									case 2:	robot.mouseMove(actual.x,actual.y-5);			break;	//2 - Arriba
									case 8:	robot.mouseMove(actual.x,actual.y+5);			break;	//8 - Abajo
									case 5:	modo=1;log("Modo Teclado.");						break;	//cambia  a modo TECLADO
									case 10:robot.mousePress(InputEvent.BUTTON1_MASK);				//* - Mouse Primario
											robot.mouseRelease(InputEvent.BUTTON1_MASK);	break;
									case 11:robot.mousePress(InputEvent.BUTTON3_MASK);				//# - mouse Secundario
											robot.mouseRelease(InputEvent.BUTTON3_MASK);	break;
									case 12:robot.mousePress(InputEvent.BUTTON2_MASK);				//0 - Mouse Centro
											robot.mouseRelease(InputEvent.BUTTON2_MASK);	break;
									case 1: robot.mouseMove(actual.x-5,actual.y-5);			break;	//1 - Arriba-Izquierda
									case 3: robot.mouseMove(actual.x+5,actual.y-5);			break;	//3 - Arriba-Derecha
									case 7: robot.mouseMove(actual.x-5,actual.y+5);			break;	//7 - Abajo-Izquierda
									case 9: robot.mouseMove(actual.x+5,actual.y+5);			break;	//9 - Abajo-Derecha
								}
							} catch (AWTException e) {e.printStackTrace();}
						}else{ //MODO TECLADO
							if(i==0){
							try {
								Robot robot = new Robot();
								switch(r){
									case 4:	robot.keyPress(KeyEvent.VK_LEFT);			break;	//4 - Izquierda
									case 6:	robot.keyPress(KeyEvent.VK_RIGHT);			break;	//6 - Derecha
									case 2:	robot.keyPress(KeyEvent.VK_UP);				break;	//2 - Arriba
									case 8:	robot.keyPress(KeyEvent.VK_DOWN);			break;	//8 - Abajo
									case 5:	modo=0;log("Modo Mouse.");						break; 	//5 - Cambio a modo MOUSE
									case 10:robot.keyPress(KeyEvent.VK_ENTER);			break;	//* - ENTER
									case 11:robot.keyPress(KeyEvent.VK_ESCAPE);			break;	//# - ESCAPE
									case 12:robot.keyPress(KeyEvent.VK_SPACE);			break;   //0 - SPACE
									/*case 1: robot.mouseMove(actual.x-5,actual.y-5);			break;	//1 - Arriba-Izquierda
									case 3: robot.mouseMove(actual.x+5,actual.y-5);			break;	//3 - Arriba-Derecha
									case 7: robot.mouseMove(actual.x-5,actual.y+5);			break;	//7 - Abajo-Izquierda
									case 9: robot.mouseMove(actual.x+5,actual.y+5);			break;	//9 - Abajo-Derecha*/
								}
							} catch (AWTException e) {e.printStackTrace();}
							}
							i=(i+1)%2;
							}
							
						if (r == -1) { log("Se ha cerrado el servidor."); break; }
					}
				} catch (IOException e) { log(e);} 
				finally {
					log("Stream de Bluetooth Cerrado.");
					if (stream != null) {
						try { stream.close(); } 
						catch (IOException e) {	log(e);	}
					}
				}
			}
		};
		echo.start();
	}

	/**
	 * Método para manejar la Interfaz gráfica de usuario (GUI)
	 */
	private void createGUI() {
		JFrame frame = new JFrame("Cliente Bluetooth");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setSize(new Dimension(450, 200));

		JPanel margin = new JPanel(new BorderLayout());
		margin.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		frame.getContentPane().add(margin);

		infoArea = new JTextArea();
		infoArea.setLineWrap(true);
		JScrollPane pane = new JScrollPane(infoArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		margin.add(pane);

		combo = new JComboBox(this.deviceList);
		combo.setEnabled(false);
		combo.setEditable(false);
		combo.setSelectedItem(null);
		combo.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				RemoteDevice remote = (RemoteDevice) value;
				if (remote == null)
					setText("No hay dispositivos cercanos.");
				else
					setText(getDeviceStr(remote));
				return this;
			}
		});
		margin.add(combo, BorderLayout.SOUTH);
		frame.setVisible(true);
	}
	
	/**
	 * Método para llevar el log en la GUI de una información normal
	 * @param msg	Mensaje a poner en la ventana
	 */
	private void log(String msg) {
		infoArea.insert(msg + "\n", infoArea.getDocument().getLength());
	}

	/**
	 * Método para llevar el log en la GUI de una información de excepción
	 * @param e	Excepción para mostrar en el log
	 */
	private void log(Exception e) {
		log(e.getMessage());
		e.printStackTrace();
	}

	/**
	 * Método para entregar un agente de descubrimiento de dispositivos
	 */
	private DiscoveryAgent getAgent() {
		try {
			return LocalDevice.getLocalDevice().getDiscoveryAgent();
		} catch (BluetoothStateException e) {
			log(e);
			log("ERROR detectado. Todas las operaciones detenidas.");
			throw new Error("No discovery agent available.");
		}
	}

	/**
	 * Método para obtener el dispositivo en formato imprimible
	 * @param btDevice	Dispositivo Bluetooth
	 */
	private String getDeviceStr(RemoteDevice btDevice) {
		return getFriendlyName(btDevice) + " - 0x" + btDevice.getBluetoothAddress();
	}
	
	/**
	 * Método para obtener el nombre del dispositivo Bluetooth
	 * @param btDevice	Dispositivo Bluetooth
	 */
	private String getFriendlyName(RemoteDevice btDevice) {
		try {
			return btDevice.getFriendlyName(false);
		} catch (IOException e) {
			return "sin nombre";
		}
	}

	/**
	 *  Método main, invoca una nueva instancia de ClientePC
	 */
	public static void main(String[] args)throws AWTException,IOException{
		new ClientePC();
	}

	// Atributos Bluetooth
	/**
	* Serial Port Profile (Universally Unique Identifier)
	*/
	protected UUID uuid = new UUID(0x1101);
	/**
	* Agente de descubrimiento en modo GIAC (General/Unlimited Inquiry Access Code)
	*/
	protected int inquiryMode = DiscoveryAgent.GIAC;
	/**
	* Tipo de conexión (Sin autentificación ni encriptación)
	*/
	protected int connectionOptions = ServiceRecord.NOAUTHENTICATE_NOENCRYPT;

	//Attributos de la GUI
	/**
	* Area de texto
	*/
	protected JTextArea infoArea = null;
	/**
	* Vector para la lista de dispositivos
	*/
	protected Vector deviceList = new Vector();
	/**
	* ComboBox donde se listan los dispositivos
	*/
	protected JComboBox combo;
	/**
	* ItemListener que actualiza la lista de dispositivos.
	*/
	protected ItemListener comboSelectionListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			startServiceSearch((RemoteDevice) combo.getSelectedItem());
			combo.removeItemListener(this);
			combo.setEnabled(false);
		}
	};
}