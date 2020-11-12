import java.awt.BorderLayout;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pedro Alves
 */
public class AppVeiFinal extends Thread {

    public static final String MULTICAST_ADDRESS = "FF02::1";
    public static final int MULTICAST_PORT = 9999;
    public static ArrayList<String> events = new ArrayList<>();
    public static JTextArea jta = new JTextArea();
    
    public static List<InetAddress> obtainValidAddresses(InetAddress group){
        List<InetAddress> result = new ArrayList<InetAddress>();

        //System.out.println("\nObtain valid addresses according to group address");
        //verify if group is a multicast address
        if (group == null || !group.isMulticastAddress()) return result;

        try{
            //Obtain interfaces list
            Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
            while(ifs.hasMoreElements())
            {
                NetworkInterface ni = ifs.nextElement();
                //ignoringg loopback, inactive interfaces and the interfaces that do not support multicast
                if (ni.isLoopback() || !ni.isUp() || !ni.supportsMulticast()) {
                    //System.out.println("Ignoring Interface: " + ni.getDisplayName());
                    continue;
                }
                Enumeration<InetAddress> addrs = ni.getInetAddresses();
                while(addrs.hasMoreElements())
                {
                    InetAddress addr = addrs.nextElement();
                    //including addresses of the same type of group address
                    if (group.getClass() != addr.getClass()) continue;
                    if ((group.isMCLinkLocal() && addr.isLinkLocalAddress()) || (!group.isMCLinkLocal() && !addr.isLinkLocalAddress()))
                    {
                        //System.out.println("Interface: " + ni.getDisplayName() + " Address: " +addr);
                        result.add(addr);
                    } else {
                        //System.out.println("Ignoring addr: " + addr + " of interface " + ni.getDisplayName());
                    }

                }
            }
        } catch (SocketException ex) {
            System.out.println("Error: " + ex);
        }
        return result;
    }
    
    public void updateEvents() throws ParseException
    {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String t;
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        Date d;
        String h;
        Date d2;
        
        for(int i = 0; i < events.size(); i++)
        {
            String[] x = events.get(i).split(",");
            
            if(x[0].matches("Acidente"))
            {
                t = x[3].substring(0, 8);
                d = dateFormat.parse(t);
                
                h = time.format(formatter);
                d2 = dateFormat.parse(h);
                
                if(d.getMinutes()+2 <= d2.getMinutes())
                {
                    //System.out.println("Aqui " + i);
                    events.remove(i);
                }
            }
            if(x[0].matches("Via Danificada"))
            {
                t = x[3].substring(0, 8);
                d = dateFormat.parse(t);
                
                h = time.format(formatter);
                d2 = dateFormat.parse(h);
                
                if(d.getMinutes()+5 <= d2.getMinutes())
                {
                    //System.out.println("Aqui " + i);
                    events.remove(i);
                }
            }
            if(x[0].matches("Trânsito Condicionado"))
            {
                t = x[3].substring(0, 8);
                d = dateFormat.parse(t);
                
                h = time.format(formatter);
                d2 = dateFormat.parse(h);
                
                if(d.getMinutes()+3 <= d2.getMinutes())
                {
                    //System.out.println("Aqui " + i);
                    events.remove(i);
                }
            }
            if(x[0].matches("Baixas Condições de Visibilidade"))
            {
                t = x[3].substring(0, 8);
                d = dateFormat.parse(t);
                
                h = time.format(formatter);
                d2 = dateFormat.parse(h);
                
                if(d.getMinutes()+2 <= d2.getMinutes())
                {
                    //System.out.println("Aqui " + i);
                    events.remove(i);
                }
            }
            if(x[0].matches("Tornado"))
            {
                t = x[3].substring(0, 8);
                d = dateFormat.parse(t);
                
                h = time.format(formatter);
                d2 = dateFormat.parse(h);
                
                if(d.getMinutes()+10 <= d2.getMinutes())
                {
                    //System.out.println("Aqui " + i);
                    events.remove(i);
                }
            }
            if(x[0].matches("Furacão"))
            {
                t = x[3].substring(0, 8);
                d = dateFormat.parse(t);
                
                h = time.format(formatter);
                d2 = dateFormat.parse(h);
                
                if(d.getMinutes()+10 <= d2.getMinutes())
                {
                    //System.out.println("Aqui " + i);
                    events.remove(i);
                }
            }
            if(x[0].matches("Mau Tempo"))
            {
                t = x[3].substring(0, 8);
                d = dateFormat.parse(t);
                
                h = time.format(formatter);
                d2 = dateFormat.parse(h);
                
                if(d.getMinutes()+5 <= d2.getMinutes())
                {
                    //System.out.println("Aqui " + i);
                    events.remove(i);
                }
            }
        }
    }
    
    public void printEvents()
    {   
        jta.setText(null);
        for(int i = 0; i < events.size(); i++)
        {
            String[] x = events.get(i).split(",");
            jta.append("Evento: " + x[0] + "\n" + "X: " + x[1] + "\n" + "Y: " + x[2] + "\n" + "Hora: " + x[3].substring(0, 8));
            jta.append("\n");
            jta.append("\n");
        }
    }
    
    @Override
    public void run(){
        try{
            while(true){
                Thread.sleep(5 * 1000);
                if(!events.isEmpty())
                {
                    updateEvents();
                    printEvents();
                    System.out.println("Numero de eventos: " + events.size());
                    for(int x = 0; x < events.size(); x++)
                    {
                        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                        MulticastSocket ms = new MulticastSocket();

                        DatagramPacket dp = new DatagramPacket(events.get(x).getBytes(), events.get(x).length(), group, MULTICAST_PORT);

                        List<InetAddress> addrs = obtainValidAddresses(group);
                        for (InetAddress addr: addrs)
                        {
                            //System.out.println("Enviar AppVeiFinal " + events.get(x));
                            ms.setInterface(addr);
                            ms.send(dp);
                        }
                    }
                }
                else
                {
                    jta.setText(null);
                }
            }
        }catch(IOException e){
            System.err.println(""+e);
        } catch (InterruptedException | ParseException ex) {
            Logger.getLogger(AppVeiFinal.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        panel.add(jta);
        frame.add(panel);
        JScrollPane scroll = new JScrollPane(jta); //place the JTextArea in a scroll pane
        panel.add(scroll, BorderLayout.CENTER);
        frame.setSize(500, 500);
        frame.setVisible(true);
        
        try
        {
            AppVeiFinal apf = new AppVeiFinal();
            apf.start();
            
            //ArrayList<String> events = new ArrayList<>();
            InetAddress group = InetAddress.getByName("FF02::1");
            List<InetAddress> addrs = obtainValidAddresses(group);
            
            for (InetAddress addr: addrs) {
                //System.out.println("Address " + addr);
            }

            MulticastSocket ms = new MulticastSocket(9999);
            ms.joinGroup(group);
            byte[] buffer = new byte[8192];
            
            while(true)
            {
                //System.out.println("Waiting for a multicast message");
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                ms.receive(dp);
                String s = new String(dp.getData(), 0, dp.getLength());
                System.out.println("Recebi: " + s);
                String addr = dp.getAddress().toString();
                if(events.isEmpty())
                {
		    //System.out.println("Lista zero");
                    events.add(s);
                }
                else
                {
		    //System.out.println("Tamanho " + events.size());
                    if(!events.contains(s))
                    {
                        events.add(s);
			//System.out.println("evento diferente");
                    }
                }
            }
            
        } catch(IOException e){ System.out.println(""+e); }
    }
    
}