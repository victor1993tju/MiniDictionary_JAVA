package project;
import java.util.*;
import java.io.*;
//File Scanner in here. put console  Scanner as a word in main function
//dictionary as map
public class SpellChecker {
		private Map<String, ArrayList<String>> dictionary = new HashMap<String, ArrayList<String>>();
		private Map<String, ArrayList<String>> dchanged = new HashMap<String, ArrayList<String>>();
		private boolean addsign=false;
		Scanner conffile=new Scanner(new File("configuration.txt"));	
		public String[]defaultname;///////
		//initiation by configuration 
		//the first time.the configuration is empty
		//if not the first time,use the dictionaries loaded last time
		public SpellChecker()throws FileNotFoundException{
			String dictName;
			defaultname();
			for(int i=0;i<defaultname.length;i++)
				{dictName=defaultname[i];
				Scanner dict = new Scanner(new File(dictName));//read the dictionary
				ArrayList<String> values=new ArrayList<String>();//only used in fill map.
				while(dict.hasNextLine()){					
					values.add(dict.nextLine());
				}				
				dictionary.put(dictName, values);
			}	
		}		
		//check if a word is in the dictionaries which have been loaded 
		public void defaultname(){//return the name of dictionary of default.
			ArrayList<String> name = new ArrayList<String>();
			while(conffile.hasNextLine())
				name.add(conffile.nextLine());//read the configuration file line by line
			defaultname =name.toArray(new String[0]);
			//System.out.print(name.toString()+"->");//test

		}
		
		public boolean contains(String str) {
			for(String name:dictionary.keySet())
				{if(dictionary.get(name).contains(str))
					return true;}
			return false;
		}
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//provide the user with the possible guesses of the correct words
		//for example: (X is the input word, Y is the correct word)
		//X has one letter more than Y
		//X has one letter less than Y
		//X has one letter which is different in Y
		//X becomes similar to Y by swapping 
		public String[] guess(String word) {
			
			String[] guesses = { more(word), less(word), diff(word), swap(word) };///////////////////what is the problem?
			print("guess(" + word + ")");
			return guesses;
		}
		
		//provide a guess/guesses similar to the input word by swapping two consecutive letters
		private String swap(String w) {
			ArrayList<String> guess_of_swap = new ArrayList<String>();	
			//swap every two consecutive letters in the word
			//to see if the new word is in the word list
			for(int i=0; i<w.length()-1; i++){
				char temp1 = w.charAt(i);
				char temp2 = w.charAt(i+1);
				String newWord = w.substring(0, i) + temp2 + temp1 + w.substring(i+2, w.length());
				for(String name:dictionary.keySet()){
				if(dictionary.get(name).contains(newWord)){
					if(!guess_of_swap.contains(newWord)){
						guess_of_swap.add(newWord);
					}
				}
				}
			}
			return (guess_of_swap.toString()).substring(1, (guess_of_swap.toString()).length()-1);/////////////////////////记得        substring the opening and closing brackets	
		}
		
		//provide a guess/guesses with one letter different with the input word
		private String diff(String w) {
			ArrayList<String> guess_of_diff = new ArrayList<String>();		//build an ArrayList to store the guesses
			
			//build an Arraylist contains words of same number of letters with the input word
			ArrayList<String> pick= new ArrayList<String>();
			for(String name:dictionary.keySet()){
			for(int i=0; i<dictionary.get(name).size(); i++){
				if((dictionary.get(name).get(i)).length() == w.length()){
					pick.add(dictionary.get(name).get(i));
				}
			}
			}
			for(int j=0; j<pick.size();j++){
				for(int i=0; i<w.length(); i++){
					String newWord_1 = (pick.get(j)). substring(0,i) +(pick.get(j)).substring(i+1, w.length());
					String newWord_2 = w. substring(0,i) + w.substring(i+1, w.length());
					if(newWord_1.equals(newWord_2)){
						if(!guess_of_diff.contains(pick.get(j))){
							guess_of_diff.add(pick.get(j));
						}
					}
				}
			}
			return (guess_of_diff.toString()).substring(1, (guess_of_diff.toString()).length()-1);	/////////////////////记得        substring the opening and closing brackets									
		}

		//provide a guess/guesses with one letter more than the input word
		private String less(String w) {
			ArrayList<String> guess_of_less = new ArrayList<String>();			//build an ArrayList to store the guesses
			
			//build an Arraylist contains words with one more letters than the input word
			ArrayList<String> pick= new ArrayList<String>();
			for(String name:dictionary.keySet()){
			for(int i=0; i<dictionary.get(name).size(); i++){
				if((dictionary.get(name).get(i)).length() == w.length()+1){
					pick.add(dictionary.get(name).get(i));
				}
			}
			}
			//remove every character of the possible words to check if the new word is the input word
			//if yes, the possible word is one of the guesses
			for(int j=0; j<pick.size();	j++){
				for(int i=0; i<w.length()+1; i++){							
					String newWord = (pick.get(j)). substring(0,i) + (pick.get(j)).substring(i+1, w.length()+1);
					if(w.equals(newWord)){	
						if(!guess_of_less.contains(pick.get(j))){
							guess_of_less.add(pick.get(j));////////////////////////////////////数组中间有null值怎么办？将来是直接输出吗？
						}
					}	
				}
			}
			return (guess_of_less.toString()).substring(1, (guess_of_less.toString()).length() -1);
		}

		//provide a guess/guesses with one letter less than the input word
		private String more(String w) {
			ArrayList<String> guess_of_more = new ArrayList<String>();
			//remove every character of the word to check if the new word is in the word list
			for(int i=0; i< w.length(); i++){
				String newWord = (w. substring(0,i) + w.substring(i+1, w.length()));
				for(String name:dictionary.keySet()){
				if(dictionary.get(name).contains(newWord)){
					if(!guess_of_more.contains(newWord)){
						guess_of_more.add(newWord);
					}
				}
				}
			}
			return (guess_of_more.toString()).substring(1, (guess_of_more.toString()).length() -1);//////////////////////记得        substring the opening and closing brackets	
		}
			
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		public void load(String dictName) throws FileNotFoundException{
			Scanner file=new Scanner(new File(dictName));
			ArrayList<String> values=new ArrayList<String>();
			while(file.hasNextLine()){				
				values.add(file.nextLine());
				dictionary.put(dictName, values);//modify the map		
			//rewrite();//modify the configuration file
			}
		}		
		public void unload(String dictName) {
			dictionary.remove(dictName);//modify the map

		}
		
		public void rewrite()throws FileNotFoundException{
			PrintStream configuration=new PrintStream(new File("configuration.txt"));
			for(String name:dictionary.keySet())
				configuration.println(name);
		}
		
		//print the list of dictionaries that have already been loaded 
		public String[] dictionaryList() {
			ArrayList<String> dlist = new ArrayList<String>();		
			for(String name:dictionary.keySet())
				dlist.add(name);
			String[] dictionaryList =dlist.toArray(new String[0]);
			return dictionaryList;

		}				
		//add a new word to a  given dictionary
		public void addWord(String newWord, String dictName) {
			ArrayList<String> values=new ArrayList<String>();
			values = dictionary.get(dictName);
			values.add(newWord);			
			dchanged.put(dictName, values);
			addsign=true;		
		}
		
		//check if there is any new word is added 
		public boolean checkAdd(){
			return addsign;
		}
		
		//save the change to the dictionary
		public void saving()throws FileNotFoundException
		{
			for(String dictName:dchanged.keySet()){	
				PrintStream dictupdate=new PrintStream(new File(dictName));		
				for(String word:dictionary.get(dictName))
					dictupdate.println(word);
			}		
		}
		
		//check if word is an command return true
		public boolean checkCommand(String word){
			if (word.startsWith("?")||word.equals(""))//solve the enter and space problem 
				return true;
			else return false;
		}
		
		//list the commands you can operate
		public void help() {
			print("?help: list the commands you can operate\n?list: list the loaded dictionaries");
			print("?load: load a new dictionary\n?unload: unload a dictionary");
			print("?add: add new words to dictionaries\n?quit: quit the program");
			print("?default: store the current list as the default list");
		}
		
		public void print(String s)
		{System.out.println(s);}

	}