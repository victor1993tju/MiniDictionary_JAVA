package project;
import java.io.*; // for File
import java.util.*; // for Scanner
public class CheckerMain {
	//in the end, I can do  input error avoiding	
	public static void main(String[] args)throws FileNotFoundException {
		Scanner input=new Scanner(System.in);
		SpellChecker sc=new SpellChecker();//a new object
		String frontword="****";
		String[] dics={"compsci.txt", "english.txt", "english-proper.txt", "yoga.txt"};
		
		sc.print("welcome to SpellChecker! input a word to check or input \"?help\" to see the command menu.");		
		
		for(;;){
			System.out.print(">>");			
			String word=input.nextLine();	
			if(!sc.checkCommand(word)&&checkDic(sc)){//check whether input is an member of command or not.
				//check whether dictionary list is null or not.
				{
					if(sc.contains(word))
					sc.print(word+" is a corret word");
				else 
					{frontword=word;//store the front word, then we can use it in add function	
					guess(sc,word);
				    }
			 	}
			}
			else doCommand(word,frontword,sc,input,dics); 
		}
	}
	
	public static boolean checkDic(SpellChecker sc){
		if(sc.dictionaryList().length==0){
			sc.print("No dictionary loaded in programe");
			return false;
		}
		else return true;
	
	}
	public static void guess(SpellChecker sc,String word){
		System.out.println("The word is not correct.");
		String[] guessword=sc.guess(word);
		String[] guesstype={"less: ","more: ","diff: ","swap: "};
		for(int i=0;i<guessword.length;i++)
		{   sc.print(guesstype[i]+guessword[i]);					
		}
	}
	public static void doCommand(String word,String frontword,SpellChecker sc,Scanner input,String[] dics)throws FileNotFoundException{		
		if(word.equals("?add"))
			doAdd(frontword,sc,input);
		else if(word.equals("?default"))
			doDefault(sc);		
		else if(word.equals("?help"))
			doHelp(sc);
		else if(word.equals("?list"))
			doList(sc);
		else if(word.equals("?load"))
			doLoad(sc,input,dics);
		else if(word.equals("?quit"))
			doQuit(sc,input);
		else if(word.equals("?unload"))
			doUnload(sc,input);	
		else sc.print("Wrong operation,input \"?help\" to see the help list");
	}
	public static void doHelp(SpellChecker sc){
		sc.help();
	}
	
	public static void doList(SpellChecker sc){
		if(checkDic(sc))
			printStr(sc.dictionaryList());
	}
	
	public static void doAdd(String frontword,SpellChecker sc,Scanner input){
		sc.print("Enter the name of dictionary you want to add the word");
		doList(sc);
		
		String name=checklist(sc,input,sc.dictionaryList());
		sc.addWord(frontword,name);

	}
	
	public static String checklist(SpellChecker sc,Scanner input,String[] s){
		int num=input.nextInt();
		while(num<=0||num>s.length){	
			sc.print("Wrong number, write it again!");
			num=input.nextInt();
		}
		input.nextLine();
		return s[num-1];
		
	}
		
	public static void doLoad(SpellChecker sc,Scanner input,String[] dics)throws FileNotFoundException{
		sc.print(dics.length+" dictionaries in computer:");
		printStr(dics);
		sc.print(sc.dictionaryList().length +" dictionaries in programe: ");
		doList(sc);
		sc.print("Enter the name of dictionary you want to load now");
		
		String name=checklist(sc,input,dics);
		sc.load(name);
		//print("Dictionary "+name+" has been loaded now");
		
	}

	public static void doUnload(SpellChecker sc,Scanner input){
		sc.print("Enter the name of dictionary you want to unload");
		doList(sc);
		
		String name=checklist(sc,input,sc.dictionaryList());
		sc.unload(name);	
	}
	
	public static void doDefault(SpellChecker sc)throws FileNotFoundException{
		sc.rewrite();
	}
	
	public static void doQuit(SpellChecker sc,Scanner input)throws FileNotFoundException{
		if(sc.checkAdd())
		{
			sc.print("New words added in dictionary, Are you sure to save the word before quit? input: \"yes\" or \"no\"");
			String choose=input.nextLine();
			if(choose.equals("yes"))
				sc.saving();//if we do not default, we do not rewritten the file from map value
		}
		sc.print("Thank you for using DiCo.");
		System.exit(0);
	}
	
	public static void printStr(String[] s)
	{
		for(int i=1;i<=s.length;i++)
			System.out.print("  "+i+"."+s[i-1]+"  ");
		System.out.println();
	}	
}
