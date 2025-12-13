package Medicare;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UI extends JFrame {
    JComboBox<Doctor> cbDoctors;
    JComboBox<LocalTime> cbTimes;
    JSpinner dateSpinner;
    JTextField tfPatientName, tfPhone;
    DefaultTableModel apModel;

    public UI() {
        setTitle("MediCare Plus — Appointment Scheduler");
        setSize(850,520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
        setVisible(true);
    }

    private void init() {
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        left.add(new JLabel("Select Doctor:"));
        cbDoctors = new JComboBox<>(Datastore.store.doctors.toArray(new Doctor[0]));
        left.add(cbDoctors);

        left.add(new JLabel("Select Date:"));
        dateSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor de = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(de);
        left.add(dateSpinner);

        left.add(new JLabel("Select Time Slot:"));
        cbTimes = new JComboBox<>(Scheduler.generateSlots().toArray(new LocalTime[0]));
        left.add(cbTimes);

        left.add(Box.createRigidArea(new Dimension(0,10)));
        left.add(new JLabel("Patient name:"));
        tfPatientName = new JTextField();
        left.add(tfPatientName);
        left.add(new JLabel("Phone:"));
        tfPhone = new JTextField();
        left.add(tfPhone);

        JButton btnBook = new JButton("Book Appointment");
        btnBook.addActionListener(e -> bookAppointment());
        left.add(Box.createRigidArea(new Dimension(0,10)));
        left.add(btnBook);

        // Right panel: appointments table
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        apModel = new DefaultTableModel(new String[]{"ID","Doctor","Patient","Date","Time","Status"},0) {
            public boolean isCellEditable(int r,int c){return false;}
        };
        JTable table = new JTable(apModel);
        refreshAppointmentsTable();
        right.add(new JScrollPane(table), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setResizeWeight(0.33);
        add(split);
    }

    private LocalDate spinnerDate() {
        Date d = (Date) dateSpinner.getValue();
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void refreshAppointmentsTable() {
        apModel.setRowCount(0);
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
        for (Appointment ap : Datastore.store.appointments) {
            Doctor doc = findDoctor(ap.doctorId);
            Patient p = findPatient(ap.patientId);
            apModel.addRow(new Object[]{ap.id, doc!=null?doc.name:ap.doctorId, p!=null?p.name:ap.patientId, ap.date.toString(), ap.time.format(tf), ap.status});
        }
    }

    private Doctor findDoctor(String id) {
        for (Doctor d : Datastore.store.doctors) if (d.id.equals(id)) return d;
        return null;
    }
    private Patient findPatient(String id) {
        for (Patient p : Datastore.store.patients) if (p.id.equals(id)) return p;
        return null;
    }

    private void bookAppointment() {
        String patientName = tfPatientName.getText().trim();
        String phone = tfPhone.getText().trim();
        if (patientName.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter patient name and phone.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Doctor doc = (Doctor) cbDoctors.getSelectedItem();
        LocalDate date = spinnerDate();
        LocalTime time = (LocalTime) cbTimes.getSelectedItem();

        // Check availability
        if (!Scheduler.isAvailable(doc.id, date, time)) {
            JOptionPane.showMessageDialog(this, "Selected slot is already taken. Choose another slot.", "Unavailable", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // create or find patient
        Patient patient = null;
        for (Patient p : Datastore.store.patients) {
            if (p.name.equalsIgnoreCase(patientName) && p.phone.equals(phone)) { patient = p; break; }
        }
        if (patient == null) {
            String newId = "P" + String.format("%03d", Datastore.store.patients.size()+1);
            patient = new Patient(newId, patientName, phone);
            Datastore.store.patients.add(patient);
        }

        Appointment ap = new Appointment(UUID.randomUUID().toString(), doc.id, patient.id, date, time, "Confirmed");
        Datastore.store.appointments.add(ap);
        Datastore.store.save();

        // Patient simple popup notification
        JOptionPane.showMessageDialog(this,"Appointment Confirmed!\n\n" +"Patient: " + patient.name + "\n" +"Doctor : " + doc.name + "\n" +"Date   : " + date + "\n" +"Time   : " + time,"Patient Notification",JOptionPane.INFORMATION_MESSAGE);

    }
}
