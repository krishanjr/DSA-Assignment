// Traffic Signal Management System (Multithreaded)
// Functionality:
//  Queue (FIFO Scheduling): Manage vehicles at a traffic signal.
//  Priority Queue (Emergency Vehicles): Give priority to ambulances and fire trucks.
//  Multithreading:
// o Separate threads for traffic light changes, vehicle movement, and emergency
// handling.
// GUI:
//  An animated traffic intersection.
//  A queue showing waiting vehicles.
//  Buttons to:
// o Change Traffic Signal (Simulates signal changes in real-time).
// o Add Vehicles (Continuously add vehicles with a thread).
// o Enable Emergency Mode (Emergency vehicle gets priority in multithreaded execution).
// Implementation:
//  Main thread: Handles GUI and user inputs.
//  Traffic light thread: Changes signals at fixed intervals.
//  Vehicle queue thread: Processes vehicles using FIFO and priority queue logic.
// Data Structures:
//  Queue: Regular vehicle queue.
//  Priority Queue: Emergency vehicle handling.
// Multithreading Benefits:
//  Vehicles move in real-time without blocking GUI updates.
//  Traffic lights operate independently of vehicle movement.



package Question6;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;


// Vehicle class with emergency priority
class Vehicle implements Comparable<Vehicle> {
    String type;
    boolean isEmergency;

    public Vehicle(String type, boolean isEmergency) {
        this.type = type;
        this.isEmergency = isEmergency;
    }

    public String toString() {
        return (isEmergency ? "🚨" : "🚗") + " " + type;
    }

    @Override
    public int compareTo(Vehicle other) {
        return Boolean.compare(other.isEmergency, this.isEmergency);
    }
}

// Vehicle Manager with queue and priority queue
class VehicleManager {
    Queue<Vehicle> regularQueue = new LinkedList<>();
    PriorityQueue<Vehicle> emergencyQueue = new PriorityQueue<>();

    public synchronized void addVehicle(Vehicle v) {
        if (v.isEmergency) emergencyQueue.add(v);
        else regularQueue.add(v);
    }

    public synchronized Vehicle getNextVehicle() {
        if (!emergencyQueue.isEmpty()) return emergencyQueue.poll();
        return regularQueue.poll();
    }

    public synchronized List<Vehicle> getAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        list.addAll(emergencyQueue);
        list.addAll(regularQueue);
        return list;
    }
}

// Traffic signal controller thread
class SignalController extends Thread {
    private volatile boolean greenLight = true;

    public void run() {
        while (true) {
            greenLight = true;
            System.out.println("🟢 GREEN Light");
            try { Thread.sleep(5000); } catch (Exception e) {}
            greenLight = false;
            System.out.println("🔴 RED Light");
            try { Thread.sleep(3000); } catch (Exception e) {}
        }
    }

    public boolean isGreen() {
        return greenLight;
    }
}

// Vehicle processor thread
class VehicleProcessor extends Thread {
    private VehicleManager manager;
    private SignalController signal;
    private JTextArea logArea;
    private Runnable updateQueue;

    public VehicleProcessor(VehicleManager manager, SignalController signal,
                            JTextArea logArea, Runnable updateQueue) {
        this.manager = manager;
        this.signal = signal;
        this.logArea = logArea;
        this.updateQueue = updateQueue;
    }

    public void run() {
        while (true) {
            if (signal.isGreen()) {
                Vehicle v = manager.getNextVehicle();
                if (v != null) {
                    String msg = "⏩ " + v + " passed the intersection.\n";
                    SwingUtilities.invokeLater(() -> {
                        logArea.append(msg);
                        updateQueue.run();
                    });
                }
            }
            try { Thread.sleep(1000); } catch (Exception e) {}
        }
    }
}

// 🚦 Main GUI class renamed to Question6
public class Question6 extends JFrame {
    private VehicleManager manager = new VehicleManager();
    private SignalController signalThread = new SignalController();
    private JTextArea queueArea = new JTextArea(10, 30);
    private JTextArea logArea = new JTextArea(10, 30);

    public Question6() {
        super("🚦 Traffic Signal Management System");

        JButton addCarBtn = new JButton("Add Regular Vehicle");
        JButton addEmergencyBtn = new JButton("Add Emergency Vehicle");

        addCarBtn.addActionListener(_ -> {
            manager.addVehicle(new Vehicle("Car", false));
            updateQueueDisplay();
        });

        addEmergencyBtn.addActionListener(_ -> {
            manager.addVehicle(new Vehicle("Ambulance", true));
            updateQueueDisplay();
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(addCarBtn);
        controlPanel.add(addEmergencyBtn);

        queueArea.setEditable(false);
        logArea.setEditable(false);

        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(queueArea), BorderLayout.CENTER);
        add(new JScrollPane(logArea), BorderLayout.SOUTH);

        // Threads
        signalThread.start();
        new VehicleProcessor(manager, signalThread, logArea, this::updateQueueDisplay).start();

        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateQueueDisplay() {
        StringBuilder sb = new StringBuilder();
        for (Vehicle v : manager.getAllVehicles()) {
            sb.append(v).append("\n");
        }
        queueArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Question6::new);
    }
}
