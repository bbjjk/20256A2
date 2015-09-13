import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PatientManager extends JFrame{
	//CONSTANTS
	private static final String FILE_NAME = "Patient.txt";

	//Attributes
	private JTextArea mainTextArea;
	private LinkedList<Patient> PatientArray;
	private JTextField nameField;
	private JTextField phoneField;
	private JComboBox ageList;
	private JComboBox genderList;
	private JComboBox sourceList;


	//Constructor
	public PatientManager(){
		PatientArray = new LinkedList<Patient>();

		this.setTitle("General Allergy Data Manager");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container mainPanel = this.getContentPane();
		//setup top banner
		JLabel topBanner =new JLabel("Queensland Children Allergy Information System", SwingConstants.CENTER);
		topBanner.setFont(topBanner.getFont().deriveFont(24.0f));
		topBanner.setForeground(Color.blue);

		mainPanel.add(topBanner, BorderLayout.NORTH);
		this.setSize(800,600);
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem loadAction = new JMenuItem("Load");
		loadAction.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				load();
			}
		});
		fileMenu.add(loadAction);
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);

		JPanel midPanel = new JPanel(new BorderLayout());
		JPanel textAreaPanel = new JPanel();
		textAreaPanel.setBorder(BorderFactory.createTitledBorder("Display Area"));
		mainTextArea = new JTextArea();

		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(BorderFactory.createTitledBorder("Patient Data"));
		inputPanel.add(new JLabel("Patient Name"));
		nameField = new JTextField();
		nameField.setColumns(10);
		inputPanel.add(nameField);
		inputPanel.add(new JLabel("Telephone"));
		phoneField = new JTextField();
		phoneField.setColumns(10);
		inputPanel.add(phoneField);

		inputPanel.add(new JLabel("Age"));
		String[] ageArray = {"1", "2", "3", "4"};
		ageList = new JComboBox(ageArray);
		inputPanel.add(ageList);

		inputPanel.add(new JLabel("Gender"));
		String[] genderArray = {"M","F"};
		genderList = new JComboBox(genderArray);
		inputPanel.add(genderList);

		inputPanel.add(new JLabel("Source"));
		String[] sourceArray = {"Alcohol","Animal hairs","Seafood","chill & pepper"};
		sourceList = new JComboBox(sourceArray);
		inputPanel.add(sourceList);

		mainTextArea = new JTextArea("",10,50);

		textAreaPanel.add(mainTextArea);

		midPanel.add(textAreaPanel, BorderLayout.CENTER);
		midPanel.add(inputPanel, BorderLayout.NORTH);

		mainPanel.add(midPanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new GridLayout(1,4,10,5));
		bottomPanel.setBorder(BorderFactory.createTitledBorder("Command Buttons"));
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				add();
			}
		});
		bottomPanel.add(addButton);

		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				delete();
			}
		});
		bottomPanel.add(deleteButton);

		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				search();
			}
		});
		bottomPanel.add(searchButton);

		JButton sortButton = new JButton("Sort");
		sortButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				sort();
			}
		});
		bottomPanel.add(sortButton);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

		this.setVisible(true);
	}

	public void load(){
		//load data from txt file
		//create the quiz array
		PatientArray = new LinkedList<Patient>();

		//load quiz data from a file and store it in an array
		try{
			FileReader fr = new FileReader(FILE_NAME);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while((s = br.readLine()) != null) {
				if(s.length() != 0){
					//mainTextArea.append(t.toString()+"\n");
					PatientArray.add(Patient.load(s));
				}
			}
			fr.close();

			display(PatientArray);
		} catch (FileNotFoundException e) {
			System.out.println(FILE_NAME + " file was not found.");
		} catch (IOException e){
			System.out.println("I/O exception caught.");
		}
	}

	public void add(){
		String name = nameField.getText().trim();
		if(name.length() == 0){
			JOptionPane.showMessageDialog(this, "Eggs are not supposed to be green.");
			return;
		}
		String phone = phoneField.getText().trim();
		String age = (String)ageList.getSelectedItem();
		String gender = (String)genderList.getSelectedItem();
		String source = (String)sourceList.getSelectedItem();
		String patientString = String.format("%s,%s,%s,%s,%s,", name, phone, age, gender, source);
		PatientArray.add(Patient.load(patientString));
		display(PatientArray);
		
		//reset input fields
		nameField.setText("");
		phoneField.setText("");
		ageList.setSelectedIndex(0);
		genderList.setSelectedIndex(0);
		sourceList.setSelectedIndex(0);
	}

	public void delete(){
		String name = nameField.getText().trim();
		String phone = phoneField.getText().trim();
		LinkedList<Patient> tempArray = new LinkedList<Patient>(PatientArray);
		Collections.sort(tempArray, new nameAndPhoneComparator());
		int index = Collections.binarySearch(tempArray, new Patient(name, phone), new nameAndPhoneComparator());
		System.out.println(index);
		if(index >= 0){
			PatientArray.remove(tempArray.get(index));
		}
		display(PatientArray);
	}

	public void search(){
		String name = nameField.getText().trim();
		LinkedList<Patient> tempArray = new LinkedList<Patient>(PatientArray);
		Collections.sort(tempArray, new nameComparator());
		LinkedList<Patient> resultArray = new LinkedList<Patient>();
		int index = 0;
		while(index >= 0){
			index = Collections.binarySearch(tempArray, new Patient(name), new nameComparator());
			if(index >= 0){
				resultArray.add(tempArray.remove(index));
			}
		}
		display(resultArray);
	}

	public void sort(){
		if(PatientArray.size() > 0){
			Collections.sort(PatientArray, new nameComparator());
			display(PatientArray);
		}
		else{
			mainTextArea.setText("Data to be loaded.");
		}
	}

	public void display(LinkedList<Patient> patients){
		mainTextArea.setText("Patient Name\tPhone\tAge\tGender\tAllergy Source\n");
		mainTextArea.append("_________________________________________________\n\n");
		for(Patient p : patients){
			mainTextArea.append(p.toString()+"\n");
		}
		mainTextArea.append("\nTotal "+patients.size()+" entries!");
	}

	private class nameAndPhoneComparator implements Comparator<Patient> {
		public int compare(Patient p1, Patient p2) {
			if(p1.getName().compareTo(p2.getName()) == 0){
				return p1.getPhone().compareTo(p2.getPhone());
			}else{
				return p1.getName().compareTo(p2.getName());
			}
		}
	}

	private class nameComparator implements Comparator<Patient> {
		public int compare(Patient p1, Patient p2) {
			return p1.getName().compareTo(p2.getName());
		}
	}


	public static void main(String args[]){
		PatientManager g = new PatientManager();
	}
}
