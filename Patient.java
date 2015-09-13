public class Patient{
	//Attributes
	private String name;
	private String phone;
	private int age;
	private char gender;
	private String allergySource;

	//Constuctor
	public Patient(String n, String p, int a, char g, String s){
		name = n;
		phone = p;
		age = a;
		gender = g;
		allergySource = s;
	}

	public Patient(String n, String p){
			name = n;
			phone = p;
			age = 1;
			gender = 'M';
			allergySource = "";
	}

	public Patient(String n){
		name = n;
		phone = "";
		age = 1;
		gender = 'M';
		allergySource = "";
	}

	public String getName(){
		return name;
	}

	public String getPhone(){
		return phone;
	}

	public static Patient load(String data){
		String[] strArray = data.split(",");
		String n = strArray[0];
		String p = strArray[1];
		int a = Integer.parseInt(strArray[2]);
		char g = strArray[3].charAt(0);
		String s = strArray[4];
		return new Patient(n, p, a, g, s);
	}

	public String toString(){
		return String.format("%s\t%s\t%d\t%s\t%s\t", name, phone, age, gender, allergySource);
	}

	public int compareTo(Patient p){
		return name.compareTo(p.name);
	}
}