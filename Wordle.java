//Wordle Solver
//Created By: David Nygren
//This program strives to dwindle down possibilities of five letter words to ensure success on Wordle puzzles.

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

public class Wordle
{
    public static void main(String args[]) throws Exception{

        //Check for case
        JFrame frame = new JFrame("Wordle");
        String[] cases = {"Just greens","Just yellows","Yellows and greens","Only grays","Solved!"};
        String casE = (String)JOptionPane.showInputDialog(null, "What were you given:","Choose Case",
                JOptionPane.QUESTION_MESSAGE,null,cases,cases[0]);

        List<String> possibleWords = new ArrayList<>();
        List<String> grayLetters = new ArrayList<>();

        //Open the txt file
        File file = new File("letterList.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        boolean check = true;
        //Traverse through the file and any word with a gray letter will be removed, otherwise it will be added to arraylist possibleWords for future use
        while ((st = br.readLine()) != null){
            Scanner sc = new Scanner(st);
            while(sc.hasNext()){
                String s = sc.next();
                for (int i = 0; i< s.length(); i++){
                    for(int j = 0; j<grayLetters.size(); j++){
                        if (grayLetters.get(j).equals(s.substring(i,i+1))){
                            check = false;
                        }
                    }
                }
                if(check == true){
                    possibleWords.add(s);
                }
                check = true;
            }
        }

        //Continue to loop until solved or finished
        int count = 0;
        while(!casE.equals("Solved!") || count < 5){
            for(int x = 0; x< 11; x++){
                System.out.println("|---------------------------|");
            }
            List<String> possibleWords1 = new ArrayList<>();
            if(count > 0){
                casE = (String)JOptionPane.showInputDialog(null, "What were you given:","Choose Case",
                    JOptionPane.QUESTION_MESSAGE,null,cases,cases[0]);
            }
            //Case Questions
            String viable = "";
            String viable2 = "";
            String notViable = "";
            if(casE.equals("Just greens")){
                //User should go in order of correct placement letters and enter the slot number
                //ex: if the 'a' and 'i' in David were correct, then input "a1i3"
                viable = (String)JOptionPane.showInputDialog(frame, "Green Letters and Index:");
            }
            else if(casE.equals("Just yellows")){
                //Same rules for input as for the Greens
                viable = (String)JOptionPane.showInputDialog(frame, "Yellow Letters and Index:");
            }
            else if(casE.equals("Yellows and greens")){
                //Same rules for input as for the Greens
                viable = (String)JOptionPane.showInputDialog(frame, "Green Letters and Index:");
                viable2 = (String)JOptionPane.showInputDialog(frame, "Yellow Letters and Index:");
            }
            else if(casE.equals("Only grays") && count > 0){
                notViable = (String)JOptionPane.showInputDialog(frame, "Grayed Letters:");
            }

            //Taking the GREEN Letters and splitting the string into an arraylist
            List<String> correctLetters = new ArrayList<>();
            for (int i = 0; i<viable.length(); i++){
                correctLetters.add(viable.substring(i,i+1));
            }
            //Taking the YELLOW Letters and splitting the string into an arraylist
            for (int i = 0; i < viable.length(); i++){
                correctLetters.add(viable.substring(i,i+1));
            }
            //Taking the GRAY Letters and splitting the string into an arraylist
            for (int i = 0; i<notViable.length(); i++){
                grayLetters.add(notViable.substring(i,i+1));
            }
            //This is for when there exist YELLOW AND GREEN Letters. This arrraylist will be used for the Yellows
            List<String> correctLetters2 = new ArrayList<>();
            for (int i = 0; i<viable2.length(); i++){
                correctLetters2.add(viable2.substring(i,i+1));
            }

            //Round 1 of cases and dwindling possibleWords based on user input
            check = true;
            if(casE.equals("Only grays")){
                for (int i = 0; i < possibleWords.size(); i++){
                    for(int j = 0; j<grayLetters.size(); j++){
                        if (possibleWords.get(i).contains(grayLetters.get(j))){
                            check = false;
                        }
                    }
                    if(check == true){
                        possibleWords1.add(possibleWords.get(i));
                    }
                    check = true;
                }
                possibleWords = possibleWords1;
            }
            if(casE.equals("Just greens")){
                for(int i = 0; i<possibleWords.size(); i++){
                    for (int j = 0; j < correctLetters.size()-1; j++){
                        if(j%2==0){
                            int index = Integer.parseInt(correctLetters.get(j+1));
                            if(!(possibleWords.get(i).substring(index,index+1).equals(correctLetters.get(j)))){
                                check = false;
                            }
                        }
                    }
                    if(check == true)possibleWords1.add(possibleWords.get(i));
                    else check = true;
                }
                possibleWords = possibleWords1;
            }
            else if (casE.equals("Just yellows")){
                boolean check2 = true;
                for (int i = 0; i < possibleWords.size(); i++){
                    for (int j = 0; j < correctLetters.size()-1; j++){
                        if (j%2 == 0){
                            int index = Integer.parseInt(correctLetters.get(j+1));
                            //If the yellow is in the same spot, this word is negated from the arraylist, since yellows can't be in the same place
                            if (possibleWords.get(i).substring(index,index+1).equals(correctLetters.get(j))){
                                check = false;
                            }
                            //Create a string of all non-yellow characters
                            String nonYellows = "";
                            for(int k = 0; k < possibleWords.get(i).length(); k++){
                                if (k != index){
                                    nonYellows += possibleWords.get(i).substring(k,k+1);
                                }
                            }
                            //Now that the non-yellow string is created, check if it contains the correct letter still. If not, check = false
                            //Must use array since multiple yellows may have appeared; traverse through the size of correctLetters/2. It will always be even.
                            for (int x = 0; x< (correctLetters.size()/2); x++){
                                if(!(nonYellows.contains(correctLetters.get(j)))){
                                    check = false;
                                }
                            }
                        }
                    }
                    //If all "checks" out, then add the word to the new arraylist. Else, set check back to true and continue loop to next word in possibleWords
                    if(check == true)possibleWords1.add(possibleWords.get(i));
                    else check = true;
                }
                possibleWords = possibleWords1;
            }
            else if(casE.equals("Yellows and greens")){
                for(int i = 0; i<possibleWords.size(); i++){
                    for (int j = 0; j < correctLetters.size()-1; j++){
                        if(j%2==0){
                            int index = Integer.parseInt(correctLetters.get(j+1));
                            if(!(possibleWords.get(i).substring(index,index+1).equals(correctLetters.get(j)))){
                                check = false;
                            }
                        }
                    }
                    boolean check2 = true;
                    for (int q = 0; q < correctLetters2.size()-1; q++){
                        if (q%2 == 0){
                            int index = Integer.parseInt(correctLetters2.get(q+1));
                            //If the yellow is in the same spot, this word is negated from the arraylist, since yellows can't be in the same place
                            if (possibleWords.get(i).substring(index,index+1).equals(correctLetters2.get(q))){
                                check2 = false;
                            }
                            //Create a string of all non-yellow characters
                            String nonYellows = "";
                            for(int k = 0; k < possibleWords.get(i).length(); k++){
                                if (k != index){
                                    nonYellows += possibleWords.get(i).substring(k,k+1);
                                }
                            }
                            //Now that the non-yellow string is created, check if it contains the correct letter still. If not, check = false
                            //Must use array since multiple yellows may have appeared; traverse through the size of correctLetters/2. It will always be even.
                            for (int x = 0; x< (correctLetters2.size()/2); x++){
                                if(!(nonYellows.contains(correctLetters2.get(q)))){
                                    check2 = false;
                                }
                            }
                        }
                    }
                    if(check == true && check2 == true)possibleWords1.add(possibleWords.get(i));
                    else {check = true; check2 = true;
                    }
                }
                possibleWords = possibleWords1;
            }
            else possibleWords1 = possibleWords;

            //print out all possible words after this round
            for(int i = 0; i<possibleWords1.size(); i++){
                System.out.println(possibleWords1.get(i));
            }
            
            count++;
        }
    }
}
