//import java.util.*;
import java.io.*;
//import javax.swing.JOptionPane;
import java.net.URL;



public class Database { 
	TriaxialTest[] allSamples = new TriaxialTest[30]; 
	int nextSamplePosition = 0; 
	String filename = "samples.txt";
	String fileName = "resources/samples.txt";
	
	int counter = 0;
	
	
	//writes data to file
	public void writeSamplesListToFile() {
		try {
			File file = new File(filename);
			FileWriter fw = new FileWriter(file);//file to write to(overwrites original)
			for(int i = 0; i < nextSamplePosition;i++){//for loop through records
				fw.write(allSamples[i].sampleToString()+"");//writes record to file
				fw.write("\r\n");//move to a new line
			}
			fw.close();//closes file write
		} catch(Exception e){//in case of write error runs this
			e.printStackTrace();//outputs error message
		}
	}
	
	//writes data to file
	public void export(File file) {
		try {
			FileWriter fw = new FileWriter(file);//file to write to(overwrites original)
			for(int i = 0; i < nextSamplePosition;i++){//for loop through records
				fw.write(allSamples[i].sampleToString()+"");//writes record to file
				fw.write("\r\n");//move to a new line
			}
			fw.close();//closes file write
		} catch(Exception e){//in case of write error runs this
			e.printStackTrace();//outputs error message
		}
	}
    
	//method reads file
	public void readSamplesFromFile() {
		//defines variables for program
		//reads line by line and then passes data until the end of the file is reached
		String[] tempArray = new String[6];// array to store record data
		nextSamplePosition = 0;//sets to 0 for later incrementing
		try {
			File file = new File(filename);
			BufferedReader br = new BufferedReader(new FileReader(file));//defines where to read from as br
			String temp = ""; //variable holds initial line read result
			temp = br.readLine()+"";//reads next line
			while(temp != null && temp.equals("null") == false) { //loops until the end of the file is reached
				tempArray = temp.split(","); //separates items by forward slash into array
				
				allSamples[nextSamplePosition] = new TriaxialTest(tempArray[0], Double.parseDouble(tempArray[1]), Double.parseDouble(tempArray[2]), Double.parseDouble(tempArray[3]), 
						Double.parseDouble(tempArray[4]), Double.parseDouble(tempArray[5]));//adds tempTeam to the array of records
				nextSamplePosition++;
				temp = br.readLine()+"";//reads next line
			}
			br.close();//closes reading
		}
		catch(Exception e){//if read error
			System.out.println("Read error");//states error
			e.printStackTrace();//prints error created
		}
	}
	
	//method reads file inputted and adds
	//the samples within to file
	public String batchRead(File file) {
		//Adds extra samples to file
		//reads line by line and then passes data until the end of the file is reached
		readSamplesFromFile();
		String[] tempArray = new String[6];// array to store record data
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String temp = ""; //variable holds initial line read result
			temp = br.readLine()+"";//reads next line
			while(temp != null && temp.equals("null") == false) { //loops until the end of the file is reached
				tempArray = temp.split(","); 
					
				if (searchByName(tempArray[0]) != null)//adds tempTeam to the array of records
					return tempArray[0];
				temp = br.readLine()+"";//reads next line
			}
			br.close();//closes reading
			
			br = new BufferedReader(new FileReader(file));
			temp = ""; //variable holds initial line read result
			temp = br.readLine()+"";//reads next line
			while(temp != null && temp.equals("null") == false) { //loops until the end of the file is reached
				tempArray = temp.split(","); 
					
				allSamples[nextSamplePosition] = new TriaxialTest(tempArray[0], Double.parseDouble(tempArray[1]), Double.parseDouble(tempArray[2]), Double.parseDouble(tempArray[3]), 
						Double.parseDouble(tempArray[4]), Double.parseDouble(tempArray[5]));//adds tempTeam to the array of records
				nextSamplePosition++;
				temp = br.readLine()+"";//reads next line
			}
			br.close();//closes reading
		}
		catch(Exception e){//if read error
			System.out.println("Read error");//states error
			return "0";
		}
		sort();
		writeSamplesListToFile();
		return "1";
	}

	//creates a record to be added to the array
	public boolean addSample(TriaxialTest test) {
		//readSamplesFromFile(); //reads file to update record array
		if(search(test.name) == null) {
			allSamples[nextSamplePosition] = test;
			nextSamplePosition++;
			sort();
			writeSamplesListToFile();//writes the updated array to file
			return true;
		}
		return false;
	}

	//method searches by name and returns string
	public String search(String searchValue) {
		//linear search by team name
		//will only return 1 value
		readSamplesFromFile();//reads file to update record array
		for(int i = 0; i < nextSamplePosition ; i++) {//loops through all of records
			if(allSamples[i].name.equals(searchValue)) {//loops through
				return i+"";//converts current item to string
			}
		}
		return null;//if the searchvalue wasn't found
	}
	
	//method searches by name and returns string
	public TriaxialTest searchByName(String searchValue) {
		//linear search by team name
		//will only return 1 value
		readSamplesFromFile();//reads file to update record array
		for(int i = 0; i < nextSamplePosition ; i++) {//loops through all of records
			if(allSamples[i].name.equals(searchValue)){//loops through
				System.out.println(allSamples[i].name);
				return allSamples[i];//converts current item to string
			}
		}
		return null;//if the searchvalue wasn't found
	}

	//method searches by name and returns string
	public int deleteByName(String searchValue) {
		//linear search by team name
		//will only return 1 value
		readSamplesFromFile();//reads file to update record array
		for(int i = 0; i < nextSamplePosition ; i++) {//loops through all of records
			System.out.println("SV:"+searchValue+"ds:"+allSamples[i].name);
			if(allSamples[i].name.equals(searchValue)){//loops through
				nextSamplePosition--;
				for(int j = i; j < nextSamplePosition;j++){
					allSamples[j] = allSamples[j+1];
				}
				sort();
				writeSamplesListToFile();//updates Team record
				return i;//converts current item to string
			}
		}
		sort();
		return -1;//if the searchvalue wasn't found
	}
	
	//method searches by name and returns string
	public int editByName(String searchValue, double Mi, double li, double ki, double Ni, double nui) {
		//linear search by team name
			//will only return 1 value
		readSamplesFromFile();//reads file to update record array
		for(int i = 0; i < nextSamplePosition ; i++) {//loops through all of records
			if(allSamples[i].name.equals(searchValue)){//loops through
				allSamples[i] = new TriaxialTest(searchValue, Mi, li, ki, Ni, nui);//adds tempTeam to the array of records
				sort();
				writeSamplesListToFile();//updates Team record
				return i;//converts current item to string
			}
		}
		sort();
		return -1;//if the searchvalue wasn't found
	}
	
	//deletes sample from the file
	public void deleteSample(int searchValue) {
		//goes to records position
		//overwrites current record with next record
		//assigns new name to current record
		readSamplesFromFile();//reads file to update record array
		nextSamplePosition--;
		for(int j = searchValue; j < nextSamplePosition;j++){
			allSamples[j] = allSamples[j+1];
			allSamples[j].name = (j+1)+"";
		}
		sort();
		writeSamplesListToFile();//updates Team record
	}
	
	public void sort() {
        TriaxialTest temp;  
        for(int i=0; i < nextSamplePosition; i++){  
        	for (int j=1; j < (nextSamplePosition-i); j++) {
        		for (int p=0; p < allSamples[j].name.length()-1; p++) {
        			//System.out.println(allSamples[j].name+":"+Character.toLowerCase(allSamples[j].name.charAt(0))+":"+(int)allSamples[j].name.charAt(p)+":"+allSamples[j].name.length());
        			//System.out.println(allSamples[j-1].name+":"+Character.toLowerCase(allSamples[j-1].name.charAt(0))+":"+(int)allSamples[j-1].name.charAt(p)+":"+allSamples[j-1].name.length());
        			if (p >= allSamples[j-1].name.length()) {
	                	temp = allSamples[j-1];
	                	allSamples[j-1] = allSamples[j];
	                	allSamples[j] = temp;
	                	break;
	             	}
        			if ((int)Character.toLowerCase(allSamples[j-1].name.charAt(p)) != (int)Character.toLowerCase(allSamples[j].name.charAt(p)))
	        			if((int)Character.toLowerCase(allSamples[j-1].name.charAt(p)) > (int)Character.toLowerCase(allSamples[j].name.charAt(p))) {
		                	temp = allSamples[j-1];  
		                	allSamples[j-1] = allSamples[j];  
		                	allSamples[j] = temp;
		                	break;
		             	} else {
		             		break;
		             	}
        		}
        	}
        }  
    }  


	public TriaxialTest getSampleAt(int position) {
		return allSamples[position];
	}

	public int getNextSamplePosition() {
		return nextSamplePosition;
	}
	
	//Array of sample names
	public String[] getSampleNames() {
		readSamplesFromFile();
		String[] s = new String[nextSamplePosition];
		readSamplesFromFile();//reads file to update record array
		for(int i = 0; i < nextSamplePosition ; i++) {//loops through all of records
			s[i] = allSamples[i].name;
				
		}
		return s;
	}
}